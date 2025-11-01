package org.harvanir.security.example.hashing;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2idDemo {

	// rekomendasi awal (tune agar ~100-250ms per hash di servermu)
	private static final int ITERATION = 3;

	private static final int MEMORY_KB = 64 * 1024; // 64MB

	private static final int PARALLELISM = 2; // thread lanes

	public static void main(String[] args) {
		Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
		char[] password = "rahasiaBanget123".toCharArray();

		try {
			String hash = argon2.hash(ITERATION, MEMORY_KB, PARALLELISM, password);
			System.out.println("Hash   : " + hash);
			System.out.println("OK?   : " + argon2.verify(hash, "rahasiaBanget123".toCharArray()));
			System.out.println("OK?   : " + argon2.verify(hash, "salahPassword".toCharArray()));
		} finally {
			// Hapus password dari memori (opsional tapi bagus)
			java.util.Arrays.fill(password, '\0');
		}

	}
}
