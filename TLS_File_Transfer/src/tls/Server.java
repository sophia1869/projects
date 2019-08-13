package tls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Server {

	ObjectOutputStream ousServer;
	ObjectInputStream insServer;
	byte[] rnonce = new byte[32];
	
	CertificateFactory cf;
	X509Certificate serverCertificate;
	BigInteger serverDHPrivateKey, serverDHPublicKey;
	PrivateKey serverKey;
	byte[] signedServerDHPubKey;
	
	X509Certificate rclientCertificate;
	BigInteger rclientDHPublicKey;
	byte[] rsignedClientDHPubKey;
	
	BigInteger serverShared;

	SecretKeySpec serverEncrypt,clientEncrypt,serverMAC,clientMAC;
	IvParameterSpec serverIV, clientIV; 
	
  void makeSecretKeys(byte[] clientNonce, byte[] sharedSecretFromDiffieHellman) throws NoSuchAlgorithmException, InvalidKeyException {
		
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(clientNonce, "HmacSHA256");
		sha256_HMAC.init(secret_key);
		byte[] prk = sha256_HMAC.doFinal(sharedSecretFromDiffieHellman);
				
		byte[] serverEncryptByets = Utl.hkdfExpand(prk, "server encrypt");
		this.serverEncrypt = new SecretKeySpec(serverEncryptByets, "AES");
		
		byte[] clientEncryptBytes = Utl.hkdfExpand(serverEncryptByets, "client encrypt");
		this.clientEncrypt = new SecretKeySpec(clientEncryptBytes, "AES");
		
		byte[] serverMACBytes = Utl.hkdfExpand(clientEncryptBytes, "server MAC");
		this.serverMAC = new SecretKeySpec(serverMACBytes, "MAC");
		
		byte[] clientMACBytes = Utl.hkdfExpand(serverMACBytes, "client MAC");
		this.clientMAC = new SecretKeySpec(clientMACBytes, "MAC");
		
		byte[] serverIVBytes = Utl.hkdfExpand(clientMACBytes, "server IV");
		this.serverIV = new IvParameterSpec(serverIVBytes);
		
		byte[] clientIVBytes = Utl.hkdfExpand(serverIVBytes, "server IV");
		this.clientIV = new IvParameterSpec(clientIVBytes);
	}

	public static void main(String[] args) throws Exception {
		
		ServerSocket ss = new ServerSocket(8080);
		while(true) {
			Socket connection = ss.accept();
			
			Server server = new Server();
			server.ousServer = new ObjectOutputStream(connection.getOutputStream());
			server.insServer = new ObjectInputStream(connection.getInputStream());
			
			// server receive nonce
			server.insServer.read(server.rnonce);
			
		    // server make and send certificate, public DH key, signed public DH key
			server.cf = CertificateFactory.getInstance("X.509");
			server.serverCertificate = (X509Certificate)server.cf.generateCertificate(new FileInputStream("CASignedServerCertificate.pem"));  
			server.serverDHPrivateKey = new BigInteger(new Random().nextInt() + ""); 
			server.serverDHPublicKey = Utl.generator.modPow(server.serverDHPrivateKey, Utl.n);     
			server.serverKey = Utl.getKey("serverPrivateKey.der");
			server.signedServerDHPubKey = Utl.sign(server.serverDHPublicKey.toByteArray(), server.serverKey);
			
			server.ousServer.writeObject(server.serverCertificate); 
			server.ousServer.writeObject(server.serverDHPublicKey);
			server.ousServer.writeObject(server.signedServerDHPubKey);
			server.ousServer.flush();
		     
		    // server receive and verify client's certificate, public DH key, signed public DH key
		    server.rclientCertificate = (X509Certificate)server.insServer.readObject();
		    server.rclientDHPublicKey = (BigInteger)server.insServer.readObject();
		    server.rsignedClientDHPubKey = (byte[])server.insServer.readObject();
		    System.out.println("server verify client's signed DH pub key: "+ Utl.verify(server.rclientDHPublicKey.toByteArray(), server.rsignedClientDHPubKey, server.rclientCertificate.getPublicKey()));
		    assert(Utl.verify(server.rclientDHPublicKey.toByteArray(), server.rsignedClientDHPubKey, server.rclientCertificate.getPublicKey()));
		    
			// server compute the shared secret and 6 session keys
			 server.serverShared = server.rclientDHPublicKey.modPow(server.serverDHPrivateKey, Utl.n);
			 server.makeSecretKeys(server.rnonce, server.serverShared.toByteArray());
			 
			 // Server: MAC(all handshake messages so far, Server's MAC key), send to client
			 ByteArrayOutputStream allMsg = new ByteArrayOutputStream();
			 allMsg.write(server.rnonce);
			 allMsg.write(server.serverCertificate.getEncoded());
			 allMsg.write(server.serverDHPublicKey.toByteArray());
			 allMsg.write(server.signedServerDHPubKey);
			 allMsg.write(server.rclientCertificate.getEncoded());
			 allMsg.write(server.rclientDHPublicKey.toByteArray());
			 allMsg.write(server.rsignedClientDHPubKey);
			 
			 byte[] serverMac = Utl.computeHMAC(server.serverMAC, allMsg.toByteArray());
			 server.ousServer.writeObject(serverMac);
			 server.ousServer.flush();
			 
			 // compute client's Mac, receive client's Mac, compare
			 allMsg.write(serverMac);
			 byte[] serverCalculatedClientMac = Utl.computeHMAC(server.clientMAC, allMsg.toByteArray());
			 byte[] rclientMac = (byte[]) server.insServer.readObject();
			 assert(serverCalculatedClientMac.equals(rclientMac));
//			 for (int i = 0; i < rclientMac.length; i++) {
//			 System.out.print(serverCalculatedClientMac[i]);
//			 System.out.print("  ");
//			 System.out.print(rclientMac[i]);
//			 System.out.println();
//		 }
			  
			// send big file 
			File serverFile = new File("serverFile.pbm"); 
			int numPacket = (int) Math.ceil(1.0*serverFile.length()/1024);
			server.ousServer.writeObject(numPacket);
			server.ousServer.flush();
	        
			FileInputStream fisServerFile = new FileInputStream(serverFile);
			
			byte[] buf = new byte[1024];
			for (int i = 0; i < serverFile.length()/1024; i++) {
				fisServerFile.read(buf);
				byte[] encrypedBuf = Utl.encryptMsg(server.serverEncrypt, server.serverMAC, server.serverIV, buf);
				server.ousServer.writeObject(encrypedBuf);
				server.ousServer.flush();
			}
			
			if (serverFile.length()%1024!=0) {
				byte[] buf2 = new byte[(int) (serverFile.length()%1024)];
				fisServerFile.read(buf2);
				byte[] encrypedBuf2 = Utl.encryptMsg(server.serverEncrypt, server.serverMAC, server.serverIV, buf2);
				server.ousServer.writeObject(encrypedBuf2);
				server.ousServer.flush();
			}
			
	        fisServerFile.close();
	        System.out.println("client said to server: "+(String)server.insServer.readObject());
			
		}
//		ss.close();
	}
	
}
