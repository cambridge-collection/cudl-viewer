package ulcambridge.foundations.viewer.authentication;

import com.github.jsonldjava.utils.Obj;
import com.google.common.collect.ImmutableSet;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class Urls {

    private static final Set<String> HTTP_SCHEMES = ImmutableSet.of(
        "http", "https");

    public static UrlCodecStrategy defaultUrlCodec() {
        return new DefaultUrlCodecStrategy();
    }

    public static UrlCodecStrategy relativisingUrlCodec(URI baseUrl) {
        return new RelativisingUrlCodecStrategy(baseUrl);
    }

    public static UrlCodecStrategy filteringUrlCodecStrategy(UrlCodecStrategy target, Predicate<URI> filter) {
        return new UriPredicateFilteringUrlCodecStrategy(target, filter);
    }

    public static UrlCodecStrategy sameSiteCodecStrategy(
        UrlCodecStrategy target, URI siteUrl, boolean ignoreHttpsHttpChanges) {

        return filteringUrlCodecStrategy(target,
            isSameSite(siteUrl, ignoreHttpsHttpChanges));
    }

    /**
     * Get the URL of an HTTP request as a URI object.
     */
    static URI getUrl(HttpServletRequest req) {
        int port = req.getServerPort();
        if(port == 80 || port == 443)
            port = -1;

        return UriComponentsBuilder.newInstance()
            .scheme(req.getScheme())
            .host(req.getServerName())
            .port(port)
            .path(req.getRequestURI())
            .query(req.getQueryString())
            .build(true) // Path and query are not decoded
            .toUri();
    }

    /** null-safe .equals() check. */
    private static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Create a Predicate which returns true for URLs which use the same
     * domain/port as the sample URL (optionally the same scheme too).
     *
     * <p>If the sample URL is protocol-relative (has no scheme, e.g.
     * {@code //foo.com/}) then the predicate will match URLs with http/https
     * schemes regardless of the value of {@code ignoreHttpsHttpChanges}.
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
            boolean sampleProtocolRelative = sample.getScheme() == null;

            boolean bothHttp = ignoreHttpsHttpChanges &&
                HTTP_SCHEMES.contains(sample.getScheme()) &&
                HTTP_SCHEMES.contains(url.getScheme());

            boolean protocolsMatch = (
                sampleProtocolRelative || bothHttp ||
                equals(sample.getScheme(), url.getScheme()));

            return protocolsMatch &&
                   equals(sample.getAuthority(), url.getAuthority());
        };
    }

    private static String urlDecodeUtf8(String s) {
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

    public static Optional<String> getQueryParam(
        HttpServletRequest req, String param) {

        return Optional.ofNullable(req.getQueryString())
            .flatMap(query -> getQueryParam(query, param));
    }

    public static Optional<String> getQueryParam(
        String query, String param) {

        Assert.notNull(query);

        String strippedQuery = query.charAt(0) == '?' ? query.substring(1)
                                                      : query;

        return Optional.ofNullable(UriComponentsBuilder.newInstance()
            .query(strippedQuery)
            .build().getQueryParams()
            .getFirst(param))
            .map(Urls::urlDecodeUtf8);
    }

    public static Optional<URI> parseUri(String uri) {
        try {
            return Optional.of(new URI(uri));
        }
        catch(URISyntaxException e) {
            return Optional.empty();
        }
    }

    public interface UrlCodecStrategy {
        String encodeUrl(URI requestUri, URI url);
        Optional<URI> decodeUrl(URI requestUri, String queryParam);
    }

    static class DefaultUrlCodecStrategy implements UrlCodecStrategy {
        @Override
        public String encodeUrl(URI requestUri, URI url) {
            return url.toString();
        }

        @Override
        public Optional<URI> decodeUrl(URI requestUri, String queryParam) {
            return parseUri(queryParam);
        }
    }

    static abstract class DelegatingUrlCodecStrategy
        implements UrlCodecStrategy {

        private final UrlCodecStrategy target;

        public DelegatingUrlCodecStrategy(UrlCodecStrategy target) {
            Assert.notNull(target);

            this.target = target;
        }

        @Override
        public String encodeUrl(URI requestUri, URI url) {
            return this.target.encodeUrl(requestUri, url);
        }

        @Override
        public Optional<URI> decodeUrl(URI requestUri, String queryParam) {
            return this.target.decodeUrl(requestUri, queryParam);
        }
    }

    static class UriPredicateFilteringUrlCodecStrategy
        extends DelegatingUrlCodecStrategy {

        private final Predicate<URI> predicate;

        public UriPredicateFilteringUrlCodecStrategy(
            UrlCodecStrategy target, Predicate<URI> predicate) {

            super(target);
            Assert.notNull(predicate);

            this.predicate = predicate;
        }

        @Override
        public Optional<URI> decodeUrl(URI requestUri, String queryParam) {
            return super.decodeUrl(requestUri, queryParam)
                .filter(this.predicate);
        }
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    static class RelativisingUrlCodecStrategy implements UrlCodecStrategy {

        private final URI baseUrl;

        public RelativisingUrlCodecStrategy(URI baseUrl) {

            Assert.notNull(baseUrl);
            this.baseUrl = baseUrl;
        }

        private URI getBaseUrl(URI requestUri) {
            URI baseUrl = requestUri.resolve(this.baseUrl);

            // URI seems to incorrectly handle resolving a relative path against
            // a URI with an empty path - resolving 'foo' against
            // 'http://bar.com' results in http://bar.comfoo rather than
            // http://bar.com/foo
            if(isNullOrEmpty(baseUrl.getRawPath()))
                return baseUrl.resolve("/");
            return baseUrl;
        }

        @Override
        public String encodeUrl(URI requestUri, URI url) {
            URI base = getBaseUrl(requestUri)
                // Strip off trailing path segments after the last slash
                // i.e. /foo/bar -> /foo/
                // This is required as relativize doesn't handle these trailing
                // sections correctly, and they're automatically stripped anyway
                // when resolving.
                .resolve("./");

            // The path will always be at least "/"
            assert base.isAbsolute() && !isNullOrEmpty(base.getRawPath());

            URI relative = base.relativize(url);

            // If the URL starts with a path, make the paths more obviously
            // paths by having them start with / or ./
            if(relative.getScheme() == null &&
               relative.getRawAuthority() == null &&
               !isNullOrEmpty(relative.getRawPath())) {

                String prefix = base.getRawPath().equals("/") ? "/" : "./";

                return prefix + relative.toString();
            }

            return relative.toString();
        }

        @Override
        public Optional<URI> decodeUrl(URI requestUri, String queryParam) {
            return parseUri(queryParam)
                .map(url -> getBaseUrl(requestUri).resolve(url));
        }
    }

    private Urls() {
        throw new RuntimeException();
    }
}
