package ulcambridge.foundations.viewer.authentication.oauth2;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ulcambridge.foundations.viewer.authentication.Obfuscation;
import ulcambridge.foundations.viewer.authentication.UsersDao;

import java.util.function.Function;

public final class CudlProviders {

    /**
     * Create an AuthenticationProvider to create authentication tokens for
     * CUDL's OAuth2 sign in methods.
     *
     * @param userDetailsService The service to fetch UserDetails objects from.
     * @param usersDao The DAO to use when auto-creating new user accounts.
     * @return The OAuth2 provider.
     */
    public static AuthenticationProvider cudlOauthAuthenticationProvider(
        UserDetailsService userDetailsService, UsersDao usersDao) {

        // This Oauth2AuthenticationProvider takes a function which maps a
        // Profile object to an auth token to represent the user with.
        // We provide such a function which in the following order:
        // - Maps plain usernames/emails to obfuscated ones
        // - Catches missing users and creates an account before retrying
        // - Looks up users in a UserDetailsService and creates a token from the
        //   resulting UserDetails object.

        return new DefaultOauth2AuthenticationProvider<>(
            autoRegisterNewUsers(
                usersDao,
                //
                userDetailsService(userDetailsService))
                // Encode User IDs and emails before registering/signing in.
                .compose(CudlProviders::obfuscateOauth2Profile));
    }

    static Profile obfuscateOauth2Profile(Profile profile) {
        String type = profile.getTypeName();

        return new DefaultProfile(type,
            Obfuscation.obfuscateUsername(type, profile.getId()),
            profile.getEmailAddress()
                .map(Obfuscation::obfuscate)
                .orElse(null));
    }

    private static Function<Profile, Oauth2AuthenticationToken<Profile>>
    userDetailsService(UserDetailsService uds) {

        return profile -> {
            UserDetails ud = uds.loadUserByUsername(profile.getId());

            return new Oauth2AuthenticationToken<>(
                profile, ud, ud.getAuthorities());
        };
    }

    private static void createUser(Profile profile, UsersDao dao) {
        dao.createUser(profile.getId(), profile.getEmailAddress().orElse(null));
    }

    /**
     * Get a token creation function which attempts to authenticate an existing
     * user using the tokenCreator. If a user doesn't exist, the user is created
     * from the {@link Profile} using the provided {@link UsersDao}.
     *
     * @param usersDao The DAO to create users with.
     * @param tokenCreator The token creation (authentication) function to
     *                     delegate to.
     * @return A token creation function which transparently registers new
     *         users.
     */
    static Function<Profile, Oauth2AuthenticationToken<Profile>>
    autoRegisterNewUsers(
        UsersDao usersDao,
        Function<Profile, Oauth2AuthenticationToken<Profile>> tokenCreator) {

        return profile -> {
            try {
                return tokenCreator.apply(profile);
            }
            catch(UsernameNotFoundException e) {
                // A user for the profile doesn't exist yet, create one and
                // try again...
                usersDao.createUser(profile.getId(),
                                    profile.getEmailAddress().orElse(null));

                // Retry authentication
                return tokenCreator.apply(profile);
            }
        };
    }

    private CudlProviders() { throw new RuntimeException(); }
}
