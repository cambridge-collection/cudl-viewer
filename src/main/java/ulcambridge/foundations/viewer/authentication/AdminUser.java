package ulcambridge.foundations.viewer.authentication;

import java.io.Serializable;
import java.util.List;

public class AdminUser extends User implements Serializable {

	private static final long serialVersionUID = 3422058233282859772L;
	private String adminEmail;
	private String adminName;

	public AdminUser(String username, String password, String email,
			boolean enabled, List<String> userRoles) {
		super(username, password, email, enabled, userRoles);
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

}
