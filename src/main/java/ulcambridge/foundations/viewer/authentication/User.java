package ulcambridge.foundations.viewer.authentication;

import java.util.List;

public class User {

	private String password;
	private String username;
	private List<String> userRoles;
	
	public User(String username, String password, List<String> userRoles) {
		this.username = username;
		this.password = password;
		this.userRoles = userRoles;				
	}
	
	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public List<String> getUserRoles() {
		return this.userRoles;
	}

}
