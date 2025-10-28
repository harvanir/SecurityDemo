package org.harvanir.security.example.encryption;

public class XorCipherDemo {

	public static final char KEY = 'K';
	

	public static void main(String[] args) throws Exception {
		char key = KEY;

		String text = "Hello Harvan!";
		String encrypted = encrypt(text, key);
		String decrypted = decrypt(encrypted, key);
		
		System.out.println("Plain text: " + text);
		System.out.println("Encrypted : " + encrypted);
		System.out.println("Decrypted : " + decrypted);
	}

	public static String encrypt(String text, char key) {
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			result.append((char) (c ^ key));
		}
		return result.toString();
	}

	public static String decrypt(String text, char key) {
		return encrypt(text, key);
	}
}
