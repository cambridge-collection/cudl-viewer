package ulcambridge.foundations.viewer.authentication;


import com.google.common.collect.ImmutableSet;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import ulcambridge.foundations.viewer.authentication.RequestFilterEntryPointWrapper.RequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Static methods to create {@link RequestFilter}s to be applied to
 * {@link AuthenticationEntryPoint}s using
 * {@link RequestFilterEntryPointWrapper}.
 */
public class EntryPointRequestFilters {

    /**
     * @see #saveRequestFromQueryParamUrl(RequestCache, String, URI, Predicate)
     */
    public static RequestFilter saveRequestFromQueryParamUrl(
        RequestCache requestCache, URI defaultUrl, Predicate<URI> isSafePredicate) {

        return saveRequestFromQueryParamUrl(
            requestCache,
            UrlQueryParamAuthenticationEntryPoint.DEFAULT_PARAM_NAME,
            defaultUrl, isSafePredicate);
    }

    /**
     * Use an URL in a query param as the destination after successful
     * authentication. This is achieved by saving the request in a
     * {@link RequestCache} for later use by the
     * {@link SavedRequestAwareAuthenticationSuccessHandler}.
     *
     * @param requestCache The object to save the request with
     * @param queryParamName The name of the query param which contains the
     *                       destination URL.
     * @param defaultUrl The default URL to use if none is specified, or the
     *                   provided URL is not safe.
     * @param isSafePredicate A predicate which decides if a provided URL is
     *                        safe to redirect to (see defaultUrl).
     */
    public static RequestFilter saveRequestFromQueryParamUrl(
        RequestCache requestCache, String queryParamName, URI defaultUrl,
        Predicate<URI> isSafePredicate) {

        return new SaveRequestFromQueryParamUrlRequestFilter(
            requestCache, isSafePredicate, queryParamName, defaultUrl);
    }

    /** null-safe .equals() check. */
    private static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private static final Set<String> HTTP_SCHEMES = ImmutableSet.of(
        "http", "https");

    /**
     * Create a Predicate which returns true for URLs which on the same
     * domain/port the sample URL (optionally the same scheme too).
     *
     * @param sample The URL defining the website that should be matched by URLs
     *               being tested by the returned predicate.
     * @param ignoreHttpsHttpChanges Allow the URLs to differ between https and
     *                               http as long as they're both one or the
     *                               other.
     */
    public static Predicate<URI> isSameSite(URI sample,
                                            boolean ignoreHttpsHttpChanges) {
        return url -> {
            boolean bothHttp = ignoreHttpsHttpChanges &&
                HTTP_SCHEMES.contains(sample.getScheme()) &&
                HTTP_SCHEMES.contains(url.getScheme());

            return (bothHttp || equals(sample.getScheme(), url.getScheme())) &&
                   equals(sample.getAuthority(), url.getAuthority());
        };
    }

    static class SaveRequestFromQueryParamUrlRequestFilter
        implements RequestFilter {

        private final RequestCache requestCache;
        private final String queryParam;
        private final URI defaultUrl;
        private Predicate<URI> isValidRedirectUrl;

        public SaveRequestFromQueryParamUrlRequestFilter(
            RequestCache requestCache, Predicate<URI> isValidRedirectUrl,
            String queryParam, URI defaultUrl) {

            Assert.notNull(requestCache);
            Assert.notNull(queryParam);
            Assert.notNull(defaultUrl);
            Assert.notNull(isValidRedirectUrl);

            this.requestCache = requestCache;
            this.queryParam = queryParam;
            this.defaultUrl = defaultUrl;
            this.isValidRedirectUrl = isValidRedirectUrl;
        }

        public Predicate<URI> getIsValidRequestUrl() {
            return this.isValidRedirectUrl;
        }

        public RequestCache getRequestCache() {
            return this.requestCache;
        }

        public String getQueryParam() {
            return this.queryParam;
        }

        public URI getDefaultUrl() {
            return this.defaultUrl;
        }

        protected URI getUrlToSave(Optional<URI> providedUrl) {
            return providedUrl.filter(this.getIsValidRequestUrl())
                .orElseGet(this::getDefaultUrl);
        }

        private static String decodeUtf8(String s) {
            try {
                return UriUtils.decode(s, "UTF-8");
            }
            catch (IllegalArgumentException e) {
                return null;
            }
            catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        protected Optional<URI> getSuggestedRedirectUrl(
            HttpServletRequest req) {

            return Optional.ofNullable(
                UriComponentsBuilder
                    .fromUriString("?" + req.getQueryString())
                    .build()
                    .getQueryParams()
                    .getFirst(this.getQueryParam()))
                .map(SaveRequestFromQueryParamUrlRequestFilter::decodeUtf8)
                .map(url -> {
                    try {
                        return new URI(url);
                    }
                    catch(URISyntaxException e) {
                        return null;
                    }
                });
        }

        @Override
        public HttpServletRequest filter(
            HttpServletRequest req, HttpServletResponse resp) {

            URI toSave = this.getUrlToSave(this.getSuggestedRedirectUrl(req));

            saveRequest(toSave, req, resp, getRequestCache());

            return req;
        }
    }

    /**
     * Save a request for the specified uri and method.
     *
     * <p>The current request and response are required to save the request,
     * but are not saved themselves.
     *
     * @param uri
     * @param method
     * @param currentReq
     * @param currentResp
     * @param requestCache
     */
    public static void saveRequest(
        URI url, String servletPath, HttpMethod method,
        HttpServletRequest currentReq, HttpServletResponse currentResp,
        RequestCache requestCache) {

        HttpServletRequest toSave = new UrlChangingHttpServletRequestWrapper(
            currentReq, method, url, servletPath);

        requestCache.saveRequest(toSave, currentResp);
    }

    /**
     * As {@link #saveRequest(URI, String, HttpMethod, HttpServletRequest, HttpServletResponse, RequestCache)}
     * with "" and GET as defaults for servletPath and method.
     */
    public static void saveRequest(URI url, HttpServletRequest currentReq, HttpServletResponse currentResp, RequestCache requestCache) {
        saveRequest(url, "", HttpMethod.GET,
                    currentReq, currentResp, requestCache);
    }

    static class UrlChangingHttpServletRequestWrapper
        extends HttpServletRequestWrapper {

        private String requestUri;
        private String requestUrl;

        private final URI fullUrl;
        private final String servletPath;
        private final HttpMethod method;

        public UrlChangingHttpServletRequestWrapper(
            HttpServletRequest wrapped, HttpMethod method,
            URI fullUrl,String servletPath) {

            super(wrapped);

            Assert.notNull(method);
            Assert.notNull(fullUrl);
            Assert.notNull(servletPath);

            // Note that this is not strictly correct, as variations in encoding
            // between fullUrl and servletPath would make the comparison fail.
            // e.g. unnecessarily encoding characters in one but not the other.
            if(!fullUrl.getRawPath().startsWith(servletPath)) {
                throw new IllegalArgumentException(
                    "servletPath is not a prefix of the fullUrl's path");
            }

            this.method = method;
            this.servletPath = servletPath;
            this.fullUrl = fullUrl;
        }

        @Override
        public String getMethod() {
            return method.toString();
        }

        @Override
        public int getServerPort() {
            return this.fullUrl.getPort();
        }

        @Override
        public String getQueryString() {
            return this.fullUrl.getRawQuery();
        }

        @Override
        public String getRequestURI() {
            return this.fullUrl.getRawPath();
        }

        @Override
        public StringBuffer getRequestURL() {
            return new StringBuffer(UriComponentsBuilder.fromUri(this.fullUrl)
                .replaceQuery(null).build().toUriString());}

        @Override public String getServletPath() {
            return this.servletPath;
        }

        @Override public String getPathInfo() {
            return this.fullUrl.getRawPath()
                .substring(this.servletPath.length());
        }
    }

    private EntryPointRequestFilters() {
        throw new RuntimeException();
    }
}
