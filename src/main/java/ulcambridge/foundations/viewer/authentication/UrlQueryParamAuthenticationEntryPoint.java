package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * An {@link AuthenticationEntryPoint} which redirects to a fixed URL with a
 * query param containing the URL of the page to redirect to after
 * authentication has completed.
 */
public class UrlQueryParamAuthenticationEntryPoint
    implements AuthenticationEntryPoint {

    public static final String DEFAULT_PARAM_NAME = "next";
    public static final RedirectStrategy DEFAULT_REDIRECT_STRATEGY =
        new DefaultRedirectStrategy();

    private final RequestCache requestCache;
    private final RedirectStrategy redirectStrategy;
    private final URI loginFormUrl;
    private final String paramName;

    public UrlQueryParamAuthenticationEntryPoint(URI loginFormUrl, RequestCache requestCache) {
        this(loginFormUrl, requestCache,
             DEFAULT_PARAM_NAME, DEFAULT_REDIRECT_STRATEGY);
    }

    public UrlQueryParamAuthenticationEntryPoint(
        URI loginFormUrl, RequestCache requestCache, String paramName,
        RedirectStrategy redirectStrategy) {

        Assert.notNull(loginFormUrl);
        Assert.notNull(paramName);
        Assert.notNull(requestCache);
        Assert.notNull(redirectStrategy);

        this.loginFormUrl = loginFormUrl;
        this.paramName = paramName;
        this.requestCache = requestCache;
        this.redirectStrategy = redirectStrategy;
    }

    public RequestCache getRequestCache() {
        return this.requestCache;
    }

    public URI getLoginFormUrl() {
        return this.loginFormUrl;
    }

    public String getParamName() {
        return this.paramName;
    }

    private String getPostLoginPageUrl(
        HttpServletRequest currentRequest,
        HttpServletResponse currentResponse) {

        SavedRequest req = this.getRequestCache().getRequest(
            currentRequest, currentResponse);

        return req != null ? req.getRedirectUrl()
                           : currentRequest.getRequestURL().toString();
    }

    protected String getRedirectUrl(HttpServletRequest request,
                                    HttpServletResponse response) {
        return UriComponentsBuilder.fromUri(this.getLoginFormUrl())
            .queryParam(this.getParamName(),
                        this.getPostLoginPageUrl(request, response))
            .build().toUriString();
    }

    @Override
    public void commence(
        HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException)
        throws IOException, ServletException {

        redirectStrategy.sendRedirect(
            request, response, this.getRedirectUrl(request, response));

    }
}
