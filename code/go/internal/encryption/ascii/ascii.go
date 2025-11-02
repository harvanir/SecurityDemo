package ascii

const SHIFT = 3

func encryptInternal(text string, shift int) string {
	runes := []rune(text)
	for i, char := range runes {
		runes[i] = char + rune(shift)
	}

	return string(runes)
}

func Encrypt(text string) string {
	return encryptInternal(text, SHIFT)
}

func Decrypt(encrypted string) string {
	return encryptInternal(encrypted, -SHIFT)
}
