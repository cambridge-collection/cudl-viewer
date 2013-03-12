package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Model class to represent all fragments referenced by a single bibliography entry.
 * 
 * @author gilleain
 *
 */
public class BibliographyReferences {
	
	/**
	 * The bibliography entry that is referencing.
	 */
	private final BibliographyEntry bibliographyEntry;
	
	/**
	 * Reference from a bibliography entry, with type.
	 */
	private final List<Reference> bibliographyReferences;
	
	public BibliographyReferences(BibliographyEntry bibligraphyEntry, List<Reference> references) {
		this.bibliographyEntry = bibligraphyEntry;
		this.bibliographyReferences = references;
	}
	
	public List<Reference> getBibliographyReferences() {
		return bibliographyReferences;
	}
	
	public BibliographyEntry getBibligraphyEntry() {
		return bibliographyEntry;
	}
	
}
