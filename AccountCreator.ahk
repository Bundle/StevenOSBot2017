+v::addAccount()
+r::addEmail() 
+f::typePassword()
+n::typeURL()
typeURL() { 
Send, {ctrl down}t{ctrl up}
SendRaw, https://secure.runescape.com/m=account-creation/g=oldscape/create_account
Send, {enter}
}
typePassword() {
SendInput,0134201342
}
addEmail() {
	static starting := 210 - 1
	starting := starting + 1
	SendInput,stevenfakeaccountemail%starting%@gmail.com
	}
addAccount() {
	static names := ["akram1", "l1ngu1s7", "cobain000", "marine", "mark12345", "drymon", "telly5", "wxwhiz"]
	static nameIndex := 0
	nameIndex := nameIndex + 1
	SendInput, % names[nameIndex]
	}

