package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * As {@link SavedRequestAwareAuthenticationSuccessHandler} except that saved
 * requests are removed from the request cache once they've been used for
 * redirection.
 */
public class CudlSavedRequestAwareAuthenticationSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache;

    public CudlSavedRequestAwareAuthenticationSuccessHandler(
        RequestCache requestCache) {

        Assert.notNull(requestCache);

        this.requestCache = requestCache;
        super.setRequestCache(requestCache);
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws ServletException, IOException {

        super.onAuthenticationSuccess(request, response, authentication);

        // Remove the saved request from the cache after redirecting so that we
        // don't re-use it next time.
        this.requestCache.removeRequest(request, response);
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {
        throw new RuntimeException("Use constructor arg");
    }
}
