# SecurityDemo - Go Implementations

This directory contains Go implementations of various cryptographic algorithms and security concepts for learning purposes.

## 📁 Project Structure

```
code/go/
├── go.mod                     # Go module definition
├── cmd/                       # Executable applications
│   ├── encryption/
│   │   └── main.go           # Combined encryption demo
│   ├── hashing/
│   │   └── main.go           # Hashing algorithms (TODO)
│   └── mtls/
│       └── main.go           # mTLS implementation (TODO)
├── internal/                  # Internal packages
│   ├── encryption/
│   │   ├── caesar/
│   │   │   └── caesar.go     # Caesar cipher implementation
│   │   ├── ascii/
│   │   │   └── ascii.go      # ASCII shift cipher
│   │   ├── aes/              # AES implementation (TODO)
│   │   └── rsa/              # RSA implementation (TODO)
│   ├── hashing/
│   │   ├── argon2/           # Argon2 implementation (TODO)
│   │   ├── bcyrpt/           # Bcrypt implementation (TODO)
│   │   └── sha256/           # SHA-256 implementation (TODO)
│   └── mtls/                 # mTLS utilities (TODO)
├── encryption/               # Public encryption utilities
└── README.md                 # This file
```

## 🚀 Quick Start

### Prerequisites
- Go 1.19 or higher
- Git (for cloning)

### Setup and Run

1. **Initialize Go module** (if not done):
   ```bash
   cd code/go
   go mod init github.com/harvanir/SecurityDemo/code/go
   go mod tidy
   ```

2. **Run encryption demos**:
   ```bash
   # Run all encryption algorithms
   go run cmd/encryption/main.go
   
   # Expected output:
   # === Encryption Demo ===
   # === Caesar Encryption Demo ===
   # Plaintext : Hello Harvan, ini rahasia.
   # Encrypted : Khoor Kduydq, lql udkdvld.
   # Decrypted : Hello Harvan, ini rahasia.
   # 
   # === ASCII Encryption Demo ===
   # Plaintext : Hello Harvan, ini rahasia.
   # Encrypted : Khoor#Kduydq/#lql#udkdvld1
   # Decrypted : Hello Harvan, ini rahasia.
   ```

3. **Import specific packages**:
   ```go
   import (
       caesar "github.com/harvanir/SecurityDemo/code/go/internal/encryption/caesar"
       ascii "github.com/harvanir/SecurityDemo/code/go/internal/encryption/ascii"
   )
   ```

## 🔐 Implemented Algorithms

### **Encryption Algorithms**

#### **Caesar Cipher** (`internal/encryption/caesar`)
- **Description**: Classic substitution cipher that shifts alphabet letters by a fixed amount
- **Key Features**: 
  - Only affects alphabetic characters (A-Z, a-z)
  - Preserves case and non-alphabetic characters
  - Uses modular arithmetic for proper wrapping
- **Use Case**: Educational purposes, understanding basic cipher concepts
- **Security**: ⚠️ Not secure - easily broken with frequency analysis

**Example:**
```go
plaintext := "Hello World!"
encrypted := caesar.Encrypt(plaintext)  // "Khoor Zruog!"
decrypted := caesar.Decrypt(encrypted)  // "Hello World!"
```

#### **ASCII Cipher** (`internal/encryption/ascii`)
- **Description**: Simple cipher that shifts all characters by their ASCII values
- **Key Features**:
  - Affects all characters (including symbols, numbers, spaces)
  - Simple addition/subtraction operation
  - May produce non-printable characters
- **Use Case**: Basic text obfuscation, learning character manipulation
- **Security**: ⚠️ Not secure - trivial to reverse

**Example:**
```go
plaintext := "Hello!"
encrypted := ascii.Encrypt(plaintext)   // "Khoor$"
decrypted := ascii.Decrypt(encrypted)   // "Hello!"
```

## 🎯 Algorithm Comparison

| Algorithm | Input Handling | Security Level | Use Case | Complexity |
|-----------|---------------|----------------|----------|------------|
| **Caesar** | Alphabets only | Very Low | Education | O(n) |
| **ASCII** | All characters | Very Low | Obfuscation | O(n) |

## 🔧 Development Guidelines

### **Adding New Algorithms**

1. **Create package directory**: `internal/encryption/{algorithm_name}/`
2. **Implement standard interface**:
   ```go
   package algorithm_name
   
   func Encrypt(plaintext string) string {
       // Implementation here
   }
   
   func Decrypt(ciphertext string) string {
       // Implementation here
   }
   ```
3. **Add to main demo**: Update `cmd/encryption/main.go`
4. **Add tests**: Create `{algorithm_name}_test.go`

### **Code Structure Standards**
- Use internal packages for implementation details
- Export only necessary functions (Encrypt, Decrypt)
- Include constants for configuration (SHIFT, KEY_SIZE, etc.)
- Add comprehensive comments and documentation
- Follow Go naming conventions

## 🧪 Testing

```bash
# Run all tests
go test ./...

# Run tests with coverage
go test -cover ./...

# Test specific package
go test ./internal/encryption/caesar
```

## 📚 Learning Path

### **Beginner**
1. Start with Caesar Cipher to understand basic substitution
2. Try ASCII Cipher to see character manipulation
3. Compare the two approaches and their limitations

### **Next Steps** (TODO)
1. Implement XOR cipher for bitwise operations
2. Add AES for symmetric encryption
3. Implement RSA for asymmetric encryption
4. Build hybrid encryption systems

### **Advanced** (TODO)
1. Add proper key management
2. Implement secure random number generation
3. Add cryptographic hashing functions
4. Build mTLS implementation

## ⚠️ Security Disclaimer

> **Important**: The implementations in this repository are for **educational purposes only**. 
> 
> **DO NOT USE IN PRODUCTION**:
> - Caesar and ASCII ciphers provide NO real security
> - Missing proper key management
> - No protection against timing attacks
> - Not audited for security vulnerabilities
> 
> For production use:
> - Use well-tested cryptographic libraries
> - Follow security best practices
> - Conduct security audits
> - Implement proper key management

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Add tests for new algorithms
4. Update documentation
5. Submit a Pull Request

## 📖 References

- [Go Cryptography Documentation](https://pkg.go.dev/crypto)
- [Practical Cryptography](http://practicalcryptography.com/)
- [OWASP Cryptographic Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)