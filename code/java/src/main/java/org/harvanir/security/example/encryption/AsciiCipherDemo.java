package org.harvanir.security.example.encryption;

public class AsciiCipherDemo {

	public static final int SHIFT = 3;

	public static void main(String[] args) throws Exception {
		String text = "Hello Harvan, ini rahasia.";
		String encrypted = encrypt(text);
		String decrypted = decrypt(encrypted);

		System.out.println("Plain text: " + text);
		System.out.println("Encrypted : " + encrypted);
		System.out.println("Decrypted : " + decrypted);

	}

	private static String encryptInternal(String text, int shift) {
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			result.append((char) (c + shift));
		}
		return result.toString();
	}

	public static String encrypt(String text) {
		return encryptInternal(text, SHIFT);
	}

	public static String decrypt(String text) {
		return encryptInternal(text, -SHIFT);
	}
}
