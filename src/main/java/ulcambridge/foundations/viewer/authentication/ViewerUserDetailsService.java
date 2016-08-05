package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Optional;


/**
 * A {@link UserDetailsService} which fetches users from a {@link UsersDao}.
 */
public class ViewerUserDetailsService implements UserDetailsService {

    private final UsersDao usersDao;

    public ViewerUserDetailsService(UsersDao usersDao) {
        Assert.notNull(usersDao);

        this.usersDao = usersDao;
    }

    public UsersDao getUsersDao() {
        return this.usersDao;
    }

    @Override
    public ViewerUserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {

        return Optional.ofNullable(
                getUsersDao().getActiveUserByUsername(username))
            .map(user -> new ViewerUserDetails(user))
            .orElseThrow(() -> new UsernameNotFoundException(
                // This is the best we can do without getActiveUserByUsername
                // throwing an exception with more information.
                "getActiveUserByUsername returned null"));
    }
}
