package ulcambridge.foundations.viewer.authentication;

import java.io.IOException;

import javax.management.relation.Role;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import ulcambridge.foundations.viewer.model.Person;

/**
 * Populates an internal system user which is needed when using the Spring OAuth API
 *
 */
public class OAuth2AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private String key;
	
    protected OAuth2AuthenticationProcessingFilter() {
        super("/mylibrary");
    }

    @Autowired
    public void setKey(String key) {
    	this.key = key;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    	System.out.println("Attempting AUTHENICATION!");
    	// User has not yet authenticated
    	
    	Authentication authentication = new AnonymousAuthenticationToken(key,"anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    	
        //Authentication authentication = new TestingAuthenticationToken("internal_system_user", "internal_null_credentials", "ROLE_USER");
        authentication.setAuthenticated(true);
        return getAuthenticationManager().authenticate(authentication);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

    	System.out.println("INFILTER");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(attemptAuthentication((HttpServletRequest) req, (HttpServletResponse) res));

            if (logger.isDebugEnabled()) {
                logger.debug("Populated SecurityContextHolder with dummy token: '"
                       + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated with dummy token, as it already contained: '"
                       + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        }
        chain.doFilter(req, res);
    }
}
