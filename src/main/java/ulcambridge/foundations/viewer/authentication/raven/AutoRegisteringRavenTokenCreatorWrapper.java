package ulcambridge.foundations.viewer.authentication.raven;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import uk.ac.cam.lib.spring.security.raven.AuthenticatedRavenTokenCreator;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationToken;
import ulcambridge.foundations.viewer.authentication.UsersDao;

import java.util.function.Function;

/**
 * An {@link AuthenticatedRavenTokenCreator} which auto-registers users with a
 * {@link UsersDao} if a delegated token creator throws a
 * {@link UsernameNotFoundException}. After the user is registered the
 * authentication is retried.
 */
public class AutoRegisteringRavenTokenCreatorWrapper
    extends RavenTokenCreatorWrapper {

    private final UsersDao usersDao;
    private final Function<String, String> usernameEncoder;
    private final Function<String, String> emailEncoder;

    public AutoRegisteringRavenTokenCreatorWrapper(
        AuthenticatedRavenTokenCreator target, UsersDao usersDao,
        Function<String, String> usernameEncoder,
        Function<String, String> emailEncoder) {

        super(target);

        Assert.notNull(usersDao);
        Assert.notNull(usernameEncoder);
        Assert.notNull(emailEncoder);

        this.usersDao = usersDao;
        this.usernameEncoder = usernameEncoder;
        this.emailEncoder = emailEncoder;
    }

    public UsersDao getUsersDao() {
        return this.usersDao;
    }

    protected String getUsername(RavenAuthenticationToken token) {
        return token.getName();
    }

    protected String getEncodedUsername(RavenAuthenticationToken token) {
        return this.usernameEncoder.apply(getUsername(token));
    }

    protected String getEmail(RavenAuthenticationToken token) {
        return getUsername(token) + "@cam.ac.uk";
    }

    protected String getEncodedEmail(RavenAuthenticationToken token) {
        return this.emailEncoder.apply(this.getEmail(token));
    }

    protected void registerUser(RavenAuthenticationToken token) {
        String username = this.getEncodedUsername(token);
        String email = this.getEncodedEmail(token);

        this.getUsersDao().createUser(username, email);
    }

    @Override
    public Authentication createAuthenticatedToken(RavenAuthenticationToken validatedToken) {
        try {
            return super.createAuthenticatedToken(validatedToken);
        }
        catch (UsernameNotFoundException e) {
            this.registerUser(validatedToken);

            return super.createAuthenticatedToken(validatedToken);
        }
    }
}
