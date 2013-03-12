package ulcambridge.foundations.viewer.genizah;


/**
 * Model class to hold all the fragments matching a search.
 * 
 * @author gilleain
 *
 */
public class FragmentSearchResult {
	
	/**
	 * The matched field.
	 */
	private final String searchMatch;
	
	/**
	 * A count of the number of references from bibliography entries to this fragment.
	 */
	private final int refCount;
	
	/**
	 * Information on the fragment.
	 */
	private final Fragment fragment;
	
	/**
	 * @param searchMatch
	 * @param bibliography
	 */
	public FragmentSearchResult(
			String searchMatch, Fragment fragment, int refCount) {
		this.searchMatch = searchMatch;
		this.fragment = fragment;
		this.refCount = refCount;
	}

	public String getSearchMatch() {
		return searchMatch;
	}

	public Fragment getFragment() {
		return fragment;
	}
	
	public int getRefCount() {
		return this.refCount;
	}

}
