package tls;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utl {

	// 1. handshake part
	static BigInteger generator = new BigInteger("2");
	static BigInteger n;
	static {
		String hex = " FFFFFFFF FFFFFFFF C90FDAA2 2168C234 C4C6628B 80DC1CD1 "
	     		+ "29024E08 8A67CC74 020BBEA6 3B139B22 514A0879 8E3404DD"
	     		+ "EF9519B3 CD3A431B 302B0A6D F25F1437 4FE1356D 6D51C245"
	     		+ "E485B576 625E7EC6 F44C42E9 A637ED6B 0BFF5CB6 F406B7ED"
	     		+ "EE386BFB 5A899FA5 AE9F2411 7C4B1FE6 49286651 ECE45B3D"
	     		+ "C2007CB8 A163BF05 98DA4836 1C55D39A 69163FA8 FD24CF5F"
	     		+ "83655D23 DCA3AD96 1C62F356 208552BB 9ED52907 7096966D"
	     		+ "670C354E 4ABC9804 F1746C08 CA18217C 32905E46 2E36CE3B"
	     		+ "E39E772C 180E8603 9B2783A2 EC07A28F B5C55DF0 6F4C52C9"
	     		+ "DE2BCBF6 95581718 3995497C EA956AE5 15D22618 98FA0510"
	     		+ "15728E5A 8AACAA68 FFFFFFFF FFFFFFFF";
	     hex = hex.replaceAll("\\s+", "");
	     
	     n = new BigInteger(hex, 16);
	}

	// signed public key
	public static byte[] sign(byte input[], PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("SHA256withRSA");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(input);

	    return privateSignature.sign();
	}

	// verify signed DH pub key
	public static boolean verify(byte input[], byte[] signature, PublicKey publicKey) throws Exception {
	    Signature publicSignature = Signature.getInstance("SHA256withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(input);
	    return publicSignature.verify(signature);
	}
	
	// Read key from file
	static PrivateKey getKey(String filename) throws IOException, GeneralSecurityException {
		
		Path filePath = Paths.get(filename);
		byte[] fileContents = Files.readAllBytes(filePath);
		// no need to do endcode & decode stuff since we are reading from DER file (if PEM, more trouble)
		KeyFactory kf = KeyFactory.getInstance("RSA");
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(fileContents);
	    return kf.generatePrivate(keySpec);
	}
	
	// Key generation: HKDF
	public static byte[] hkdfExpand(byte[] input, String tag) throws NoSuchAlgorithmException, InvalidKeyException { 
		
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(input, "HmacSHA256");
		sha256_HMAC.init(secret_key);
		
//		data = tag concatenated with a byte with value 1
		byte[] data = new byte[tag.getBytes().length+1];
		for(int i = 0; i < data.length-1; i++) {
			data[i] = tag.getBytes()[i];
		}
		data[data.length-1] = 1;
		
		byte[] okm = sha256_HMAC.doFinal(data); 
		// encodeBase64String is just to convert string and byte, 
		// it does not have anything to do with encryption
		byte[] res = new byte[16];
		for (int i = 0; i < res.length; i++) {
			res[i] = okm[i];
		}
		return res;
	}

	
	// 2. Msg part
	public static byte[] computeHMAC(SecretKeySpec key, byte[] msg) throws InvalidKeyException, NoSuchAlgorithmException {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getEncoded(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return sha256_HMAC.doFinal(msg);
	}

	public static byte[] encryptMsg(SecretKeySpec encryptKey, SecretKeySpec macKey, IvParameterSpec iv,  byte[] msg) throws Exception {
		byte[] msgMAC = computeHMAC(macKey, msg);
		
		byte[] data = new byte[msg.length + msgMAC.length];
		System.arraycopy(msg, 0, data, 0, msg.length);
		System.arraycopy(msgMAC, 0, data, msg.length, msgMAC.length);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, encryptKey, iv);
		return cipher.doFinal(data);
	}
	
	public static byte[] decryptMsg(SecretKeySpec encryptKey, SecretKeySpec macKey, IvParameterSpec iv,  byte[] msg) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, encryptKey, iv);
		byte[] decrypted = cipher.doFinal(msg); // this decrypted is = plain + mac; mac is 32 byte
		byte[] plain = new byte[decrypted.length-32];
		System.arraycopy(decrypted,0,plain,0,plain.length);
		byte[] mac = new byte[32];
		System.arraycopy(decrypted,plain.length,mac,0,mac.length);
		
		// calculate mac of plain and check if it is equal to the above mac
		byte[] calculatedMac = computeHMAC(macKey, plain);
		assert(calculatedMac.equals(mac));
//		for (int i = 0; i < calculatedMac.length; i++) {
//			System.out.print(calculatedMac[i] + " " + mac[i]);
//			System.out.println();
//		}
		
		// remove mac from decrypted, and return only plain
		return plain;
	}
	
}
