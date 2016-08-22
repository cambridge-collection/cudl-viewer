package ulcambridge.foundations.viewer.authentication.oauth2;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ulcambridge.foundations.viewer.authentication.Obfuscation;

import java.util.function.Function;

public final class CudlProviders {

    public static AuthenticationProvider cudlOauthAuthenticationProvider(
        UserDetailsService userDetailsService) {

        return new DefaultOauth2AuthenticationProvider<>(
            userDetailsService(userDetailsService)
                .compose(CudlProviders::obfuscateOauth2Profile)
        );
    }

    static Profile obfuscateOauth2Profile(Profile profile) {
        String type = profile.getTypeName();

        return new DefaultProfile(type,
            Obfuscation.obfuscateUsername(type, profile.getId()),
            Obfuscation.obfuscate(profile.getEmailAddress()));
    }

    private static Function<Profile, Oauth2AuthenticationToken<Profile>>
    userDetailsService(UserDetailsService uds) {

        return profile -> {
            UserDetails ud = uds.loadUserByUsername(profile.getId());

            return new Oauth2AuthenticationToken<>(
                profile, ud, ud.getAuthorities());
        };
    }

    private CudlProviders() { throw new RuntimeException(); }
}
