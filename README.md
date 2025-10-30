# SecurityDemo

Repository ini berisi kumpulan demo dan implementasi berbagai konsep keamanan (security) dalam pemrograman Java, mulai dari algoritma enkripsi, hashing, hingga implementasi mutual TLS (mTLS).

## 🎯 Tujuan Repository

Repository ini dibuat untuk:
- **Pembelajaran**: Memahami konsep-konsep keamanan secara praktis
- **Referensi**: Implementasi best practices untuk berbagai kebutuhan security
- **Perbandingan**: Analisis kelebihan dan kekurangan berbagai algoritma security
- **Dokumentasi**: Penjelasan lengkap tentang kapan dan bagaimana menggunakan setiap metode

## 📚 Topik yang Dibahas

### 🔐 **Encryption (Enkripsi)**
Implementasi berbagai algoritma enkripsi dari yang sederhana hingga modern:
- **Caesar Cipher** - Cipher klasik untuk pembelajaran
- **XOR Cipher** - Cipher sederhana untuk obfuscation
- **AES-GCM** - Enkripsi simetris modern yang direkomendasikan
- **RSA-2048** - Enkripsi asimetris untuk key exchange dan digital signature

📂 **Lokasi**: `src/main/java/org/harvanir/security/example/encryption/`
📖 **Dokumentasi Detail**: [encryption_hashing.md](encryption_hashing.md)

### � **Hashing**
Implementasi algoritma hashing untuk keamanan password:
- **PBKDF2** - Standard untuk password hashing dengan salt
- **bcrypt** - Algoritma hashing yang umum digunakan
- **scrypt** - Memory-hard hashing untuk anti-GPU attacks
- **Argon2id** - Modern password hashing (OWASP recommendation)

📂 **Lokasi**: `src/main/java/org/harvanir/security/example/hashing/`
📖 **Dokumentasi Detail**: [encryption_hashing.md](encryption_hashing.md)

### 🤝 **Mutual TLS (mTLS)**
Implementasi autentikasi dua arah menggunakan certificate:
- **TLS Fundamentals** - Penjelasan dasar TLS handshake dan keystore/truststore
- **Certificate Generation** - Script untuk generate CA, server, dan client certificates
- **mTLS Handshake** - Simulasi dan implementasi mutual TLS
- **Keystore Management** - Pengelolaan certificate dan truststore

📂 **Lokasi**: `mtls/` (files) dan `src/main/java/org/harvanir/security/example/mtls/` (Java code)
📖 **Dokumentasi Detail**: [mTLS Guide](mtls/mtls-generation-guide.md)

## 🚀 Cara Menjalankan

### Prerequisites
- **Java 11+** (JDK)
- **Maven 3.6+**
- **OpenSSL** (untuk generate mTLS certificates)

### Build & Run
```bash
# Clone repository
git clone https://github.com/harvanir/SecurityDemo.git
cd SecurityDemo

# Build project
mvn clean compile

# Run specific demo (contoh: AES demo)
mvn exec:java -Dexec.mainClass="org.harvanir.security.example.encryption.SimpleAesDemo"

# Run test
mvn test
```

### Generate mTLS Certificates
```bash
cd mtls/
./generate_mtls_certs.sh
```

## 📖 Dokumentasi Detail

| Topik | Dokumentasi | Deskripsi |
|-------|-------------|-----------|
| **Encryption & Hashing** | [encryption_hashing.md](encryption_hashing.md) | Perbandingan detail algoritma enkripsi dan hashing |
| **TLS Fundamentals** | [TLS_Handshake_Keystore_Truststore.md](TLS_Handshake_Keystore_Truststore.md) | Penjelasan dasar TLS handshake dan konsep keystore/truststore |
| **mTLS Implementation** | [mtls/mtls-generation-guide.md](mtls/mtls-generation-guide.md) | Guide lengkap implementasi mutual TLS |

## 🎓 Roadmap Pembelajaran

### 1️⃣ **Pemula**
- Mulai dengan Caesar Cipher dan XOR Cipher untuk memahami konsep dasar
- Pelajari AES-GCM untuk enkripsi modern
- Pahami bcrypt untuk password hashing

### 2️⃣ **Menengah**
- Eksplorasi RSA untuk enkripsi asimetris
- Bandingkan PBKDF2, scrypt, dan Argon2id
- Implementasi hybrid encryption (RSA + AES)

### 3️⃣ **Lanjutan**
- Pahami TLS handshake dan konsep keystore/truststore
- Implementasi mTLS untuk autentikasi mutual
- Certificate management dan PKI
- Security best practices untuk production

## ⚠️ Security Notice

> **Penting**: Demo dalam repository ini ditujukan untuk pembelajaran dan pengembangan. Untuk penggunaan production:
> - Gunakan parameter security yang sesuai (key length, iteration count, dll.)
> - Implementasikan proper key management
> - Lakukan security audit dan penetration testing
> - Ikuti security compliance yang berlaku

## 📋 Rekomendasi Quick Start

| Kebutuhan | Solusi Rekomendasi | Demo File |
|-----------|-------------------|-----------|
| **Enkripsi data aplikasi** | AES-GCM | `SimpleAesDemo.java` |
| **Password storage** | Argon2id atau bcrypt | `Argon2idDemo.java` atau `BcryptDemo.java` |
| **Key exchange** | RSA-2048 | `Rsa2048MiniDemo.java` |
| **Mutual authentication** | mTLS | `MtlsSocketExample.java` |

## 🤝 Kontribusi

Kontribusi selalu diterima! Silakan:
1. Fork repository ini
2. Buat branch untuk feature/fix Anda
3. Commit changes dengan pesan yang jelas
4. Submit Pull Request

## 📄 Lisensi

Silakan gunakan sesuai kebutuhan pembelajaran dan pengembangan Anda.
