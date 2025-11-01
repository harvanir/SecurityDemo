package org.harvanir.security.example.encryption;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import org.harvanir.security.example.encryption.Rsa2048MiniDemo.EncryptedCommunication.EncryptedMessage;

public class Rsa2048MiniDemo {

	public static void main(String[] args) throws Exception {
		runObjectOrientedCode();
	}

	private static void runObjectOrientedCode() throws Exception {
		EncryptedCommunication communicator1 = new EncryptedCommunication("communicator1");
		EncryptedCommunication communicator2 = new EncryptedCommunication("communicator2");
		communicator1.pairing(communicator2);

		String message1 = "Halo ini pesan pertama dari communicator 1 ke 2";
		EncryptedMessage encryptedMessage1 = communicator1.createMessage(message1);
		System.out.println("Pesan #1 : " + message1);
		System.out.println("Encrypted pesan #1 : " + encryptedMessage1.getEncryptedMessage());

		System.out.println();
		boolean isValidEncryptedFormat = communicator2.isValidEncryptedFormat(encryptedMessage1.getEncryptedMessage());
		System.out.println("Format encrypted pesan #1 valid: " + isValidEncryptedFormat);
		boolean isMessage1Received = communicator2.receiveMessage(encryptedMessage1);
		System.out.println("Pesan #1 diterima dengan sukses: " + isMessage1Received);
		System.out.println();

		String message2 = "Halo ini balasan dari communicator 2 ke 1";
		EncryptedMessage encryptedMessage2 = communicator2.createMessage(message2);
		System.out.println("Pesan #2 : " + message2);
		System.out.println("Encrypted pesan #2 : " + encryptedMessage2.getEncryptedMessage());

		System.out.println();
		boolean isMessage2Received = communicator1.receiveMessage(encryptedMessage2);
		System.out.println("Pesan #2 diterima dengan sukses: " + isMessage2Received);

	}

	public static class EncryptedCommunication {

		private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

		private static final String SIGNER_ALGORITHM = "SHA256withRSA";

		private final String objectName;

		private PublicKey otherPublicKey;

		private PrivateKey myPrivateKey;

		private PublicKey myPublicKey;

		private boolean hasPairing;

		public EncryptedCommunication(String objectName) {
			this.objectName = objectName;
			try {
				KeyPair kp = generate();
				this.myPrivateKey = kp.getPrivate();
				this.myPublicKey = kp.getPublic();
			} catch (Exception e) {
				System.err.println("Key pair generation failed: " + e.getMessage());
				throw new RuntimeException("Error on generating key pair");
			}
		}

		public PublicKey getPublicKey() {
			return myPublicKey;
		}

		public void pairing(EncryptedCommunication other) {
			if (hasPairing) return;
			
			hasPairing = true;
			if (this.equals(other)) {
				throw new RuntimeException("Cannot establish the same object.");
			}
			this.otherPublicKey = other.getPublicKey();
			other.pairing(this);
		}

		private KeyPair generate() throws Exception {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			return kpg.generateKeyPair();
		}

		public EncryptedMessage createMessage(String message) throws Exception {
			String encrypted = encrypt(message);
			String signature = signEncrypted(encrypted);
			return new EncryptedMessage(encrypted, signature);
		}

		// Enforcement of OAEP with SHA-256 (compatibility betwe)
		private OAEPParameterSpec getOapp256Spec() {
			return new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
		}

		private String encrypt(String message) throws Exception {
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			OAEPParameterSpec oaep256 = getOapp256Spec();
			c.init(Cipher.ENCRYPT_MODE, otherPublicKey, oaep256);
			byte[] ct = c.doFinal(message.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(ct);
		}

		private String signEncrypted(String encryptedText) throws Exception {
			Signature signer = Signature.getInstance(SIGNER_ALGORITHM);
			signer.initSign(myPrivateKey);
			signer.update(encryptedText.getBytes(StandardCharsets.UTF_8));
			byte[] signature = signer.sign();
			return Base64.getEncoder().encodeToString(signature);
		}

		// Method untuk memverifikasi signature encrypted message sebelum decrypt
		private boolean isValidSignature(String encryptedText, String signature) throws Exception {
			try {
				Signature verifier = Signature.getInstance(SIGNER_ALGORITHM);
				verifier.initVerify(otherPublicKey);
				verifier.update(encryptedText.getBytes(StandardCharsets.UTF_8));
				boolean isValid = verifier.verify(Base64.getDecoder().decode(signature));

				if (!isValid) {
					System.err.printf("%s says: Encrypted message signature verification failed!%n", this);
					return false;
				}

				System.out.printf("%s says: Encrypted message signature verified successfully%n", this);
				return true;
			} catch (Exception e) {
				System.err.printf("%s says: Signature verification failed.%n", this);
				return false;
			}
		}

		// Method alternatif untuk validasi encrypted text (check format)
		private boolean isValidEncryptedFormat(String encryptedText) {
			try {
				// Check apakah string adalah valid Base64 dan panjangnya reasonable untuk RSA
				// 2048
				byte[] decoded = Base64.getDecoder().decode(encryptedText);
				// RSA 2048 dengan OAEP menghasilkan ciphertext 256 bytes
				return decoded.length == 256;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		private String decrypt(String text) throws Exception {
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			OAEPParameterSpec spec = getOapp256Spec();
			c.init(Cipher.DECRYPT_MODE, myPrivateKey, spec);
			byte[] pt = c.doFinal(Base64.getDecoder().decode(text));
			return new String(pt, StandardCharsets.UTF_8);
		}

		public boolean receiveMessage(EncryptedMessage message) throws Exception {
			System.out.printf("%s says: Received encrypted message: %s%n", this, message.getMessage());

			if (!isValidSignature(message.encryptedMessage, message.getSignature())) {
				System.err
						.printf("%s says: Message signature invalid! Possible tampering detected. Aborting decryption.%n",
								this);
				return false;
			}
			System.out.printf("%s says: Message signature valid. Proceeding with decryption...%n", this);

			String rawMessage = decrypt(message.getMessage());
			System.out.printf("%s says: Decrypted message: %s%n", this, rawMessage);

			return true;
		}

		@Override
		public String toString() {
			return objectName;
		}

		final class EncryptedMessage {

			final String encryptedMessage;

			final String signature;

			EncryptedMessage(String encryptedMessage, String signature) {
				this.encryptedMessage = encryptedMessage;
				this.signature = signature;
			}

			String getMessage() {
				return encryptedMessage;
			}

			String getSignature() {
				return signature;
			}

			String getEncryptedMessage() {
				return encryptedMessage;
			}

		}
	}

}
