package ulcambridge.foundations.viewer.authentication;

public interface UsersDao {
	
	public User getActiveUserByUsername(String username);
	public User createUser(String username, String email);
	public AdminUser getAdminUserByUsername(String username);

}
