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
	
	public FragmentReferences(String typeString, String position, BibliographyEntry entry) {
		super(typeString, position);
		this.entry = entry;
	}

	public BibliographyEntry getEntry() {
		return entry;
	}
	
}
