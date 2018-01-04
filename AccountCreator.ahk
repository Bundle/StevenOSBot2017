+v::addAccount()
+r::addEmail() 
+f::typePassword()
typePassword() {
SendInput,0134201342
}
addEmail() {
	static starting := 32 - 1
	starting := starting + 1
	SendInput,stevenfakeaccountemail%starting%@gmail.com
	}
addAccount() {
	static names := ["jeansebOiEKs", "demensioYWzK", "KOOLMANFHZok", "slave482PIMQ", "golferdgDeNS", "baldingercnK", "snowbordr36p", "gxv5377945"]
	static nameIndex := 0
	nameIndex := nameIndex + 1
	SendInput, % names[nameIndex]
	}

