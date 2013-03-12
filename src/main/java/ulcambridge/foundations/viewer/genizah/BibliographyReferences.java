package ulcambridge.foundations.viewer.genizah;


/**
 * References from a bibliography entry to a fragment.
 * 
 * @author gilleain
 *
 */
public class BibliographyReferences extends AbstractReference {
	
	/**
	 * The fragment referred to by the bibliography entry.
	 */
	private final Fragment fragment;
	
	public BibliographyReferences(String typeString, Fragment fragment) {
		super(typeString);
		this.fragment = fragment;
	}

	public Fragment getFragment() {
		return fragment;
	}
	
}
