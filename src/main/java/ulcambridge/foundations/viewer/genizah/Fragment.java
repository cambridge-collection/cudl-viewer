package ulcambridge.foundations.viewer.genizah;

/**
 * Model object representing a Fragment from the database.
 * 
 * @author gilleain
 *
 */
public class Fragment {
	
	private final String classmark;
	
	private final int titleID;
	
	public Fragment(String classmark, int titleID) {
		this.classmark = classmark;
		this.titleID = titleID;
	}
	
	public String getClassmark() {
		return classmark;
	}

	public int getTitleID() {
		return titleID;
	}

}
