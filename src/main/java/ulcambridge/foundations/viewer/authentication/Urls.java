package ulcambridge.foundations.viewer.authentication;

import com.google.common.collect.ImmutableSet;
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

    static class RelativisingUrlCodecStrategy implements UrlCodecStrategy {

        private final URI baseUrl;

        public RelativisingUrlCodecStrategy(URI baseUrl) {

            Assert.notNull(baseUrl);
            this.baseUrl = baseUrl;
        }

        private URI getBaseUrl(URI requestUri) {
            return requestUri.resolve(this.baseUrl);
        }

        @Override
        public String encodeUrl(URI requestUri, URI url) {
            return getBaseUrl(requestUri).relativize(url).toString();
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
