package ulcambridge.foundations.viewer.model;

/**
 * This class contains information on a person. Could be an author, scribe etc.
 * 
 * @author jennie
 * 
 */
public class Person implements Comparable<Person> {

	private String authForm;
	private String shortForm;
	private String authority;
	private String authorityURI;
	private String valueURI;
	private String type;
	private String role;

	public Person(String authForm, String shortForm, String authorityURI,
			String authority, String valueURI, String type, String role) {

		this.authForm = authForm;
		this.shortForm = shortForm;
		this.authority = authority;
		this.authorityURI = authorityURI;
		this.valueURI = valueURI;
		this.type = type;
		this.role = role;

	}

	public String getFullForm() {
		return authForm;
	}

	public String getDisplayForm() {
		return shortForm;
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
		return shortForm;
	}

	@Override
	public int compareTo(Person o) {
		return getFullForm().compareTo(o.getFullForm());
	}

}
