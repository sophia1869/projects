
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SEndPoint {

	static boolean isClose;
	
	static void echo1(OutputStream output,byte[] combine) throws IOException  {
		
		int headerl = (combine[1]==127)? 9 :((combine[1]==126)?3:2);
		//extract username and msg from the combined name byte[]
		byte[] frank = new byte[combine.length-headerl];
		for (int i=0; i<frank.length; i++) {
			frank[i]= combine[i+headerl];
		}
	    String line = new String(frank);
	    String[] parts = line.split(" ");
	    String userName = parts[0];
	    String msg = "";
	    for (int i=1; i<parts.length;i++) {
	    	msg += (parts[i]+" ");
	    }
	    String myString = "{ \"user\" : \"" + userName + "\", \"message\" : \"" + msg + "\" }";
	    
	    //combine[1]=126,combine[2][3]=myString.getBytes().length;
	    //combine[1]=127,combine[2]~[9]=myString.getBytes().length;
	    combine[1]= (byte) myString.getBytes().length; 
	 
	    byte[] ready = new byte[headerl+myString.getBytes().length];
	    ready[0] = combine[0];
	    for (int i=1; i<headerl; i++) {
	    	
	    	ready[i] = combine[i];
	    }
	    for (int i=headerl; i<ready.length;i++) {
	    	ready[i]=myString.getBytes()[i-headerl];
	    }
	    output.write(ready);
		output.flush();
	}
	
	
	static byte[] read (Socket connection) throws IOException {
		byte[] info = new byte[2];
		connection.getInputStream().read(info, 0, 2);
		
		// MDN: Decoding Payload Length:
		// Read bits 9-15 (inclusive) and interpret that as an unsigned integer
		long uns = info[1] & 127;
		long delength = 0;
		byte[] maskingKey = null;
		int l =2;
		byte[] info126 = new byte[2];
		byte[] info127 = new byte[8];
		
		if((info[0] & 0xF) ==8) {
			isClose = true; 
		}
		if ((info[0] & 127) == 1) { 
			if (uns == 126) {
				// Read the next 16 bits and interpret those as an unsigned integer.
				connection.getInputStream().read(info126, 0, 2);		
				delength = (info[0] & 127) << 8 | (info[1] & 127);
				l=3;
			}
			if (uns == 127) {
				// Read the next 64 bits and interpret those as an unsigned integer
				connection.getInputStream().read(info127, 0, 8);
				delength = (info127[ 0] & 127) << 56 | (info127[1] & 127) << 48
						| (info127[ 2] & 127) << 40 | (info127[3] & 127) << 32
						| (info127[ 4] & 127) << 24 | (info127[5] & 127) << 16
						| (info127[6] & 127) << 8 | info127[7] & 127;
				l=9;
			} else {
				// If it's 125 or less, then that's the length
				delength = uns;
			}
			
			// find masking key which is a byte[] array with 4 elements inside.
			maskingKey = new byte[4];
			connection.getInputStream().read(maskingKey, 0, 4);
		}
		
		//127 takes 8 bytes, might overflow. need further care----
		byte[] decode = new byte[Math.toIntExact(delength)]; 
		byte[] tempIn = new byte[1];
		for (int i = 0; i < delength; i++) {
			connection.getInputStream().read(tempIn);
			decode[i] = (byte) (tempIn[0] ^ maskingKey[i % 4 ]);
		}
		
		byte[] infoh = (uns ==126)? info126 :((uns==127)? info127:info);
		byte[] combine = new byte[infoh.length+decode.length];
		for (int i = 0; i < combine.length; ++i)
		{
		    combine[i] = i < infoh.length ? info[i] : decode[i - infoh.length];
		}
		return combine;
	}
	
	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(8080));
		
		while (true) {
			SocketChannel sc = ssc.accept();
			Socket connection = sc.socket();
		
			Thread connect = new Thread(new Runnable() {
				public void run() {
					
						Request myrequest = null;
						try {
							myrequest = new Request(connection);
						} catch (NoSuchAlgorithmException | IOException | BadRequestException e) {
							e.printStackTrace();
						}

						if (myrequest.encoded.equals("non")) {
							// no web socket, normal response
							String requestfile = myrequest.requestfile;
							try {
								new Response(connection, requestfile);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
						} else {
							try {
								Response myre = new Response("ws", connection, myrequest.encoded);
							} catch (IOException e1) {
								e1.printStackTrace();
							}

								try {
									byte [] combine = SEndPoint.read(connection);
									System.out.println("Main Server : "+new String(combine));
									String joinRequest = new String(combine);
									Room theRoom = null;
									
									if (joinRequest.contains ("join")) {
										String[] parts = joinRequest.split(" ");
										String roomname= parts[parts.length-1];
										
										boolean roomFound = false;
										
										for (Room r:Room.roomNow) {
											if (r.roomName.equals(roomname)) {
												roomFound = true;
													r.addClient(sc);
											}
										}
										if(roomFound == false) 
										{
												theRoom = new Room(roomname,sc);
										}
									}								
	
								} catch (IOException e) {
									e.printStackTrace();
								}								
						}
				}
			});
			connect.start();
		}

		// close one thing in the socket(here is the output), the other sockets will
		// close automatically,
		// no need to close ss or connection.
	}
}
