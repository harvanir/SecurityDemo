package caesar

const SHIFT = 3

func caesarEncrypt(text string, shift int) string {
	runes := []rune(text)
	for i, char := range runes {
		runes[i] = shiftChar(char, shift)
	}

	return string(runes)
}

func shiftChar(ch rune, shift int) rune {
	if ch >= 'A' && ch <= 'Z' {
		return shiftAlphabet(ch, shift, 'A')
	}
	if ch >= 'a' && ch <= 'z' {
		return shiftAlphabet(ch, shift, 'a')
	}
	return ch
}

func shiftAlphabet(ch rune, shift int, base rune) rune {
	// posisi relatif dari base
	offset := int(ch - base)

	// geser dan wrap modulo 26
	newPos := (offset + shift) % 26

	if newPos < 0 {
		newPos += 26
	}
	return base + rune(newPos)
}

func Encrypt(text string) string {
	return caesarEncrypt(text, SHIFT)
}

func Decrypt(encrypted string) string {
	return caesarEncrypt(encrypted, -SHIFT)
}
