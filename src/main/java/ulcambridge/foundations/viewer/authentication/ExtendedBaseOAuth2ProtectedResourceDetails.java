package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ExtendedBaseOAuth2ProtectedResourceDetails extends
    AuthorizationCodeResourceDetails {

public boolean isClientOnly() {
    return true;
}
}