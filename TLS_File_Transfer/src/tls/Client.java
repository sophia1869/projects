package tls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Client {

	byte[] nonce1 = new byte[32];
	ObjectOutputStream ousClient;
	ObjectInputStream insClient;
	
	CertificateFactory cf;
	X509Certificate clientCertificate;
	BigInteger clientDHPrivateKey, clientDHPublicKey;
	PrivateKey clientKey;
	byte[] signedClientDHPubKey;
	
	X509Certificate rserverCertificate;
	BigInteger rserverDHPublicKey;
	byte[] rsignedServerDHPubKey;
	
	BigInteger clientShared;
	
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
		
		InetAddress host = InetAddress.getLocalHost();
		Socket socket = new Socket(host.getHostName(), 8080);
		
		Client client = new Client();
		client.ousClient = new ObjectOutputStream(socket.getOutputStream());
		client.insClient = new ObjectInputStream(socket.getInputStream());
		
		//client send nonce 
		SecureRandom random = new SecureRandom();
		random.nextBytes(client.nonce1);
		
		client.ousClient.write(client.nonce1); 
		client.ousClient.flush();
		
		// client receive and verify server's cer, DH pub, signed DH pub
		 client.rserverCertificate = (X509Certificate)client.insClient.readObject();
		 client.rserverDHPublicKey = (BigInteger)client.insClient.readObject();
		 client.rsignedServerDHPubKey = (byte[])client.insClient.readObject();
		 System.out.println("client verify server's signed DH pub key: "+ Utl.verify(client.rserverDHPublicKey.toByteArray(), client.rsignedServerDHPubKey, client.rserverCertificate.getPublicKey()));
		 assert(Utl.verify(client.rserverDHPublicKey.toByteArray(), client.rsignedServerDHPubKey, client.rserverCertificate.getPublicKey()));
		 
		// client make and send certificate, public DH key, signed public DH key
		 client.cf = CertificateFactory.getInstance("X.509");
		 client.clientCertificate = (X509Certificate) client.cf.generateCertificate(new FileInputStream("CASignedClientCertificate.pem"));	
		 client.clientDHPrivateKey = new BigInteger(new Random().nextInt() + ""); 
		 client.clientDHPublicKey = Utl.generator.modPow(client.clientDHPrivateKey, Utl.n); 		   
		 client.clientKey = Utl.getKey("clientPrivateKey.der");
		 client.signedClientDHPubKey = Utl.sign(client.clientDHPublicKey.toByteArray(), client.clientKey);
		 
		 client.ousClient.writeObject(client.clientCertificate); 
		 client.ousClient.writeObject(client.clientDHPublicKey);
		 client.ousClient.writeObject(client.signedClientDHPubKey);
		 client.ousClient.flush();
		 
		 // client compute shared secret and 6 session keys
		 client.clientShared = client.rserverDHPublicKey.modPow(client.clientDHPrivateKey, Utl.n);
		 client.makeSecretKeys(client.nonce1, client.clientShared.toByteArray());
		 
		 // compute server's big Mac, receive server's big Mac, compare
		 ByteArrayOutputStream allMsg = new ByteArrayOutputStream();
		 allMsg.write(client.nonce1);
		 allMsg.write(client.rserverCertificate.getEncoded());
		 allMsg.write(client.rserverDHPublicKey.toByteArray());
		 allMsg.write(client.rsignedServerDHPubKey);
		 allMsg.write(client.clientCertificate.getEncoded());
		 allMsg.write(client.clientDHPublicKey.toByteArray());
		 allMsg.write(client.signedClientDHPubKey);
		 
		 byte[] clientCalculatedServerMac = Utl.computeHMAC(client.serverMAC, allMsg.toByteArray());
		 byte[] rserverMac = (byte[]) client.insClient.readObject();
		 assert(clientCalculatedServerMac.equals(rserverMac));
//		 for (int i = 0; i < rserverMac.length; i++) {
//			 System.out.print(clientCalculatedServerMac[i]);
//			 System.out.print("  ");
//			 System.out.print(rserverMac[i]);
//			 System.out.println();
//		 }
		 
		 // MAC(all handshake messages including the previous step, Client's MAC key), send to server
		 allMsg.write(rserverMac);
		 byte[] clientMac = Utl.computeHMAC(client.clientMAC, allMsg.toByteArray());
		 client.ousClient.writeObject(clientMac);
		 client.ousClient.flush();
		 
		 // receive big file from server
		 int numPacket = (int)client.insClient.readObject();
		 
		 ByteArrayOutputStream clientReceived = new ByteArrayOutputStream();
		 
		 for (int i = 0; i < numPacket; i++) {
			 clientReceived.write(Utl.decryptMsg(client.serverEncrypt, client.serverMAC, client.serverIV, (byte[])client.insClient.readObject()));
		 }
		 
		 FileOutputStream fosclientReceived = new FileOutputStream("clientReceivedFile.pbm");
		 fosclientReceived.write(clientReceived.toByteArray());
		 fosclientReceived.close();
		 
		 client.ousClient.writeObject("big file received");
		 client.ousClient.flush();
	}

}
