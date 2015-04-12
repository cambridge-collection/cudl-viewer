package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Populates an internal system user which is needed when using the Spring OAuth API
 *
 */
public class SystemAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    protected SystemAuthenticationProcessingFilter() {
        super("/dummy_will_never_be_used_in_practice");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // Populate an internal system user in the security context with proper access privileges. These is typically not
        // necessary for multi user systems in production as users typically have to authenticate against your
        // application before using it.
        Authentication authentication = new TestingAuthenticationToken("internal_system_user", "internal_null_credentials", "ROLE_USER");
        authentication.setAuthenticated(true);
        return getAuthenticationManager().authenticate(authentication);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(attemptAuthentication((HttpServletRequest) req, (HttpServletResponse) res));

            if (logger.isDebugEnabled()) {
         //       logger.debug("Populated SecurityContextHolder with dummy token: '"
         //               + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        } else {
            if (logger.isDebugEnabled()) {
          //      logger.debug("SecurityContextHolder not populated with dummy token, as it already contained: '"
          //              + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        }
        chain.doFilter(req, res);
    }
}
