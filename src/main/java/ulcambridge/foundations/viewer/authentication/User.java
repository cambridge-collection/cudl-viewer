package ulcambridge.foundations.viewer.authentication;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = 985462454355609846L;
	
	private String password;
	private String username;
	private boolean enabled;
	private List<String> userRoles;
	
	public User(String username, String password, boolean enabled, List<String> userRoles) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
