package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Model class to hold all the bibliograpy entries for a particular author.
 * 
 * @author gilleain
 *
 */
public class AuthorBibliography {
	
	private final String author;
	
	private final List<BibliographyEntry> bibliography;
	
	public AuthorBibliography(String author, List<BibliographyEntry> bibliography) {
		this.author = author;
		this.bibliography = bibliography;
	}

	public String getAuthor() {
		return author;
	}

	public List<BibliographyEntry> getBibliography() {
		return bibliography;
	}

}
