package ulcambridge.foundations.viewer.authentication.raven;


import org.springframework.security.core.userdetails.UserDetailsService;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationToken;
import uk.ac.cam.lib.spring.security.raven.hooks.UserDetailsRavenTokenCreator;
import ulcambridge.foundations.viewer.authentication.Obfuscation;

/**
 * This is responsible for mapping the CRSIDs received from Raven into the
 * slightly obfuscated format stored in the CUDL database.
 *
 * <p>Raven usernames are stored as {@code raven:<hash>} where {@code <hash>} is
 * the hex-encoded sha256 hash of the crsid.
 */
public class CudlUserDetailsRavenTokenCreator
    extends UserDetailsRavenTokenCreator {

    public CudlUserDetailsRavenTokenCreator(
        UserDetailsService userDetailsService) {

        super(userDetailsService);
    }

    @Override
    protected String getUsername(RavenAuthenticationToken token) {
        return Obfuscation.obfuscateUsername("raven", super.getUsername(token));
    }
}
