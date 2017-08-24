package ulcambridge.foundations.viewer.authentication;


import org.springframework.http.HttpMethod;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.authentication.RequestFilterEntryPointWrapper.RequestFilter;
import ulcambridge.foundations.viewer.authentication.Urls.UrlCodecStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.net.URI;
import java.util.Optional;

/**
 * Static methods to create {@link RequestFilter}s to be applied to
 * {@link AuthenticationEntryPoint}s using
 * {@link RequestFilterEntryPointWrapper}.
 */
public class EntryPointRequestFilters {

    /**
     * @see #saveRequestFromQueryParamUrl(RequestCache, String, URI, UrlCodecStrategy, Optional)
     */
    public static RequestFilter saveRequestFromQueryParamUrl(
        RequestCache requestCache, URI defaultUrl, UrlCodecStrategy urlCodec) {

        return saveRequestFromQueryParamUrl(
            requestCache,
            UrlQueryParamAuthenticationEntryPoint.DEFAULT_PARAM_NAME,
            defaultUrl, urlCodec, Optional.empty());
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
     * @param urlCodec The strategy to use to decode URLs from query parameters.
     */
    public static RequestFilter saveRequestFromQueryParamUrl(
        RequestCache requestCache, String queryParamName, URI defaultUrl,
        UrlCodecStrategy urlCodec,
        Optional<HttpServletRequestFragmentStorageStrategy>
            fragmentStorageStrategy) {

        return new SaveRequestFromQueryParamUrlRequestFilter(
            requestCache, urlCodec, queryParamName, defaultUrl,
            fragmentStorageStrategy);
    }

    static class SaveRequestFromQueryParamUrlRequestFilter
        implements RequestFilter {

        private final RequestCache requestCache;
        private final String queryParam;
        private final URI defaultUrl;
        private final UrlCodecStrategy urlCodec;
        private final Optional<HttpServletRequestFragmentStorageStrategy>
            fragmentStorageStrategy;

        public SaveRequestFromQueryParamUrlRequestFilter(
            RequestCache requestCache, UrlCodecStrategy urlCodec,
            String queryParam, URI defaultUrl,
            Optional<HttpServletRequestFragmentStorageStrategy>
                fragmentStorageStrategy) {

            Assert.notNull(requestCache);
            Assert.notNull(queryParam);
            Assert.notNull(defaultUrl);
            Assert.notNull(urlCodec);
            Assert.notNull(fragmentStorageStrategy);

            this.requestCache = requestCache;
            this.queryParam = queryParam;
            this.defaultUrl = defaultUrl;
            this.urlCodec = urlCodec;
            this.fragmentStorageStrategy = fragmentStorageStrategy;
        }

        public UrlCodecStrategy getUrlCodecStrategy() {
            return this.urlCodec;
        }

        public Optional<HttpServletRequestFragmentStorageStrategy>
        getFragmentStorageStrategy() {

            return this.fragmentStorageStrategy;
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

        protected URI getUrlToSave(HttpServletRequest req) {
            return Urls
                .getQueryParam(req, this.getQueryParam())
                .flatMap(url ->
                    this.getUrlCodecStrategy().decodeUrl(Urls.getUrl(req), url))
                .orElseGet(this::getDefaultUrl);
        }

        @Override
        public HttpServletRequest filter(
            HttpServletRequest req, HttpServletResponse resp) {

            saveRequest(getUrlToSave(req), "", HttpMethod.GET, req, resp,
                        getRequestCache(), getFragmentStorageStrategy());

            return req;
        }
    }

    /**
     * Save a request for the specified uri and method.
     *
     * <p>The current request and response are required to save the request,
     * but are not saved themselves.
     */
    public static void saveRequest(
        URI url, String servletPath, HttpMethod method,
        HttpServletRequest currentReq, HttpServletResponse currentResp,
        RequestCache requestCache,
        Optional<HttpServletRequestFragmentStorageStrategy>
            fragmentStorageStrategy) {

        HttpServletRequest toSave = new UrlChangingHttpServletRequestWrapper(
            currentReq, method, url, servletPath);

        if(url.getRawFragment() != null &&
            fragmentStorageStrategy.isPresent()) {

            toSave = fragmentStorageStrategy.get()
                .storeFragment(toSave, url.getFragment());
        }

        requestCache.saveRequest(toSave, currentResp);
    }

    /**
     * As {@link #saveRequest(URI, String, HttpMethod, HttpServletRequest, HttpServletResponse, RequestCache, Optional)}
     * with "" and GET as defaults for servletPath and method.
     */
    public static void saveRequest(
        URI url, HttpServletRequest currentReq, HttpServletResponse currentResp,
        RequestCache requestCache) {

        saveRequest(url, "", HttpMethod.GET,
                    currentReq, currentResp, requestCache, Optional.empty());
    }

    static class UrlChangingHttpServletRequestWrapper
        extends HttpServletRequestWrapper {

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
            int port = this.fullUrl.getPort();

            // URIs represent the implied default port as -1 rather than the
            // actual default value, so we have to provide that ourselves.
            if(port == -1) {
                String scheme = this.getScheme();
                if("http".equals(scheme))
                    return 80;
                else if("https".equals(scheme))
                    return 443;
            }

            return port;
        }

        @Override
        public String getScheme() {
            String scheme = this.fullUrl.getScheme();
            // If the full URL is not absolute ( //foo.com/blah ) the scheme
            // will be drawn from the actual request.
            return scheme == null ? super.getScheme() : scheme;
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
                .replaceQuery(null).toUriString());}

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
