package ulcambridge.foundations.viewer.genizah;

public enum RisTag {
	
	CY("Place Published"),
	DA("Date"),
	DO("DOI"),
	ET("Edition"),
	M1("Number"),
	NV("Volume Number"),
	OP("Original Publisher"),
	PB("Publisher"),
	PY("Year"),
	RN("Research Notes"),
	RP("Reprint Edition"),
	SN("ISBN"),
	SP("Start Page"),
	ST("Short Title"),
	SV("SV"),
	T2("Secondary Title"),
	TI("Title"),
	TT("Translated Title"),
	TY("Type"),
	VL("Volume");
	
	private String readableForm;
	
	private RisTag(String readableForm) {
		this.readableForm = readableForm;
	}
	
	public String getReadableForm() {
		return readableForm;
	}

}
