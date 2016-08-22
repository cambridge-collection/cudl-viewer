package ulcambridge.foundations.viewer.authentication.oauth2;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 *
 */
public class Oauth2AuthenticationFilter<T> extends AbstractAuthenticationProcessingFilter {

    private final OAuth2RestOperations restOperations;
    private final URI profileUrl;
    private final Class<T> profileType;

    public Oauth2AuthenticationFilter(
        AuthenticationManager authenticationManager,
        OAuth2RestOperations restOperations,
        URI profileUrl, Class<T> profileType,
        RequestMatcher requiresAuthenticationRequestMatcher) {

        super(requiresAuthenticationRequestMatcher);

        Assert.notNull(authenticationManager);
        Assert.notNull(restOperations);
        Assert.notNull(profileUrl);
        Assert.notNull(profileType);

        this.restOperations = restOperations;
        this.profileUrl = profileUrl;
        this.profileType = profileType;

        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void setAuthenticationManager(
        AuthenticationManager authenticationManager) {

        throw new RuntimeException("Use constructor argument");
    }

    public OAuth2RestOperations getRestOperations() {
        return this.restOperations;
    }

    public URI getProfileUrl() {
        return this.profileUrl;
    }

    public Class<T> getProfileType() {
        return this.profileType;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        // Note that invoking this for the first time for a user will result in
        // the request failing, leading to the oauth2 library redirecting the
        // user to the appropriate external party to authorise the access
        // (using oauth2). Once they've authorised the access they'll re-attempt
        // the request that lead to this point being reached, and this request
        // will succeed. At that point we'll create an auth token and authorise
        // them using our oauth2 provider.
        T result = getRestOperations()
            .getForObject(getProfileUrl(), getProfileType());

        Authentication token = new Oauth2AuthenticationToken<T>(result);

        return getAuthenticationManager().authenticate(token);
    }

}
