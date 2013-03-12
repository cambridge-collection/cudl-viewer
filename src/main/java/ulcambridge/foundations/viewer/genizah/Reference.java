package ulcambridge.foundations.viewer.genizah;

/**
 * Reference to a fragment in a bibliography entry.
 * 
 * @author gilleain
 *
 */
public class Reference {
	
	private final String type;
	
	private final BibliographyEntry entry;
	
	public Reference(String type, BibliographyEntry entry) {
		this.type = type;
		this.entry = entry;
	}

	public String getType() {
		return type;
	}

	public BibliographyEntry getEntry() {
		return entry;
	}
	
}
