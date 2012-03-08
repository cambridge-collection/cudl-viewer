package ulcambridge.foundations.viewer.model;

/**
 * This class contains information on a person. Could be an author, scribe etc.
 * 
 * @author jennie
 * 
 */
public class Person implements Comparable<Person> {

	private String fullForm;
	private String displayForm;
	private String authority;
	private String authorityURI;
	private String valueURI;
	private String type;
	private String role;

	public Person(String fullForm, String displayForm, String authorityURI,
			String authority, String valueURI, String type, String role) {

		this.fullForm = fullForm;
		this.displayForm = displayForm;
		this.authority = authority;
		this.authorityURI = authorityURI;
		this.valueURI = valueURI;
		this.type = type;
		this.role = role;

	}

	public String getFullForm() {
		return fullForm;
	}

	public String getDisplayForm() {
		return displayForm;
	}
	
	public String getAuthority() {
		return authority;
	}	

	public String getAuthorityURI() {
		return authorityURI;
	}

	public String getValueURI() {
		return valueURI;
	}

	public String getType() {
		return type;
	}

	public String getRole() {
		return role;
	}
	
	@Override
	public String toString() {
		return displayForm;
	}

	@Override
	public int compareTo(Person o) {
		return getFullForm().compareTo(o.getFullForm());
	}

}
