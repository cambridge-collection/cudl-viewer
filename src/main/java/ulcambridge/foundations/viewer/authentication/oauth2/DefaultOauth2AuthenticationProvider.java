package ulcambridge.foundations.viewer.authentication.oauth2;


import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.function.Function;

public class DefaultOauth2AuthenticationProvider<T, A extends Authentication>
    extends AbstractOauth2AuthenticationProvider<T, A> {

    private final Function<T, A> authenticatedTokenCreator;

    public DefaultOauth2AuthenticationProvider(
        Function<T, A> authenticatedTokenCreator) {

        Assert.notNull(authenticatedTokenCreator);

        this.authenticatedTokenCreator = authenticatedTokenCreator;
    }

    @Override
    A createAuthenticatedToken(T profile) {
        return this.authenticatedTokenCreator.apply(profile);
    }
}
