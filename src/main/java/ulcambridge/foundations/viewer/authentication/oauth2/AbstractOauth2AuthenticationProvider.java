package ulcambridge.foundations.viewer.authentication.oauth2;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public abstract class AbstractOauth2AuthenticationProvider<T, A extends Authentication>
    implements AuthenticationProvider {

    @Override
    public A authenticate(Authentication authentication)
        throws AuthenticationException {

        Oauth2AuthenticationToken<T> token =
            (Oauth2AuthenticationToken)authentication;

        assert !token.isAuthenticated();

        T profile = token.getCredentials()
            .orElseThrow(() -> new IllegalStateException("Missing profile object"));

        // This constructor always creates authenticated tokens.
        A authenticatedToken =
            this.createAuthenticatedToken(profile);

        if(!authenticatedToken.isAuthenticated())
            throw new IllegalStateException(
                "createAuthenticatedToken() returned unauthenticated token");

        return authenticatedToken;
    }

    abstract A createAuthenticatedToken(T profile);

    @Override
    public boolean supports(Class<?> authentication) {
        return Oauth2AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
