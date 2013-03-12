package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to represent a fragment referenced by a number of bibliography entries.
 * 
 * @author gilleain
 *
 */
public class FragmentReferenceList {
	
	/**
	 * The fragment referenced.
	 */
	private final Fragment fragment;
	
	/**
	 * Reference from a bibliography entry, with type.
	 */
	private final List<FragmentReferences> fragmentReferences;
	
	public FragmentReferenceList(Fragment fragment, List<FragmentReferences> fragmentReferences) {
		this.fragment = fragment;
		this.fragmentReferences = fragmentReferences;
	}
	
	public List<FragmentReferences> getFragmentReferences() {
		return fragmentReferences;
	}
	
	public List<BibliographyEntry> getBibliography() {
		List<BibliographyEntry> entries = new ArrayList<BibliographyEntry>();
		for (FragmentReferences reference : this.fragmentReferences) {
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
