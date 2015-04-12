package ulcambridge.foundations.viewer.authentication;

//import com.google.common.io.Files;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * Custom access token store which stores access tokens in a JSON file. The most important restriction is that only
 * one user is supported by this implementation. For multiple users a database implementation such as
 * JdbcClientTokenServices may be considered.
 */
public class JsonClientTokenServices implements ClientTokenServices {
    private static final Logger LOG = LoggerFactory.getLogger(JsonClientTokenServices.class);

    //represents the remaining valid time in seconds of an expired token
    private static final int TOKEN_ALREADY_EXPIRED = -1;
    // we subtract a safety time gap of 10 seconds to ensure that returned tokens are valid when we return them
    private static final int SAFETY_TIME_GAP_SECONDS = 10;

    private static final int FACTOR_MILLIS_TO_SECONDS = 1000;

    private final ObjectMapper mapper = new ObjectMapper();

    //${google.calendar.user.credentialStore.path}
    @Value("REPLACE_ME")
    private File tokenFile;

    @SuppressWarnings("unused")
    private JsonClientTokenServices() {
        //default constructor for Spring
    }

    public JsonClientTokenServices(File outputFile) {
        this.tokenFile = outputFile;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        if (!tokenFile.exists()) {
            return null;
        }
        try {
            DefaultOAuth2AccessToken rawAccessToken = mapper.readValue(tokenFile, DefaultOAuth2AccessToken.class);
            adjustExpiresIn(rawAccessToken);
            return rawAccessToken;
        } catch (IOException ex) {
            LOG.warn("Could not read token file '" + tokenFile + "'.", ex);
            return null;
        }
    }

    private void adjustExpiresIn(DefaultOAuth2AccessToken rawAccessToken) {
        // The expires_in attribute is only suitable for usage of in-memory tokens. As it is a time stamp relative to
        // the point in time the access token has been issued it should obviously change. This is apparently
        // impossible if it is persisted...
        int originalExpiresIn = rawAccessToken.getExpiresIn();
        int adjustedExpiresIn = getAdjustedExpiry(originalExpiresIn);
        LOG.debug("Adjusting expires_in from {} to {}.", originalExpiresIn, adjustedExpiresIn);
        rawAccessToken.setExpiration(new Date(System.currentTimeMillis() + adjustedExpiresIn * FACTOR_MILLIS_TO_SECONDS));
    }

    private int getAdjustedExpiry(int originalExpiry) {
        try {
            BasicFileAttributes basicFileAttributes = java.nio.file.Files.readAttributes(tokenFile.toPath(), BasicFileAttributes.class);
            long creationTimeStampMillis = basicFileAttributes.creationTime().toMillis();
            long nowMillis = new Date().getTime();
            long relativeTimeSinceCreationInSeconds = (nowMillis - creationTimeStampMillis) / FACTOR_MILLIS_TO_SECONDS;
            //ensure the int cast succeeds
            if (relativeTimeSinceCreationInSeconds < Integer.MAX_VALUE) {
                //consider a safety time gap
                return originalExpiry - ((int) relativeTimeSinceCreationInSeconds) - SAFETY_TIME_GAP_SECONDS;
            } else {
                return TOKEN_ALREADY_EXPIRED;
            }

        } catch (IOException ex) {
            LOG.warn("Could not adjust expiry date. Considering the token to be expired.", ex);
            return TOKEN_ALREADY_EXPIRED;
        }
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication, OAuth2AccessToken accessToken) {
       // try {
           // Files.createParentDirs(tokenFile);
       //     mapper.writeValue(tokenFile, accessToken);
       // } catch (IOException e) {
        //    LOG.warn("Could not save access token", e);
        //}
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        if (!tokenFile.delete()) {
            //it's the best we can do...
            LOG.info("Could not delete token file '" + tokenFile + ". Scheduling file removal at termination of JVM.");
            tokenFile.deleteOnExit();
        }
    }
}
