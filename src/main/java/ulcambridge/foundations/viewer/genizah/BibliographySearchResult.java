package ulcambridge.foundations.viewer.genizah;


/**
 * Model class to hold all the bibliograpy entries matching a search.
 * 
 * @author gilleain
 *
 */
public class BibliographySearchResult {
	
	/**
	 * The matched field.
	 */
	private final String searchMatch;
	
	/**
	 * A count of the number of references to fragments in this work.
	 */
	private int refCount;
	
	/**
	 * Information on the book.
	 */
	private final BibliographyEntry bibliographyEntry;
	
	/**
	 * @param searchMatch
	 * @param bibliography
	 */
	public BibliographySearchResult(
			String searchMatch, BibliographyEntry bibliographyEntry, int refCount) {
		this.searchMatch = searchMatch;
		this.bibliographyEntry = bibliographyEntry;
		this.refCount = refCount;
	}

	public String getSearchMatch() {
		return searchMatch;
	}

	public BibliographyEntry getBibliographyEntry() {
		return bibliographyEntry;
	}
	
	public int getRefCount() {
		return this.refCount;
	}
	
	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}

}
