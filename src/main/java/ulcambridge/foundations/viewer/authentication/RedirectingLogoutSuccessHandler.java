package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.authentication.Urls.UrlCodecStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

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
    private final UrlCodecStrategy urlCodec;
    private final URI defaultRedirectUrl;

    public RedirectingLogoutSuccessHandler(URI defaultRedirectUrl) {
        this(Urls.sameSiteCodecStrategy(
                Urls.defaultUrlCodec(), defaultRedirectUrl, false),
             defaultRedirectUrl);
    }

    public RedirectingLogoutSuccessHandler(
        UrlCodecStrategy urlCodec, URI defaultRedirectUrl) {
        this(urlCodec, DEFAULT_PARAM_NAME,
             defaultRedirectUrl, DEFAULT_REDIRECT_STRATEGY);
    }

    public RedirectingLogoutSuccessHandler(
        UrlCodecStrategy urlCodec, String paramName, URI defaultRedirectUrl,
        RedirectStrategy redirectStrategy) {

        Assert.notNull(redirectStrategy);
        Assert.notNull(urlCodec);
        Assert.notNull(paramName);
        Assert.notNull(defaultRedirectUrl);

        this.redirectStrategy = redirectStrategy;
        this.paramName = paramName;
        this.urlCodec = urlCodec;
        this.defaultRedirectUrl = defaultRedirectUrl;
    }

    public RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

    public String getParamName() {
        return this.paramName;
    }

    public UrlCodecStrategy getUrlCodec() {
        return this.urlCodec;
    }

    public URI getDefaultRedirectUrl() {
        return this.defaultRedirectUrl;
    }

    public String getDefaultRedirectUrlString(HttpServletRequest req) {
        URI url = this.getDefaultRedirectUrl();

        if(!url.isAbsolute()) {
            url = Urls.getUrl(req).resolve(url);
        }

        return url.toASCIIString();
    }

    public String getRedirectUrl(HttpServletRequest req) {
        return Urls.getQueryParam(req, this.getParamName())
            .flatMap(value -> this.getUrlCodec()
                .decodeUrl(Urls.getUrl(req), value))
            .map(Object::toString)
            .orElseGet(() -> getDefaultRedirectUrlString(req));
    }

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        this.getRedirectStrategy().sendRedirect(
            request, response, this.getRedirectUrl(request));
    }
}
