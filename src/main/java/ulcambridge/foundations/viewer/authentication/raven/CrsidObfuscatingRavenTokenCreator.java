package ulcambridge.foundations.viewer.authentication.raven;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import uk.ac.cam.lib.spring.security.raven.AuthenticatedRavenTokenCreator;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationException;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationToken;
import uk.ac.cam.ucs.webauth.WebauthException;
import uk.ac.cam.ucs.webauth.WebauthResponse;
import ulcambridge.foundations.viewer.authentication.Obfuscation;

import java.util.function.Function;

/**
 * This is responsible for mapping the CRSIDs received from Raven into the
 * slightly obfuscated format stored in the CUDL database.
 *
 * <p>Raven usernames are stored as {@code raven:<hash>} where {@code <hash>} is
 * the hex-encoded sha256 hash of the crsid.
 */
public class CrsidObfuscatingRavenTokenCreator
    extends RavenTokenCreatorWrapper {

    private static final String USERNAME_FIELD_NAME = "principal";
    private static final int USERNAME_FIELD_INDEX = 6;

    private final Function<String, String> obfuscator;

    public CrsidObfuscatingRavenTokenCreator(
        AuthenticatedRavenTokenCreator target) {
        this(target, s -> Obfuscation.obfuscateUsername("raven", s));
    }

    public CrsidObfuscatingRavenTokenCreator(
        AuthenticatedRavenTokenCreator target,
        Function<String, String> obfuscator) {

        super(target);
        Assert.notNull(obfuscator);
        this.obfuscator = obfuscator;
    }

    /**
     * Get a copy of a {@link WebauthResponse} with the principal
     * (username/crsid) field replaced with the specified value.
     *
     * @param wr The response to clone
     * @param principal The new principal value
     * @throws RavenAuthenticationException If the response doesn't contain a
     *         username, or the response cannot be re-created.
     */
    private static WebauthResponse cloneWithPrincipal(
        WebauthResponse wr, String principal) {

        // The only way to construct a WebauthResponse is via an auth response
        // string, so to change a username we've got to construct a new string
        // containing the new value...

        String[] fields = wr.getRawData().split("!");

        if(fields.length < USERNAME_FIELD_INDEX + 1)
            throw new RavenAuthenticationException(
                "Raven auth response does not contain (required) principal " +
                "field");

        // Replace the principal field with the new value (encoding % and !
        // chars per the waa2wls protocol spec.
        fields[USERNAME_FIELD_INDEX] =
            principal.replace("%", "%25").replace("!", "%21");

        try {
            return new WebauthResponse(String.join("!", fields));
        }
        catch (WebauthException e) {
            // This shouldn't happen as the response has already been validated.
            throw new RavenAuthenticationException(
                "Unable to re-create WebauthResponse object", e);
        }
    }

    protected static String getUsername(WebauthResponse response) {
        return response.get(USERNAME_FIELD_NAME);
    }

    protected String obfuscateUsername(String username) {
        return this.obfuscator.apply(username);
    }

    protected WebauthResponse createObfuscatedResponse(WebauthResponse input) {
        return cloneWithPrincipal(input, obfuscateUsername(getUsername(input)));
    }

    @Override
    public Authentication createAuthenticatedToken(
        RavenAuthenticationToken validatedToken) {


        RavenAuthenticationToken obfuscatedToken = new RavenAuthenticationToken(
            validatedToken.getRavenRequest()
                .orElseThrow(this::noCredentials),
            createObfuscatedResponse(
                validatedToken.getRavenResponse()
                    .orElseThrow(this::noCredentials)),
            validatedToken.getResponseReceivedTime()
                .orElseThrow(this::noCredentials));

        return super.createAuthenticatedToken(obfuscatedToken);
    }

    protected IllegalArgumentException noCredentials() {
        return new IllegalArgumentException(
            "Token's credentials have been cleared");
    }
}
