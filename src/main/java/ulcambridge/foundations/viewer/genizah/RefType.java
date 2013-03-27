package ulcambridge.foundations.viewer.genizah;

/**
 * The 'type' of a reference, as defined by the short form ('m', 'y', etc) along with a 
 * more readable form like "mention" or "partial text".
 * 
 * @author gilleain
 *
 */
public enum RefType {
	m("mention"),
	y("partial text"),
	x("full text"),
	yt("partial text with translation"),
	xt("full text with translation"),
	ty("partial translation, no text"),
	tx("full translation, no text"),
	//E("addendum"),	XXX altered to obscure false addenda
	E("mention"),
	u("unknown");
	
	private String readableForm;
	
	private RefType(String readableForm) {
		this.readableForm = readableForm;
	}
	
	public String getReadableForm() {
		return readableForm;
	}
}