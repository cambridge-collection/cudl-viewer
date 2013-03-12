package ulcambridge.foundations.viewer.genizah;

/**
 * Model object representing the data in the Bibliograph table.
 * 
 * @author gilleain
 *
 */
public class BibliographyEntry {
	
	private String title;
	
	public BibliographyEntry(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

}
