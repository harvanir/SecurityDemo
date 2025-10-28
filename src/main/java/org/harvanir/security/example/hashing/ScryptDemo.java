package org.harvanir.security.example.hashing;

import com.lambdaworks.crypto.SCryptUtil;

public class ScryptDemo {

	public static void main(String[] args) {
		String password = "rahasiaBanget123";

		// Parameter N, r, p
		// N = 2^14 = 16384 (work factor)
		// r = 8 (block size)
		// p = 1 (parallelism)
		String hash = SCryptUtil.scrypt(password, 16384, 8, 1);
		System.out.println("Hash  : " + hash);

		// verifikasi password
		boolean ok1 = SCryptUtil.check(password, hash);
		boolean ok2 = SCryptUtil.check("salahPassword", hash);

		System.out.println("OK1 ? : " + ok1);
		System.out.println("OK2 ? : " + ok2);
	}
}