package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.authentication.Urls.UrlCodecStrategy;
import ulcambridge.foundations.viewer.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

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
    private final UrlCodecStrategy urlCodecStrategy;
    private final URI loginFormUrl;
    private final String paramName;

    public UrlQueryParamAuthenticationEntryPoint(
        URI loginFormUrl, RequestCache requestCache) {

        this(loginFormUrl, requestCache, Urls.defaultUrlCodec());
    }

    public UrlQueryParamAuthenticationEntryPoint(
        URI loginFormUrl, RequestCache requestCache,
        UrlCodecStrategy urlCodec) {

        this(loginFormUrl, requestCache, DEFAULT_PARAM_NAME,
            DEFAULT_REDIRECT_STRATEGY, urlCodec);
    }

    public UrlQueryParamAuthenticationEntryPoint(
        URI loginFormUrl, RequestCache requestCache, String paramName,
        RedirectStrategy redirectStrategy, UrlCodecStrategy urlCodec) {

        Assert.notNull(urlCodec);
        Assert.notNull(loginFormUrl);
        Assert.notNull(paramName);
        Assert.notNull(requestCache);
        Assert.notNull(redirectStrategy);

        this.urlCodecStrategy = urlCodec;
        this.loginFormUrl = loginFormUrl;
        this.paramName = paramName;
        this.requestCache = requestCache;
        this.redirectStrategy = redirectStrategy;
    }

    public RequestCache getRequestCache() {
        return this.requestCache;
    }

    public UrlCodecStrategy getUrlCodecStrategy() {
        return this.urlCodecStrategy;
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

        URI nextUrl = Optional.ofNullable(req)
            .map(r -> r.getRedirectUrl())
            .flatMap(Urls::parseUri)
            .orElseGet(() -> Urls.getUrl(currentRequest));

        return this.getUrlCodecStrategy()
            .encodeUrl(Urls.getUrl(currentRequest), nextUrl);
    }

    protected String getRedirectUrl(HttpServletRequest request,
                                    HttpServletResponse response) {

        return Utils.populateScheme(
            UriComponentsBuilder.fromUri(this.getLoginFormUrl()), request)
            .queryParam(this.getParamName(),
                        this.getPostLoginPageUrl(request, response))
            .toUriString();
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

