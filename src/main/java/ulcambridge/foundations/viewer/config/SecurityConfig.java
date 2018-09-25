package ulcambridge.foundations.viewer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.authentication.ExtendedBaseOAuth2ProtectedResourceDetails;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class SecurityConfig {

    @Bean(name = "google")
    public static OAuth2ProtectedResourceDetails oauth2Google(
        @Value("${google.clientId}") String clientId,
        @Value("${google.clientSecret}") String clientSecret) {

        AuthorizationCodeResourceDetails details =
            new ExtendedBaseOAuth2ProtectedResourceDetails();

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri("https://accounts.google.com/o/oauth2/token");

        details.setUserAuthorizationUri(
            UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .toUriString());

        details.setScope(Arrays.asList("profile", "openid", "email"));
        details.setClientAuthenticationScheme(AuthenticationScheme.form);

        return details;
    }

    @Bean(name = "facebook")
    public static OAuth2ProtectedResourceDetails oauth2Facebook(
        @Value("${facebook.appId}") String clientId,
        @Value("${facebook.appSecret}") String clientSecret) {

        AuthorizationCodeResourceDetails details =
            new ExtendedBaseOAuth2ProtectedResourceDetails();

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(
            "https://graph.facebook.com/oauth/access_token");
        details.setUserAuthorizationUri(
            "https://www.facebook.com/dialog/oauth");
        details.setTokenName("oauth_token");
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setScope(Arrays.asList("email"));
        details.setClientAuthenticationScheme(AuthenticationScheme.form);

        return details;
    }

    @Bean(name = "linkedin")
    public static OAuth2ProtectedResourceDetails oauth2Linkedin(
        @Value("${linkedin.clientId}") String clientId,
        @Value("${linkedin.clientSecret}") String clientSecret) {

        AuthorizationCodeResourceDetails details =
            new ExtendedBaseOAuth2ProtectedResourceDetails();

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(
            "https://www.linkedin.com/uas/oauth2/accessToken");
        details.setUserAuthorizationUri(
            "https://www.linkedin.com/uas/oauth2/authorization");
        details.setScope(Arrays.asList("r_basicprofile", "r_emailaddress"));
        details.setClientAuthenticationScheme(AuthenticationScheme.form);

        return details;
    }

    @Bean(name = "googleOauth")
    public static OAuth2RestTemplate googleOauth2Rest(
        @Qualifier("google") OAuth2ProtectedResourceDetails details,
        OAuth2ClientContext context) {

        return new OAuth2RestTemplate(details, context);
    }

    @Bean(name = "facebookOauth")
    public static OAuth2RestTemplate facebookOauth2Rest(
        @Qualifier("facebook") OAuth2ProtectedResourceDetails details,
        OAuth2ClientContext context) {

        return new OAuth2RestTemplate(details, context);
    }

    @Bean(name = "linkedinOauth")
    public static OAuth2RestTemplate linkedinOauth2Rest(
        @Qualifier("linkedin") OAuth2ProtectedResourceDetails details,
        OAuth2ClientContext context) {

        return new OAuth2RestTemplate(details, context);
    }
}
