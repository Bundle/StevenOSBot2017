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
	static starting := 203 - 1
	starting := starting + 1
	SendInput,stevenfakeaccountemail%starting%@gmail.com
	}
addAccount() {
	static names := ["rossumjr", "bschmuec", "FUF311EO", "ferpier", "damage4", "austin", "american30", "allenabo"]
	static nameIndex := 0
	nameIndex := nameIndex + 1
	SendInput, % names[nameIndex]
	}

