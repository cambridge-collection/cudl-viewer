package ulcambridge.foundations.viewer.genizah;


/**
 * References to a fragment in a bibliography entry.
 * 
 * @author gilleain
 *
 */
public class FragmentReferences extends AbstractReference {
	
	/**
	 * The bibliography entry that references this fragment.
	 */
	private final BibliographyEntry entry;
	
	public FragmentReferences(String typeString, BibliographyEntry entry) {
		super(typeString);
		this.entry = entry;
	}

	public BibliographyEntry getEntry() {
		return entry;
	}
	
}
