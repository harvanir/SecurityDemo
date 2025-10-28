package org.harvanir.security.example.encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TinyAesGcmPSKDemo {

	private static final int IV_LEN = 12; // 96-bit IV
	
	private static final int TAG_BIT = 128; // 128-bit auth tag
	
	private static final SecureRandom RNG = new SecureRandom();
	
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	
	private static final String ALGORITHM = "AES";
	
	public static void main(String[] args) throws Exception {
		String key = generateKey();

		String text = "Hello Harvan!";
		String encrypted = encrypt(text, key);
		String decrypted = decrypt(encrypted, key);
		
		System.out.println("Plain text: " + text);
		System.out.println("Encrypted : " + encrypted);
		System.out.println("Decrypted : " + decrypted);

	}

	private static String generateKey() throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(256);
		SecretKey sk = kg.generateKey();
		return Base64.getEncoder().encodeToString(sk.getEncoded());
	}

	private static String encrypt(String text, String key) throws Exception {
		byte[] k = Base64.getDecoder().decode(key);
		byte[] iv = new byte[IV_LEN];
		RNG.nextBytes(iv);

		Cipher c = Cipher.getInstance(TRANSFORMATION);
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(k, "AES"), new GCMParameterSpec(TAG_BIT, iv));
		byte[] ct = c.doFinal(text.getBytes(StandardCharsets.UTF_8));

		byte[] out = new byte[iv.length + ct.length];
		System.arraycopy(iv, 0, out, 0, iv.length);
		System.arraycopy(ct, 0, out, iv.length, ct.length);
		return Base64.getEncoder().encodeToString(out);
	}

	private static String decrypt(String text, String key) throws Exception {
		byte[] all = Base64.getDecoder().decode(text);
		if (all.length < IV_LEN + 16) throw new IllegalArgumentException("Input too short");

		byte[] iv = new byte[IV_LEN];
		byte[] ct = new byte[all.length - IV_LEN];
		System.arraycopy(all, 0, iv, 0, IV_LEN);
		System.arraycopy(all, IV_LEN, ct, 0, ct.length);

		byte[] k = Base64.getDecoder().decode(key);
		Cipher c = Cipher.getInstance(TRANSFORMATION);
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(k, ALGORITHM), new GCMParameterSpec(TAG_BIT, iv));
		byte[] pt = c.doFinal(ct);
		return new String(pt, StandardCharsets.UTF_8);
	}
}