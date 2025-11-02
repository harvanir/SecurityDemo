package main

import (
	"fmt"

	ascii "github.com/harvanir/SecurityDemo/code/go/internal/encryption/ascii"
	caesar "github.com/harvanir/SecurityDemo/code/go/internal/encryption/caesar"
)

func main() {
	fmt.Println("=== Encryption Demo ===")

	plaintext := "Hello Harvan, ini rahasia."

	fmt.Println("=== Caesar Encryption Demo ===")
	encrypted := caesar.Encrypt(plaintext)
	decrypted := caesar.Decrypt(encrypted)

	fmt.Println("Plaintext :", plaintext)
	fmt.Println("Encrypted :", encrypted)
	fmt.Println("Decrypted :", decrypted)
	fmt.Println()

	fmt.Println("=== ASCII Encryption Demo ===")

	encrypted = ascii.Encrypt(plaintext)
	decrypted = ascii.Decrypt(encrypted)

	fmt.Println("Plaintext :", plaintext)
	fmt.Println("Encrypted :", encrypted)
	fmt.Println("Decrypted :", decrypted)
}
