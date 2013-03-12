package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to represent a fragment referenced by a number of bibliography entries.
 * 
 * @author gilleain
 *
 */
public class FragmentReferences {
	
	/**
	 * The fragment referenced.
	 */
	private final Fragment fragment;
	
	/**
	 * Reference from a bibliography entry, with type.
	 */
	private final List<Reference> bibliographyReferences;
	
	public FragmentReferences(Fragment fragment, List<Reference> bibliographyReferences) {
		this.fragment = fragment;
		this.bibliographyReferences = bibliographyReferences;
	}
	
	public List<Reference> getBibliographyReferences() {
		return bibliographyReferences;
	}
	
	public List<BibliographyEntry> getBibliography() {
		List<BibliographyEntry> entries = new ArrayList<BibliographyEntry>();
		for (Reference reference : this.bibliographyReferences) {
			BibliographyEntry entry = reference.getEntry();
			if (!entries.contains(entry)) {
				entries.add(entry);
			}
		}
		return entries;
	}
	
	public Fragment getFragment() {
		return fragment;
	}
	
}
