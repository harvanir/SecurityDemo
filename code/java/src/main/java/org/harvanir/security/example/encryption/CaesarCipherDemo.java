package org.harvanir.security.example.encryption;

public class CaesarCipherDemo {
	
	public static final int SHIFT = 3;

	public static void main(String[] args) throws Exception {
		int key = SHIFT;

		String text = "Hello Harvan!";
		String encrypted = encrypt(text, key);
		String decrypted = decrypt(encrypted, key);
		
		System.out.println("Plain text: " + text);
		System.out.println("Encrypted : " + encrypted);
		System.out.println("Decrypted : " + decrypted);

	}

	public static String encrypt(String text, int key) {
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			result.append((char) (c + key));
		}
		return result.toString();
	}

	public static String decrypt(String text, int key) {
		StringBuffer result = new StringBuffer();
		for (char c : text.toCharArray()) {
			result.append((char) (c - SHIFT));
		}
		return result.toString();
	}
}
