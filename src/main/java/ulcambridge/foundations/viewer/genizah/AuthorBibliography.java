package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Model class to hold all the bibliograpy entries for a particular author.
 * 
 * @author gilleain
 *
 */
public class AuthorBibliography {
	
	/**
	 * The author searched for, among all the other authors.
	 */
	private final String searchAuthor;
	
	/**
	 * Information on the books.
	 */
	private final List<BibliographyEntry> bibliography;
	
	/**
	 * @param searchAuthor
	 * @param bibliography
	 */
	public AuthorBibliography(String searchAuthor, List<BibliographyEntry> bibliography) {
		this.searchAuthor = searchAuthor;
		this.bibliography = bibliography;
	}

	public String getSearchAuthor() {
		return searchAuthor;
	}

	public List<BibliographyEntry> getBibliography() {
		return bibliography;
	}

}
