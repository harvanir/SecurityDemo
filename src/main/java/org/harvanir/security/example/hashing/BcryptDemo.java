package org.harvanir.security.example.hashing;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptDemo {

	public static void main(String[] args) {
		String password = "rahasiaBanget123";
		String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));

		System.out.println("Hash : " + hash);
		System.out.println("Match: " + BCrypt.checkpw("rahasiaBanget123", hash));
		System.out.println("Match: " + BCrypt.checkpw("salahPassword", hash));
	}

}
