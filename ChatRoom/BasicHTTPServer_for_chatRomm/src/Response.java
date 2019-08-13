
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Response {

	byte[] bytelist;
	File file;
	public OutputStream output;

	// for web socket
	Response(String declare, Socket connection, String code) throws IOException {
		this.output = connection.getOutputStream();
		output.write(("HTTP/1.1 101 Switching Protocols\n" + "Upgrade: websocket\n" + "Connection: Upgrade\n"
				+ "Sec-WebSocket-Accept:").getBytes());
		output.write((code + "\n\n").getBytes("UTF-8"));
		output.flush();
	}

	// for normal output
	Response(Socket connection, String filename) throws IOException, InterruptedException {
		this.output = connection.getOutputStream();
		file = new File("resource/" + filename);
		if (!file.exists() || file.isDirectory()) {
			output.write(("HTTP/1.1" + "404 not found\r\n\r\n".getBytes()).getBytes());
			output.flush();
		}

		bytelist = new byte[(int) file.length()];
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
		stream.read(bytelist, 0, bytelist.length);

		output.write(("HTTP/1.1 200 OK" + "\r\n").getBytes("UTF-8"));
		output.write(("Content-Length: " + bytelist.length + "\r\n\r\n").getBytes());

		output.write(bytelist, 0, bytelist.length);
		output.flush();
//for (int i =0; i<bytelist.length; i+=100){
//int length =100;
//if (bytelist.length-i<100) {
//	length = bytelist.length-i;
//}
// output.write(bytelist,i,length);
// output.flush();
// Thread.sleep(100);
//}
		output.close();
	}
}
