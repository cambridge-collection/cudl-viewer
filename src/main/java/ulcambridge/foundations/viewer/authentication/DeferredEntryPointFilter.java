package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ulcambridge.foundations.viewer.utils.Utils.stream;

/**
 * A ServletFilter which is responsible for invoking an
 * {@link AuthenticationEntryPoint} on matching requests. This can be used to
 * run an entry point on a subsequent request to the one that triggered an
 * {@link AuthenticationException}.
 */
public class DeferredEntryPointFilter extends GenericFilterBean {

    private final List<EntryPointSelector> entryPointSelectors;

    public DeferredEntryPointFilter(Iterable<EntryPointSelector> entryPointSelectors) {
        Assert.notNull(entryPointSelectors);
        entryPointSelectors.forEach(s -> Assert.notNull(s));

        this.entryPointSelectors =
            stream(entryPointSelectors).collect(Collectors.toList());
    }

    protected Optional<AuthenticationEntryPoint> getEntryPoint(
        HttpServletRequest request) {

        return this.entryPointSelectors.stream()
            .map(s -> s.selectEntryPoint(request))
            .filter(ep -> ep.isPresent())
            .findFirst()
            .orElse(Optional.empty());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {

        Optional<AuthenticationEntryPoint> entryPoint =
            this.getEntryPoint((HttpServletRequest)request);

        if(!entryPoint.isPresent()) {
            chain.doFilter(request, response);
            return;
        }

        entryPoint.get().commence(
            (HttpServletRequest)request, (HttpServletResponse)response,
            new DeferredEntryPointContinuationAuthenticationException());
    }

    public interface EntryPointSelector {
        Optional<AuthenticationEntryPoint> selectEntryPoint(
            HttpServletRequest request);
    }

    /**
     * An {@link AuthenticationException} which is used when the execution of
     * the AuthenticationEntryPoint has been deferred and invoked by a
     * {@link DeferredEntryPointFilter}.
     */
    public static class DeferredEntryPointContinuationAuthenticationException
        extends AuthenticationException {

        public DeferredEntryPointContinuationAuthenticationException(
            String msg, Throwable t) {

            super(msg, t);
        }

        public DeferredEntryPointContinuationAuthenticationException(
            String msg) {

            super(msg);
        }

        public DeferredEntryPointContinuationAuthenticationException() {
            this("Continuing deferred authentication process");
        }
    }
}
