package ulcambridge.foundations.viewer.authentication;

import com.google.common.collect.Iterators;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;

/**
 * A {@link HttpServletRequestFragmentStorageStrategy} which stores fragments in
 * a HTTP header value.
 */
public class HeaderValueHttpServletRequestFragmentStorageStrategy
    implements HttpServletRequestFragmentStorageStrategy {

    public static final String REQUEST_FRAGMENT_HEADER =
        HeaderValueHttpServletRequestFragmentStorageStrategy.class.getName() +
        "#KEY";

    private final String headerName;

    public HeaderValueHttpServletRequestFragmentStorageStrategy() {
        this(REQUEST_FRAGMENT_HEADER);
    }

    public HeaderValueHttpServletRequestFragmentStorageStrategy(
        String headerName) {

        Assert.hasText(headerName);

        this.headerName = headerName;
    }

    @Override
    public Optional<String> retrieveFragment(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(this.headerName));
    }

    @Override
    public Optional<String> retrieveFragment(SavedRequest request) {
        return request.getHeaderValues(headerName).stream().findFirst();
    }

    @Override
    public HttpServletRequest storeFragment(HttpServletRequest request,
                                            String fragment) {
        Assert.notNull(fragment);

        return new ExtraHeaderHttpServletRequest(
            request, this.headerName, fragment);
    }

    private static class ExtraHeaderHttpServletRequest
        extends HttpServletRequestWrapper {

        private final String headerName;
        private final String[] headerValues;

        public ExtraHeaderHttpServletRequest(
            HttpServletRequest request, String headerName,
            String... headerValues) {

            super(request);
            Assert.notNull(headerName);
            Assert.notNull(headerValues);
            Assert.noNullElements(headerValues);

            if(headerValues.length == 0)
                throw new IllegalArgumentException(
                    "at least one value must be provided");

            this.headerName = headerName;
            this.headerValues = Arrays.copyOf(headerValues,
                                              headerValues.length);
        }

        @Override
        public String getHeader(String name) {
            if(this.headerName.equals(name)) {
                assert this.headerValues.length > 0;
                return this.headerValues[0];
            }

            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            Enumeration<String> headers = super.getHeaders(name);

            return Iterators.asEnumeration(
                Iterators.concat(
                    headers == null ? Collections.emptyIterator() :
                        Iterators.forEnumeration(headers),
                    Iterators.forArray(this.headerValues)));
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            // Don't add the header twice if it already exists
            if(super.getHeader(this.headerName) != null)
                return super.getHeaderNames();

            Enumeration<String> parentHeaders = super.getHeaderNames();

            return Iterators.asEnumeration(
                Iterators.concat(
                    parentHeaders == null ? Collections.emptyIterator() :
                        Iterators.forEnumeration(parentHeaders),
                    Iterators.singletonIterator(headerName)));
        }
    }
}
