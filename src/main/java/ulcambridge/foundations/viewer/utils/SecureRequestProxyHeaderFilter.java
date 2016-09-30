package ulcambridge.foundations.viewer.utils;

import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * A {@link Filter} which marks {@link HttpServletRequest}
 */
public class SecureRequestProxyHeaderFilter extends GenericFilterBean {

    public static final String DEFAULT_SECURE_REQUEST_HEADER = "x-forwarded-proto";

    static Predicate<HttpServletRequest> requestHasSecureHeader(
        String headerName, String expectedValue) {

        Assert.hasText(headerName);
        Assert.notNull(expectedValue);

        return r -> expectedValue.equals(r.getHeader(headerName));
    }

    private final Predicate<HttpServletRequest> isSecurePredicate;

    public SecureRequestProxyHeaderFilter() {
        this(DEFAULT_SECURE_REQUEST_HEADER);
    }

    public SecureRequestProxyHeaderFilter(String header) {
        this(header, "https");
    }

    public SecureRequestProxyHeaderFilter(String header, String value) {
        this(requestHasSecureHeader(header, value));
    }

    public SecureRequestProxyHeaderFilter(
        Predicate<HttpServletRequest> isSecure) {

        Assert.notNull(isSecure);
        this.isSecurePredicate = isSecure;
    }

    @Override
    public void doFilter(
        ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if(!(request instanceof HttpServletRequest))
            throw new RuntimeException("request was not an HttpServletRequest");

        HttpServletRequest req = (HttpServletRequest)request;

        if(!request.isSecure() && this.isSecurePredicate.test(req)) {
            req = new SecureHttpServletRequestWrapper(req);
        }

        chain.doFilter(req, response);
    }

    /**
     * A {@link HttpServletRequestWrapper} which always reports that the wrapped
     * request is secure.
     */
    static class SecureHttpServletRequestWrapper
        extends HttpServletRequestWrapper {

        public SecureHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public boolean isSecure() {
            return true;
        }

        @Override
        public String getScheme() {
            return "https";
        }
    }
}
