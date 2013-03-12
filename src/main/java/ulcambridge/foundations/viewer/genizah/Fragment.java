package ulcambridge.foundations.viewer.genizah;

/**
 * Model object representing a Fragment from the database.
 * 
 * @author gilleain
 *
 */
public class Fragment {
	
	/**
	 * The 'LB' field in the Fragment table.
	 */
	private final String label;
	
	/**
	 * The classmark identifier generated from the label.
	 */
	private final String classmark;
	
	private final int titleID;
	
	public Fragment(int titleID, String label, String classmark) {
		this.label = label;
		this.classmark = classmark;
		this.titleID = titleID;
	}
	
	public String getClassmark() {
		return classmark;
	}

	public int getTitleId() {
		return titleID;
	}
	
	public String getLabel() {
		return label;
	}

}
