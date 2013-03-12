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
	
	public Fragment(String label, String classmark) {
		this.label = label;
		this.classmark = classmark;
	}
	
	public String getClassmark() {
		return classmark;
	}
	
	public String getLabel() {
		return label;
	}

}
