package org.harvanir.security.example.hashing;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Demo {

	private static final int SALT_LEN = 16;

	private static final int ITER = 150_000;

	private static final int KEY_BITS = 256;

	private static final SecureRandom RNG = new SecureRandom();

	public static void main(String[] args) throws Exception {
		String h = hash("password-ku".toCharArray());
		System.out.println("Stored: " + h);
		System.out.println("OK? " + verify("password-ku".toCharArray(), h));
		System.out.println("OK? " + verify("salah".toCharArray(), h));
	}

	// Hasil format : pbkdf2$sha256$ITER$base64(salt)$base64(hash)
	private static String hash(char[] password) throws Exception {
		byte[] salt = new byte[SALT_LEN];
		RNG.nextBytes(salt);

		PBEKeySpec spec = new PBEKeySpec(password, salt, ITER, KEY_BITS);
		byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
				.generateSecret(spec).getEncoded();

		return String.join("$", "pbkdf2", "sha256", String.valueOf(ITER),
				Base64.getEncoder().encodeToString(salt),
				Base64.getEncoder().encodeToString(hash));
	}

	private static boolean verify(char[] password, String stored) throws Exception {
		String[] parts = stored.split("\\$");
		if (parts.length != 5)
			return false;
		int iter = Integer.parseInt(parts[2]);
		byte[] salt = Base64.getDecoder().decode(parts[3]);
		byte[] expected = Base64.getDecoder().decode(parts[4]);

		PBEKeySpec spec = new PBEKeySpec(password, salt, iter, expected.length * 8);
		byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
				.generateSecret(spec).getEncoded();

		return constantTimeEquals(expected, actual);
	}

	private static boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a.length != b.length)
			return false;
		int r = 0;
		for (int i = 0; i < a.length; i++)
			r |= a[i] ^ b[i];
		return r == 0;
	}

}
