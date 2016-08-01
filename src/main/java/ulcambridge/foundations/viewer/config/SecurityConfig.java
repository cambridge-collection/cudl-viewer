package ulcambridge.foundations.viewer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.authentication.ExtendedBaseOAuth2ProtectedResourceDetails;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableOAuth2Client
@EnableGlobalMethodSecurity
public class SecurityConfig {

    @EnableWebSecurity
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final DataSource dataSource;

        @Autowired
        public WebSecurityConfig(DataSource dataSource) {
            Assert.notNull(dataSource);
            this.dataSource = dataSource;
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                .dataSource(this.dataSource);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .loginPage("/auth/login")
                    .and()
                .logout()
                    .logoutUrl("/auth/logout")
                    // FIXME: This
                    .logoutSuccessUrl("/auth/login?error=Successfully%20logged%20out &amp;access=/mylibrary/")
                    .deleteCookies("JSESSIONID")
                    .and()
                .authorizeRequests()
                    .antMatchers("/admin/**", "/editor/**").hasRole("ADMIN")
                    .antMatchers("/mylibrary/**").hasRole("USER")
                    .anyRequest().hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                    .and()
                .headers()
                    .addHeaderWriter(
                        new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
        }
    }

    @Bean(name = "google")
    public static OAuth2ProtectedResourceDetails oauth2Google(
        @Value("${google.clientId}") String clientId,
        @Value("${google.clientSecret}") String clientSecret,
        @Value("${google.openid.realm}") String realm) {

        AuthorizationCodeResourceDetails details =
            new ExtendedBaseOAuth2ProtectedResourceDetails();

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri("https://accounts.google.com/o/oauth2/token");

        details.setUserAuthorizationUri(
            UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("openid.realm", realm)
                .build()
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
