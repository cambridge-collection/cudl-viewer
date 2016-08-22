package ulcambridge.foundations.viewer.authentication.oauth2;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;


public class Oauth2AuthenticationToken<T>
    extends AbstractAuthenticationToken {

    private Optional<T> profile;
    private final Object principle;

    public Oauth2AuthenticationToken(T profile) {
        super(null);

        Assert.notNull(profile);

        this.profile = Optional.of(profile);
        this.principle = profile;
    }

    public Oauth2AuthenticationToken(T profile, Object principle,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        this.profile = Optional.of(profile);
        this.principle = principle;

        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new IllegalArgumentException(
            "Cannot set this token to trusted - use constructor");
    }

    @Override
    public Optional<T> getCredentials() {
        return this.profile;
    }

    @Override
    public Object getPrincipal() {
        return this.principle;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();

        this.profile = Optional.empty();
    }
}
