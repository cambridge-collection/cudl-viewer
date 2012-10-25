package ulcambridge.foundations.viewer.authentication;

public interface UsersDao {

	public User getByUsername(String username);
	public void createNewUser(String username);

}
