+v::addAccount()
+r::addEmail() 
+f::typePassword()
typePassword() {
SendInput,0134201342
}
addEmail() {
	static starting := 63 - 1
	starting := starting + 1
	SendInput,stevenfakeaccountemail%starting%@gmail.com
	}
addAccount() {
	static names := ["jsundar3000n", "dick234utcST", "jack1431UKcm", "heartWrVPyow", "smitty9ArvdU", "Spud48uklHEr", "melly1OABsYX", "mariostanca"]
	static nameIndex := 0
	nameIndex := nameIndex + 1
	SendInput, % names[nameIndex]
	}

