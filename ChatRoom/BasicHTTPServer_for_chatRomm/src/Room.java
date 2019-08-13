import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Room {
	
	static ArrayList<Room> roomNow = new ArrayList <> ();
	String roomName;
	Selector sel;
	ArrayList <SocketChannel> myChannels;
	HashSet <SocketChannel> regiChannels;
	ArrayList <byte[]> historyMessage;
	
	Room(String roomName, SocketChannel sc) throws IOException{
	this.roomName = roomName;
	roomNow.add(this);
	sel = Selector.open();
	myChannels = new ArrayList <>();
	regiChannels = new HashSet <>();
	historyMessage = new ArrayList<>();
	
	System.out.println("Creating room" + roomName);
	
	addClient (sc);
	Thread roomthread = new Thread(new Runnable() {
		public void run() {
				try {
					
						listen();
					
				} catch (IOException e) {
					e.printStackTrace();
				}	 										
		}		
	});
	roomthread.start();
	}		  
		
	  void listen() throws IOException {
		  while(true) { 
			  for (SocketChannel wsc:myChannels) {
				    //System.out.println(wsc);
					wsc.configureBlocking(false);//unblock to register
					wsc.register(sel, SelectionKey.OP_READ);
					regiChannels.add(wsc);
					//System.out.println("client regi");
				}
			  
			sel.select(); //blocks until any of the monitored channels are ready
			Set<SelectionKey> readySet = sel.selectedKeys(); //get a set of keys that are ready
			Iterator<SelectionKey> it = readySet.iterator(); 
			
			while(it.hasNext()){ 
			  
			  SelectionKey key = it.next(); 
			  
			  if(key.isReadable()){ 
				    it.remove(); 
					SelectionKey key2 = key.channel().keyFor(sel);
			        key2.cancel();
			       
			        SocketChannel sc = ((SocketChannel) key.channel());
			        sc.configureBlocking(true);
			        
			        byte[] b = SEndPoint.read(sc.socket());
			        historyMessage.add(b);
			        
			        if (!SEndPoint.isClose) {
			        	
			        sc.configureBlocking(false);
					sel.selectNow();  
					key2.channel().register(sel, SelectionKey.OP_READ);
					
			        for (SocketChannel s:regiChannels) {
			        	SelectionKey key3 = s.keyFor(sel);
				        key3.cancel();
				        s.configureBlocking(true);
			        	
			        	SEndPoint.echo1(s.socket().getOutputStream(),b);
			        	
			        	s.configureBlocking(false);
						sel.selectNow();  
						key3.channel().register(sel, SelectionKey.OP_READ);
			        }
			        
			        }else {
			        	regiChannels.remove(sc);
			        	sc.socket().close();
			        	
			        }
			        
			        
			        
			  }
			}
		  }
	  }

	synchronized void addClient (SocketChannel sc) throws IOException {
		for (byte[] m:historyMessage) {
			SEndPoint.echo1(sc.socket().getOutputStream(), m);
		}
		System.out.println("client added");
		myChannels.add(sc);
		sel.wakeup();
	}
}
