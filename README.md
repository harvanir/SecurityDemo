# SecurityDemo

Repository ini berisi kumpulan demo dan implementasi berbagai konsep keamanan (security) dalam **multi-bahasa pemrograman**, mulai dari algoritma enkripsi dasar hingga implementasi mutual TLS (mTLS) yang kompleks. 

**🔥 Multi-Language Support:**
- **Java** - Implementasi lengkap dengan semua algoritma modern (AES, RSA, Argon2, mTLS)
- **Go** - Implementasi cipher dasar untuk pembelajaran (Caesar, ASCII) + roadmap pengembangan
- **Kotlin** - Dalam perencanaan untuk ekspansi masa depan

Dirancang khusus untuk **pembelajaran interaktif** dan **referensi praktis** bagi developer yang ingin memahami cybersecurity secara mendalam melalui implementasi kode nyata.

## ✨ Fitur Unggulan

🎯 **Ready-to-Run Demos** - Semua kode bisa langsung dijalankan tanpa setup kompleks
🔄 **Multi-Language Learning** - Bandingkan implementasi Java vs Go untuk pemahaman lebih dalam  
📊 **Performance Analysis** - Benchmark dan perbandingan performa algoritma
🛡️ **Security Best Practices** - Implementasi yang mengikuti standar industri
📚 **Comprehensive Docs** - Dokumentasi lengkap dengan contoh dan use case nyata
🚀 **Production Templates** - Code yang bisa diadaptasi langsung ke proyek real

### 🏃‍♂️ **Quick Demo** (30 detik untuk mulai!)
```bash
# Clone & test Java AES encryption
git clone https://github.com/harvanir/SecurityDemo.git && cd SecurityDemo/code/java
mvn exec:java -Dexec.mainClass="org.harvanir.security.example.encryption.SimpleAesDemo"

# Test Go cipher comparison  
cd ../go && go run cmd/encryption/main.go
```

## 📁 Struktur Repository

```
SecurityDemo/
├── code/
│   ├── java/          # Java project (Maven) - Complete implementations
│   ├── go/            # Go implementations - Caesar & ASCII ciphers
│   └── kotlin/        # Kotlin implementations (future)
├── mtls/              # mTLS certificates dan tools
├── doc/               # Documentation files
├── *.md               # Main documentation
└── README.md          # This file
```

## 🎯 Tujuan Repository

Repository ini dibuat sebagai **one-stop learning hub** untuk cybersecurity implementation:

### 🎓 **Untuk Developer & Students**
- **Hands-on Learning**: Memahami konsep keamanan melalui kode yang bisa dijalankan langsung
- **Multi-Language Comparison**: Melihat bagaimana algoritma yang sama diimplementasikan di bahasa berbeda
- **Production-Ready Examples**: Template dan best practices untuk aplikasi nyata

### 🔬 **Untuk Security Enthusiasts**
- **Algorithm Deep Dive**: Analisis mendalam kelebihan, kekurangan, dan use case setiap metode
- **Performance Benchmarking**: Perbandingan performa antar algoritma dan bahasa
- **Security Analysis**: Penjelasan detail tentang attack vectors dan mitigasi

### 🚀 **Untuk Praktisi Industry**
- **Quick Reference**: Panduan cepat memilih algoritma yang tepat untuk kebutuhan spesifik
- **Code Templates**: Implementasi siap pakai yang bisa diadaptasi ke proyek real
- **Compliance Guidelines**: Memenuhi standar keamanan seperti OWASP, NIST, dll

## 📚 Topik yang Dibahas

> **💡 Learning Philosophy**: Dari basic ke advanced, dari teori ke implementasi nyata, dari single-language ke multi-language comparison!

### 🔐 **Encryption (Enkripsi)**
Implementasi berbagai algoritma enkripsi dari yang sederhana hingga modern:

**Java Implementations:**
- **Caesar Cipher** - Cipher klasik untuk pembelajaran
- **XOR Cipher** - Cipher sederhana untuk obfuscation
- **AES-GCM** - Enkripsi simetris modern yang direkomendasikan
- **RSA-2048** - Enkripsi asimetris untuk key exchange dan digital signature

**Go Implementations:**
- **Caesar Cipher** - Alphabet-only cipher dengan modular arithmetic
- **ASCII Cipher** - Simple ASCII character shifting cipher

📂 **Lokasi Java**: `code/java/src/main/java/org/harvanir/security/example/encryption/`
📂 **Lokasi Go**: `code/go/internal/encryption/`
📖 **Dokumentasi Detail**: [encryption_hashing.md](encryption_hashing.md)

### � **Hashing**
Implementasi algoritma hashing untuk keamanan password:
- **PBKDF2** - Standard untuk password hashing dengan salt
- **bcrypt** - Algoritma hashing yang umum digunakan
- **scrypt** - Memory-hard hashing untuk anti-GPU attacks
- **Argon2id** - Modern password hashing (OWASP recommendation)

📂 **Lokasi**: `code/java/src/main/java/org/harvanir/security/example/hashing/`
📖 **Dokumentasi Detail**: [encryption_hashing.md](encryption_hashing.md)

### 🤝 **Mutual TLS (mTLS)**
Implementasi autentikasi dua arah menggunakan certificate:
- **TLS Fundamentals** - Penjelasan dasar TLS handshake dan keystore/truststore
- **Certificate Generation** - Script untuk generate CA, server, dan client certificates
- **mTLS Handshake** - Simulasi dan implementasi mutual TLS
- **Keystore Management** - Pengelolaan certificate dan truststore

📂 **Lokasi**: `mtls/` (files) dan `code/java/src/main/java/org/harvanir/security/example/mtls/` (Java code)
📖 **Dokumentasi Detail**: [mTLS Guide](mtls/mtls-generation-guide.md)

## 🚀 Cara Menjalankan

### Prerequisites
- **Java 11+** (JDK)
- **Maven 3.6+**
- **Go 1.19+** (untuk Go implementations)
- **OpenSSL** (untuk generate mTLS certificates)

### Build & Run

#### **Java Project:**
```bash
# Clone repository
git clone https://github.com/harvanir/SecurityDemo.git
cd SecurityDemo

# Navigate to Java project
cd code/java

# Build project
mvn clean compile

# Run specific demo (contoh: AES demo)
mvn exec:java -Dexec.mainClass="org.harvanir.security.example.encryption.SimpleAesDemo"

# Run test
mvn test
```

#### **Go Project:**
```bash
# Navigate to Go project
cd code/go

# Initialize module (if not done already)
go mod init github.com/harvanir/SecurityDemo/code/go
go mod tidy

# Run encryption demo (Caesar & ASCII)
go run cmd/encryption/main.go

# Run specific cipher test
go run -c "import caesar 'github.com/harvanir/SecurityDemo/code/go/internal/encryption/caesar'; fmt.Println(caesar.Encrypt('Hello'))"
```

#### **VS Code:**
```bash
# Open Java workspace directly
cd SecurityDemo/code/java
code .

# Or use workspace file
code SecurityDemo.code-workspace
```
Setelah membuka di VS Code, Anda bisa klik tombol **"Run"** pada `public static void main` method.

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

### 🌱 **Level 1: Foundation (Pemula)**
**🎯 Goal**: Pahami konsep dasar cryptography
- **Start Here**: Caesar Cipher (Java + Go) → XOR Cipher → ASCII Cipher
- **Key Concepts**: Substitution, bit manipulation, character encoding
- **Hands-on**: Jalankan demo, modifikasi shift values, analisis output
- **Time**: 2-3 hari

### 🚀 **Level 2: Modern Crypto (Menengah)**  
**🎯 Goal**: Implementasi algoritma production-grade
- **Encryption**: AES-GCM → RSA-2048 → Hybrid (RSA+AES)
- **Hashing**: bcrypt → PBKDF2 → Argon2id comparison
- **Key Concepts**: Key management, salt, iteration counts, IV handling
- **Hands-on**: Build secure login system, file encryption tool
- **Time**: 1-2 minggu

### 🔥 **Level 3: Enterprise Security (Lanjutan)**
**🎯 Goal**: Production-ready security architecture
- **PKI**: Certificate generation → Trust chains → mTLS handshake
- **Architecture**: Security patterns, threat modeling, compliance
- **Performance**: Benchmarking, optimization, scalability
- **Hands-on**: Build microservices dengan mTLS, security audit
- **Time**: 2-4 minggu

### 🎖️ **Level 4: Security Expert (Master)**
**🎯 Goal**: Contribute to security community
- **Research**: Algorithm analysis, vulnerability assessment
- **Multi-language**: Cross-platform security implementation
- **Leadership**: Security best practices, team guidance
- **Contribution**: Open source contributions, security reviews

## ⚠️ Security Notice

> **🚨 IMPORTANT - READ BEFORE USE**: 
> 
> Repository ini dibuat untuk **PEMBELAJARAN dan DEVELOPMENT** purposes. 
> 
> ### ✅ **Safe for Learning:**
> - Educational demos dan proof-of-concept
> - Development environment testing
> - Academic research dan training
> 
> ### 🚫 **NOT for Production Use Without:**
> - **Security audit** dan penetration testing
> - **Proper key management** dan secure storage
> - **Parameter tuning** sesuai threat model
> - **Compliance review** (GDPR, SOX, HIPAA, etc.)
> - **Code review** oleh security expert
> 
> ### 📋 **Production Checklist:**
> - [ ] Security parameters review (key length, iterations, etc.)
> - [ ] Key management strategy implementation  
> - [ ] Audit logging dan monitoring
> - [ ] Error handling yang tidak leak information
> - [ ] Rate limiting dan brute force protection
> - [ ] Regular security updates dan patches

## 📋 Rekomendasi Quick Start

### **Java Implementations:**
| Kebutuhan | Solusi Rekomendasi | Demo File |
|-----------|-------------------|-----------|
| **Enkripsi data aplikasi** | AES-GCM | `SimpleAesDemo.java` |
| **Password storage** | Argon2id atau bcrypt | `Argon2idDemo.java` atau `BcryptDemo.java` |
| **Key exchange** | RSA-2048 | `Rsa2048MiniDemo.java` |
| **Mutual authentication** | mTLS | `MtlsSocketExample.java` |

### **Go Implementations:**
| Kebutuhan | Solusi Rekomendasi | Demo File |
|-----------|-------------------|-----------|
| **Learning cipher basics** | Caesar Cipher | `caesar.go` |
| **Simple text obfuscation** | ASCII Cipher | `ascii.go` |
| **Run all encryption demos** | Combined demo | `go run cmd/encryption/main.go` |

## 🤝 Kontribusi

Kontribusi selalu diterima! Silakan:
1. Fork repository ini
2. Buat branch untuk feature/fix Anda
3. Commit changes dengan pesan yang jelas
4. Submit Pull Request

## 📄 Lisensi

Silakan gunakan sesuai kebutuhan pembelajaran dan pengembangan Anda.
