package ulcambridge.foundations.viewer.authentication.raven;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import uk.ac.cam.lib.spring.security.raven.AuthenticatedRavenTokenCreator;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationToken;

public abstract class RavenTokenCreatorWrapper
    implements AuthenticatedRavenTokenCreator {

    private final AuthenticatedRavenTokenCreator target;

    public RavenTokenCreatorWrapper(AuthenticatedRavenTokenCreator target) {
        Assert.notNull(target);

        this.target = target;
    }

    /**
     * @return the wrapped token creator.
     */
    public AuthenticatedRavenTokenCreator getTarget() {
        return this.target;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation returns the result of calling this method on
     * the wrapped {@link AuthenticatedRavenTokenCreator} returned by
     * {@link #getTarget()}.
     */
    @Override
    public Authentication createAuthenticatedToken(
        RavenAuthenticationToken validatedToken) {

        return this.getTarget().createAuthenticatedToken(validatedToken);
    }
}
