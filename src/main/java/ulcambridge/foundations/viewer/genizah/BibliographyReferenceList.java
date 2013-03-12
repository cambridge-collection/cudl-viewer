package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Model class to represent all fragments referenced by a single bibliography entry.
 * 
 * @author gilleain
 *
 */
public class BibliographyReferenceList {
	
	/**
	 * The bibliography entry that is referencing.
	 */
	private final BibliographyEntry bibliographyEntry;
	
	/**
	 * Reference from a bibliography entry, with type.
	 */
	private final List<FragmentReferences> bibliographyReferences;
	
	public BibliographyReferenceList(BibliographyEntry bibligraphyEntry, List<FragmentReferences> references) {
		this.bibliographyEntry = bibligraphyEntry;
		this.bibliographyReferences = references;
	}
	
	public List<FragmentReferences> getBibliographyReferences() {
		return bibliographyReferences;
	}
	
	public BibliographyEntry getBibligraphyEntry() {
		return bibliographyEntry;
	}
	
}
