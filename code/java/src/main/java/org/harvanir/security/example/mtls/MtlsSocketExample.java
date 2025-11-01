package org.harvanir.security.example.mtls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * MtlsSocketExample
 *
 * Usage:
 * - Compile: javac MtlsSocketExample.java
 * - Run server:
 * java MtlsSocketExample server <keystore.p12> <keystore-password>
 * <truststore.p12> <truststore-password> <port>
 * - Run client:
 * java MtlsSocketExample client <keystore.p12> <keystore-password>
 * <truststore.p12> <truststore-password> <host:port>
 *
 * The keystore should contain the node's private key + cert chain (PKCS12).
 * The truststore should contain the CA cert(s) that will be used to verify the
 * peer.
 */
public class MtlsSocketExample {

	static final String KEYSTORE_TYPE = "PKCS12";

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int serverPort = 8443;

		// server server.p12 serverpass server-trust.p12 storepass 8443
		Thread server = new Thread(() -> {
			try {
				System.out.println("current dir: " + System.getProperty("user.dir"));
				KeystoreInfo ks = new KeystoreInfo(getCurrentFilePath("server.p12"), "serverpass");
				TruststoreInfo ts = new TruststoreInfo(getCurrentFilePath("server-trust.p12"), "storepass");
				runServer(ks, ts, serverPort);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		server.start();

		// client client.p12 clientpass client-trust.p12 storepass localhost 8443
		Thread client = new Thread(() -> {
			try {
				KeystoreInfo ks = new KeystoreInfo(getCurrentFilePath("client.p12"), "clientpass");
				TruststoreInfo ts = new TruststoreInfo(getCurrentFilePath("client-trust.p12"), "storepass");
				runClient(ks, ts, host, serverPort);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		client.start();
	}

	private static String getCurrentFilePath(String fileName) {
		try {
			return new File(System.getProperty("user.dir")).getParentFile().getParentFile()
					.getPath() + File.separator + "mtls" + File.separator + fileName;
		} catch (

		Exception e) {
			throw new RuntimeException("error load file: " + e.getMessage());
		}
	}

	static SSLContext createSslContext(KeystoreInfo ksInfo, TruststoreInfo tsInfo) throws Exception {
		// Load keystore (contains private key + cert)
		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		try (FileInputStream fis = new FileInputStream(ksInfo.getPath())) {
			ks.load(fis, ksInfo.getPass().toCharArray());
		}

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, ksInfo.getPass().toCharArray());

		// Load truststore (contains CA certs)
		KeyStore ts = KeyStore.getInstance(KEYSTORE_TYPE);
		try (FileInputStream fis = new FileInputStream(tsInfo.getPath())) {
			ts.load(fis, tsInfo.getPass().toCharArray());
		}

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ts);

		SSLContext ctx = SSLContext.getInstance("TLSv1.3");
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		return ctx;
	}

	static void runServer(KeystoreInfo ksInfo, TruststoreInfo tsInfo, int port) throws Exception {
		SSLContext ctx = createSslContext(ksInfo, tsInfo);
		SSLServerSocketFactory ssf = ctx.getServerSocketFactory();

		try (SSLServerSocket serverSockeet = (SSLServerSocket) ssf.createServerSocket(port, 50,
				InetAddress.getByName("0.0.0.0"))) {
			serverSockeet.setNeedClientAuth(true);
			System.out.println("[Server] Listening on port " + port + ", waiting for mTLS clients...");

			var pool = Executors.newFixedThreadPool(1);
			SSLSocket s = (SSLSocket) serverSockeet.accept();
			pool.submit(() -> {
				try (SSLSocket socket = s) {
					socket.setSoTimeout(30_000);
					System.out.println("[Server] Connection from " + socket.getInetAddress());
					BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

					String line = r.readLine();
					System.out.println("[Server] Received: " + line);
					w.write("Hello from mTLS server. You said: " + line + "\n");
					w.flush();

					// print per cert subject if available
					try {
						SSLSession session = socket.getSession();
						Certificate[] peerCerts = session.getPeerCertificates();

						StringBuilder sb = new StringBuilder();
						Arrays.stream(peerCerts)
								.forEach(c -> {
									sb.append("[Server] Client cert class: " + c.getType());
									sb.append("\n");
									sb.append("Client cert: " + Stream.of(c.toString().split("\\n")).limit(9)
											.collect(Collectors.joining(System.lineSeparator())).toString());
									sb.append("\n");
								});
						System.out.println(sb.toString());
					} catch (Exception e) {
						System.err.println("[Server] Error on get peer certs: " + e.getMessage());
					}
				} catch (Exception e) {
					System.err.println("[Server] Connection handler error: " + e.getMessage());
				}
			});
		}
	}

	static void runClient(KeystoreInfo ksInfo, TruststoreInfo tsInfo, String host, int port) throws Exception {
		SSLContext ctx = createSslContext(ksInfo, tsInfo);
		SSLSocketFactory sf = ctx.getSocketFactory();

		try (SSLSocket socket = (SSLSocket) sf.createSocket(host, port)) {
			socket.setSoTimeout(30_000);
			socket.startHandshake();

			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String msg = "Hello from mTLS client";
			w.write(msg + "\n");
			w.flush();

			String reply = r.readLine();
			System.out.println("[Client] Server replied: " + reply);

			try {
				SSLSession session = socket.getSession();
				java.security.cert.Certificate[] certs = session.getPeerCertificates();
				System.out.println("[Client] server cert lenght: " + (certs != null ? certs.length : 0));
				StringBuilder sb = new StringBuilder();

				Arrays.stream(certs)
						.forEach(c -> {
							sb.append("[Client]");
							sb.append("\n");
							sb.append("Server cert class: " + c.getType());
							sb.append("\n");
							sb.append("Server cert: " + Stream.of(c.toString().split("\\n")).limit(9)
									.collect(Collectors.joining(System.lineSeparator())).toString());
							sb.append("\n");
						});
				System.out.println(sb.toString());
			} catch (Exception e) {
				System.err.println("[Client] Connection handler error: " + e.getMessage());
			}
		}
	}

	static class StoreInfo {
		private String path;

		private String pass;

		StoreInfo(String path, String pass) {
			this.path = path;
			this.pass = pass;
		}

		public String getPath() {
			return path;
		}

		public String getPass() {
			return pass;
		}

	}

	static class KeystoreInfo extends StoreInfo {
		KeystoreInfo(String path, String pass) {
			super(path, pass);
		}
	}

	static class TruststoreInfo extends StoreInfo {
		TruststoreInfo(String path, String pass) {
			super(path, pass);
		}
	}
}
