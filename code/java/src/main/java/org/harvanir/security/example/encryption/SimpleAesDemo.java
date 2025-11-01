package org.harvanir.security.example.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class SimpleAesDemo {

	public static void main(String[] args) throws Exception {
		SecretKey key = generateKey();

		String text = "Hello Harvan!";
		String encrypted = encrypt(text, key);
		String decrypted = decrypt(encrypted, key);
		
		System.out.println("Plain text: " + text);
		System.out.println("Encrypted : " + encrypted);
		System.out.println("Decrypted : " + decrypted);

	}
	
	private static String decrypt(String encryptedText, SecretKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
		return new String(decryptedBytes);
	}

	private static String encrypt(String text, SecretKey key) throws Exception	{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(text.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static SecretKey generateKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		return keyGen.generateKey();
	}


}