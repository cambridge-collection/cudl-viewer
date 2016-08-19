package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.Assert;
import uk.ac.cam.lib.spring.security.raven.BadStatusRavenAuthenticationException;
import uk.ac.cam.ucs.webauth.WebauthResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;

public class RavenAuthCancellationFailureHandler
    implements AuthenticationFailureHandler {

    private final AuthenticationFailureHandler defaultHandler;
    private final Handler handler;

    public RavenAuthCancellationFailureHandler(
        Handler handler,
        AuthenticationFailureHandler defaultHandler) {

        Assert.notNull(handler);
        Assert.notNull(defaultHandler);

        this.handler = handler;
        this.defaultHandler = defaultHandler;
    }

    public interface Handler {
        void handleAuthFailure(HttpServletRequest req, HttpServletResponse resp,
                               BadStatusRavenAuthenticationException e);
    }

    public static RavenAuthCancellationFailureHandler redirectingTo(
        String location, AuthenticationFailureHandler defaultHandler) {

        return new RavenAuthCancellationFailureHandler((req, resp, e) -> {
            try {
                resp.sendRedirect(location);
            }
            catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }, defaultHandler);
    }

    public static RavenAuthCancellationFailureHandler redirectingTo(
        String location) {

        return redirectingTo(
            location, new SimpleUrlAuthenticationFailureHandler());
    }

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception)
        throws IOException, ServletException {

        if(exception instanceof BadStatusRavenAuthenticationException) {
            BadStatusRavenAuthenticationException e =
                (BadStatusRavenAuthenticationException)exception;

            if(e.getStatus() == WebauthResponse.CANCELED) {
                this.handler.handleAuthFailure(request, response, e);

                return;
            }
        }

        this.defaultHandler.onAuthenticationFailure(
            request, response, exception);
    }
}
