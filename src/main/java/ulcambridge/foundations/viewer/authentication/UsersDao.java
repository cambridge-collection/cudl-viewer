package ulcambridge.foundations.viewer.authentication;

public interface UsersDao {

	public User getActiveUserByUsername(String username);
	public void createOpenIdUser(String username);

}
