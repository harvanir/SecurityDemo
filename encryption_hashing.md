# Perbandingan CaesarCipher, XorCipher, AES-GCM (PSK), PBKDF2, RSA-2048, bcrypt, scrypt, dan Argon2id

> **Multi-Language Implementation**: Repository ini mengimplementasikan algoritma keamanan dalam **Java** (lengkap) dan **Go** (Caesar & ASCII cipher untuk pembelajaran). Lihat bagian [Implementasi Multi-Bahasa](#implementasi-multi-bahasa) di bawah untuk detail.

## Tabel Perbandingan Utama

| Metode | Tipe | Arah | Keamanan | Biaya CPU | Kunci/Parameter | Kapan Dipakai | Catatan Penting |
|---|---|---|---|---|---|---|---|
| **Caesar Cipher** | Cipher klasik (shift) | 2-arah | ❌ Sangat lemah | ⚡ Sangat ringan | shift (angka) | Obfuscation/latihan | Mudah ditebak; jangan untuk data rahasia. |
| **XOR Cipher** | Cipher sederhana (XOR 1 kunci) | 2-arah | ❌ Lemah | ⚡ Sangat ringan | 1 byte/array key | Obfuscation/latihan | Pola mudah dianalisis; bukan kripto aman. |
| **AES-GCM (PSK)** | Kripto modern simetris | 2-arah | ✅ Kuat (dengan IV unik) | ⚡⚡ Ringan (cepat) | key 128/256-bit + IV 12B | Enkripsi data aplikasi/API/IoT | **Jangan reuse IV** untuk key yang sama. Integrity tag bawaan. |
| **RSA-2048 (OAEP)** | Kripto asimetris | 2-arah (pesan pendek) | ✅ Kuat | 🐢 Lebih berat | public/private key | Enkripsi pendek, distribusi key, tanda tangan | Untuk data besar → pakai hybrid (RSA + AES). |
| **PBKDF2** | Password hashing (KDF) | 1-arah (verifikasi) | ✅ Kuat (bergantung iterasi) | 🐢 Lambat by design | password + salt + iterasi | Simpan/verifikasi password | Bukan enkripsi. Tujuannya anti-bruteforce. |
| **bcrypt** | Password hashing (CPU-bound) | 1-arah | ✅ Kuat (masih luas dipakai) | ⚖️ Sedang | cost factor (10–12) | Aplikasi umum (login, web) | Mudah diimplementasikan; salt otomatis di-hash. |
| **scrypt** | Password hashing (memory-hard) | 1-arah | ✅ Lebih kuat dari PBKDF2 | 🐢 Lebih berat (pakai RAM) | N, r, p (parameter) | Server modern, hindari GPU attack | Aman tapi lebih berat pada RAM tinggi. |
| **Argon2id** | Password hashing (modern hybrid) | 1-arah | ✅✅ Paling kuat (OWASP rekomendasi) | ⚖️ Bisa diatur | iter, memory, parallelism | Sistem modern, sensitif security | Kombinasi CPU+memory-hard, tahan GPU & side-channel. |
---

## Rekomendasi Pemakaian

| Kebutuhan | Solusi |
|---|---|
| Enkripsi 2-arah cepat & aman | **AES-GCM (PSK)** |
| Menyimpan password (umum) | **bcrypt (cost 10–12)** |
| Menyimpan password (modern/high security) | **Argon2id** |
| Hindari brute force GPU | **scrypt / Argon2id** |
| Kompatibilitas lama | **PBKDF2 (SHA-256)** |
| Kirim kunci / tanda tangan / enkripsi pesan pendek | **RSA-2048 (OAEP/PSS)** |
| Belajar / obfuscation ringan | **Caesar / XOR** |

> Prinsip dasar: **AES untuk data**, **RSA untuk kunci**, **bcrypt/Argon2id untuk password**.

---

## Pro & Kontra

### **AES-GCM (PSK)**
- ✅ Cepat (sering hardware AES-NI)
- ✅ Ada authentication tag (integritas terjamin)
- ⚠️ **IV wajib unik** per enkripsi (boleh disimpan bersama ciphertext)

### **PBKDF2**
- ✅ Standar luas dan aman
- ⚠️ CPU-bound, bisa dipecahkan GPU
- ⚠️ Lambat by design (memang untuk memperlambat brute force)

### **bcrypt**
- ✅ Mudah dipakai, ada di banyak bahasa (Java, Node.js, Python, Go)
- ✅ Salt otomatis disertakan dalam hash
- ⚠️ Tidak memory-hard (masih CPU-bound)
- ⚖️ Cepat diimplementasikan tapi kurang optimal untuk hardware modern (GPU attack masih mungkin)

### **scrypt**
- ✅ Memory-hard (menggunakan RAM untuk hashing)
- ✅ Lebih sulit dipecahkan oleh GPU/ASIC
- ⚠️ Lebih berat di RAM dan setup
- ⚖️ Pilihan bagus bila sistem mendukung resource cukup

### **Argon2id**
- ✅ Kombinasi CPU + memory hard (modern)
- ✅ Aman terhadap GPU dan side-channel attack
- ✅ Dapat diatur (iterasi, memori, paralelisme)
- ⚖️ Ideal untuk sistem baru, menggantikan PBKDF2/bcrypt

### **RSA-2048**
- ✅ Aman untuk key exchange & signature
- ⚠️ Lambat dan plaintext terbatas
- ⚠️ Untuk data besar gunakan **hybrid: RSA + AES**

### **Caesar/XOR**
- ✅ Sangat ringan & simple
- ❌ Tidak aman sama sekali

---

## Do & Don’t Checklist

| Do ✅ | Don’t ❌ |
|---|---|
| Pakai AES-GCM (bukan ECB) | Menggunakan AES-ECB |
| Pakai IV unik per enkripsi | Reuse IV |
| Pakai bcrypt/Argon2id untuk password | Menyimpan password dalam bentuk encrypted |
| Pakai RSA-OAEP / RSA-PSS | RSA-PKCS#1 v1.5 untuk enkripsi |
| Hybrid untuk data besar | RSA untuk file besar |
| Gunakan scrypt/Argon2id untuk anti-GPU | Gunakan PBKDF2 tanpa mempertimbangkan iterasi tinggi |

---

## Kesimpulan

- **AES-GCM (PSK)** = pilihan terbaik untuk enkripsi 2-arah yang cepat & aman  
- **bcrypt / Argon2id** = hashing password paling direkomendasikan untuk sistem modern  
- **PBKDF2** = masih aman untuk kompatibilitas luas (jika iterasi tinggi)  
- **scrypt** = tahan GPU, cocok bila RAM cukup  
- **RSA-2048** = distribusi kunci & signature  
- **Caesar/XOR** = hanya untuk edukasi atau obfuscation level rendah  

---

## Implementasi Multi-Bahasa

### **Java Implementations** (Lengkap)
| Algoritma | Library Java | Maven Dependency | Contoh Package |
|------------|---------------|------------------|----------------|
| **AES-GCM** | Java JCE (core) | *bawaan JDK* | `javax.crypto.*` |
| **PBKDF2** | Java JCE (core) | *bawaan JDK* | `SecretKeyFactory`, `PBEKeySpec` |
| **bcrypt** | `jBCrypt` | `org.mindrot:jbcrypt:0.4` | `org.mindrot.jbcrypt.BCrypt` |
| **scrypt** | `BouncyCastle` / `com.lambdaworks:scrypt` | `com.lambdaworks:scrypt:1.4.0` | `SCryptUtil` |
| **Argon2id** | `argon2-jvm` / Spring Security Crypto | `de.mkammerer:argon2-jvm:2.11` | `Argon2Factory.create(Argon2Types.ARGON2id)` |
| **RSA-2048** | Java JCE (core) | *bawaan JDK* | `java.security.KeyPairGenerator`, `Cipher` |

📂 **Lokasi**: `code/java/src/main/java/org/harvanir/security/example/`

### **Go Implementations** (Pembelajaran)
| Algoritma | Package Path | Deskripsi | Status |
|-----------|--------------|-----------|---------|
| **Caesar Cipher** | `internal/encryption/caesar` | Alphabet-only cipher dengan modular arithmetic | ✅ Done |
| **ASCII Cipher** | `internal/encryption/ascii` | Simple ASCII character shifting | ✅ Done |
| **AES-GCM** | `internal/encryption/aes` | Modern symmetric encryption | 🔄 TODO |
| **RSA** | `internal/encryption/rsa` | Asymmetric encryption | 🔄 TODO |
| **Argon2** | `internal/hashing/argon2` | Modern password hashing | 🔄 TODO |
| **bcrypt** | `internal/hashing/bcrypt` | Password hashing | 🔄 TODO |

📂 **Lokasi**: `code/go/internal/`
📖 **Dokumentasi**: [code/go/README.md](code/go/README.md)

**Cara menjalankan Go implementations:**
```bash
cd code/go
go run cmd/encryption/main.go
```

---

## Benchmark Umum (rata-rata di laptop modern)

| Algoritma | Waktu rata-rata per operasi | Catatan |
|------------|-----------------------------|----------|
| Caesar/XOR | < 0.1 ms | tidak aman |
| AES-GCM (256-bit) | ~1 ms | cepat & efisien |
| PBKDF2 (150k iterasi) | 120–200 ms | aman, tapi CPU-bound |
| bcrypt (cost=12) | 150–250 ms | aman, stabil |
| scrypt (N=16384,r=8,p=1) | 200–400 ms | memory-hard |
| Argon2id (64MB, t=3, p=2) | 200–400 ms | paling aman & fleksibel |
| RSA-2048 encrypt/decrypt | 1–5 ms (pesan pendek) | lambat untuk data besar |

---

## Rekomendasi Akhir
- 🔹 **Gunakan AES-GCM untuk enkripsi data.**
- 🔹 **Gunakan Argon2id (atau bcrypt jika perlu performa) untuk password.**
- 🔹 **Gunakan RSA untuk key exchange atau signature.**
- 🔹 **Gunakan PBKDF2 hanya jika butuh kompatibilitas luas (mis. Java lama).**
- 🔹 **Jangan gunakan Caesar atau XOR untuk keamanan nyata.**

---
