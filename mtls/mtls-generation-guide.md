# mTLS Certificate Generation Guide

Dokumentasi ini menjelaskan file-file yang digunakan dan dihasilkan oleh script `generate_mtls_certs.sh` untuk implementasi mutual TLS (mTLS).

## 📋 Overview

Script `generate_mtls_certs.sh` menghasilkan semua certificate dan keystore yang diperlukan untuk implementasi mTLS dalam aplikasi Java. Script ini membuat Certificate Authority (CA) sendiri dan menggunakannya untuk menandatangani certificate server dan client.

## 🔧 Proses Generate Certificate

### 1️⃣ **Membuat Certificate Authority (CA)**
```bash
openssl genpkey -algorithm RSA -out ca.key.pem -pkeyopt rsa_keygen_bits:4096
openssl req -x509 -new -nodes -key ca.key.pem -sha256 -days 3650 \
  -subj "/CN=Test-Root-CA" -out ca.crt.pem
```

**File yang dihasilkan:**
- `ca.key.pem` - Private key untuk CA (4096 bit RSA)
- `ca.crt.pem` - Self-signed certificate untuk CA (berlaku 10 tahun)

**Penggunaan:** CA digunakan untuk menandatangani certificate server dan client

---

### 2️⃣ **Membuat Server Certificate**
```bash
openssl genpkey -algorithm RSA -out server.key.pem -pkeyopt rsa_keygen_bits:2048
openssl req -new -key server.key.pem -subj "/CN=localhost" -out server.csr.pem
openssl x509 -req -in server.csr.pem -CA ca.crt.pem -CAkey ca.key.pem -CAcreateserial \
  -out server.crt.pem -days 825 -sha256
```

**File yang dihasilkan:**
- `server.key.pem` - Private key untuk server (2048 bit RSA)
- `server.csr.pem` - Certificate Signing Request untuk server
- `server.crt.pem` - Certificate server yang ditandatangani oleh CA
- `ca.crt.srl` - Serial number file dari CA

**Penggunaan:** Server menggunakan `server.key.pem` dan `server.crt.pem` untuk otentikasi

---

### 3️⃣ **Membuat Client Certificate**
```bash
openssl genpkey -algorithm RSA -out client.key.pem -pkeyopt rsa_keygen_bits:2048
openssl req -new -key client.key.pem -subj "/CN=client-001" -out client.csr.pem
openssl x509 -req -in client.csr.pem -CA ca.crt.pem -CAkey ca.key.pem -CAcreateserial \
  -out client.crt.pem -days 825 -sha256
```

**File yang dihasilkan:**
- `client.key.pem` - Private key untuk client (2048 bit RSA)
- `client.csr.pem` - Certificate Signing Request untuk client
- `client.crt.pem` - Certificate client yang ditandatangani oleh CA

**Penggunaan:** Client menggunakan `client.key.pem` dan `client.crt.pem` untuk otentikasi

---

### 4️⃣ **Membuat PKCS#12 Keystores**

#### Server Keystore
```bash
openssl pkcs12 -export \
  -in server.crt.pem -inkey server.key.pem -certfile ca.crt.pem \
  -name server \
  -passout pass:"$SERVER_PASS" \
  -out server.p12
```

#### Client Keystore
```bash
openssl pkcs12 -export \
  -in client.crt.pem -inkey client.key.pem -certfile ca.crt.pem \
  -name client \
  -passout pass:"$CLIENT_PASS" \
  -out client.p12
```

**File yang dihasilkan:**
- `server.p12` - PKCS#12 keystore berisi server private key, certificate, dan CA chain
- `client.p12` - PKCS#12 keystore berisi client private key, certificate, dan CA chain

**Penggunaan:** Java aplikasi menggunakan file .p12 untuk SSL/TLS configuration

---

### 5️⃣ **Membuat Truststores**

```bash
keytool -importcert -noprompt \
  -alias root-ca -file ca-only.crt.pem \
  -keystore server-trust.p12 -storetype PKCS12 -storepass "$STORE_PASS"

keytool -importcert -noprompt \
  -alias root-ca -file ca-only.crt.pem \
  -keystore client-trust.p12 -storetype PKCS12 -storepass "$STORE_PASS"
```

**File yang dihasilkan:**
- `server-trust.p12` - Truststore untuk server berisi CA certificate
- `client-trust.p12` - Truststore untuk client berisi CA certificate
- `ca-only.crt.pem` - Copy dari CA certificate untuk keytool

**Penggunaan:** Truststore digunakan untuk memverifikasi certificate dari peer (server/client)

---

## 📁 File Structure

```
mtls-certs/
├── ca.key.pem              # CA private key (4096 bit)
├── ca.crt.pem              # CA self-signed certificate
├── ca.crt.srl              # CA serial number file
├── ca-only.crt.pem         # CA certificate copy untuk keytool
├── server.key.pem          # Server private key (2048 bit)
├── server.csr.pem          # Server certificate request
├── server.crt.pem          # Server certificate (signed by CA)
├── server.p12              # Server PKCS#12 keystore
├── server-trust.p12        # Server truststore
├── client.key.pem          # Client private key (2048 bit)
├── client.csr.pem          # Client certificate request
├── client.crt.pem          # Client certificate (signed by CA)
├── client.p12              # Client PKCS#12 keystore
└── client-trust.p12        # Client truststore

Parent Directory (untuk convenience):
├── server.p12              # Copy dari server keystore
├── client.p12              # Copy dari client keystore
├── server-trust.p12        # Copy dari server truststore
├── client-trust.p12        # Copy dari client truststore
└── ca.crt.pem              # Copy dari CA certificate
```

## 🔐 Password Configuration

Script menggunakan password yang dapat dikonfigurasi:

```bash
CA_PASS="capass"            # Password untuk CA (tidak digunakan karena -nodes)
SERVER_PASS="serverpass"    # Password untuk server.p12 keystore
CLIENT_PASS="clientpass"    # Password untuk client.p12 keystore
STORE_PASS="storepass"      # Password untuk truststore files
```

## 🚀 Penggunaan dalam Java

### Server Configuration
```java
// Keystore untuk server certificate dan private key
System.setProperty("javax.net.ssl.keyStore", "server.p12");
System.setProperty("javax.net.ssl.keyStorePassword", "serverpass");
System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

// Truststore untuk memverifikasi client certificate
System.setProperty("javax.net.ssl.trustStore", "server-trust.p12");
System.setProperty("javax.net.ssl.trustStorePassword", "storepass");
System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
```

### Client Configuration
```java
// Keystore untuk client certificate dan private key
System.setProperty("javax.net.ssl.keyStore", "client.p12");
System.setProperty("javax.net.ssl.keyStorePassword", "clientpass");
System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

// Truststore untuk memverifikasi server certificate
System.setProperty("javax.net.ssl.trustStore", "client-trust.p12");
System.setProperty("javax.net.ssl.trustStorePassword", "storepass");
System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
```

## 🔄 Auto-Cleanup Features

Script memiliki fitur pembersihan otomatis:

1. **Hapus file lama di parent directory:**
   ```bash
   rm -f server.p12 client.p12 server-trust.p12 client-trust.p12 ca.crt.pem
   ```

2. **Hapus direktori mtls-certs:**
   ```bash
   rm -rf "$OUTDIR"
   ```

Ini memastikan setiap eksekusi dimulai dari kondisi bersih tanpa konflik file lama.

## ⚠️ Security Notes

- **Private Keys:** File `.key.pem` dan `.p12` berisi private key yang sensitif
- **Production Use:** Ganti semua password default sebelum digunakan di production
- **File Permissions:** Private key files memiliki permission 600 (read-write owner only)
- **Certificate Validity:** Server dan client certificate berlaku 825 hari, CA certificate 10 tahun

## 🛠️ Requirements

- **OpenSSL:** Untuk generate certificate dan convert format
- **Java Keytool:** Untuk membuat PKCS#12 truststore
- **Bash:** Script menggunakan bash dengan `set -euo pipefail`

## 📚 Related Files

- `../code/java/src/main/java/org/harvanir/security/example/mtls/MtlsSocketExample.java` - Contoh implementasi mTLS dalam Java
- `generate_mtls_certs.sh` - Script generator certificate ini

---

**Generated by:** `generate_mtls_certs.sh`  
**Last Updated:** October 30, 2025