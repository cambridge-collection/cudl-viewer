package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.function.Predicate;

/**
 * A {@link LogoutSuccessHandler} which redirects to a URL specified in a query
 * parameter of the logout URL.
 */
public class RedirectingLogoutSuccessHandler implements LogoutSuccessHandler {

    public static final String DEFAULT_PARAM_NAME = "next";
    public static final RedirectStrategy DEFAULT_REDIRECT_STRATEGY =
        new DefaultRedirectStrategy();

    private final RedirectStrategy redirectStrategy;
    private final String paramName;
    private final Predicate<URI> isValidRedirectUrl;
    private final URI defaultRedirectUrl;

    public RedirectingLogoutSuccessHandler(URI defaultRedirectUrl) {
        this(EntryPointRequestFilters.isSameSite(defaultRedirectUrl, false),
             defaultRedirectUrl);
    }

    public RedirectingLogoutSuccessHandler(
        Predicate<URI> isValidRedirectUrl, URI defaultRedirectUrl) {
        this(isValidRedirectUrl, DEFAULT_PARAM_NAME,
             defaultRedirectUrl, DEFAULT_REDIRECT_STRATEGY);
    }

    public RedirectingLogoutSuccessHandler(
        Predicate<URI> isValidRedirectUrl, String paramName, URI defaultRedirectUrl,
        RedirectStrategy redirectStrategy) {

        Assert.notNull(redirectStrategy);
        Assert.notNull(isValidRedirectUrl);
        Assert.notNull(paramName);
        Assert.notNull(defaultRedirectUrl);

        this.redirectStrategy = redirectStrategy;
        this.paramName = paramName;
        this.isValidRedirectUrl = isValidRedirectUrl;
        this.defaultRedirectUrl = defaultRedirectUrl;
    }

    public RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

    public String getParamName() {
        return this.paramName;
    }

    public Predicate<URI> getIsValidRedirectUrl() {
        return this.isValidRedirectUrl;
    }

    public URI getDefaultRedirectUrl() {
        return this.defaultRedirectUrl;
    }

    public String getDefaultRedirectUrlString() {
        return this.getDefaultRedirectUrl().toString();
    }

    public String getRedirectUrl(HttpServletRequest req) {
        return EntryPointRequestFilters.getUrlFromQueryParam(
            req, this.getParamName())
            .filter(this.getIsValidRedirectUrl())
            .map(Object::toString)
            .orElseGet(this::getDefaultRedirectUrlString);
    }

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        this.getRedirectStrategy().sendRedirect(
            request, response, this.getRedirectUrl(request));
    }
}
