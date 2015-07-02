package ulcambridge.foundations.viewer.authentication;

import gs.spri.raven.RavenAuthenticationException;
import gs.spri.raven.RavenException;
import gs.spri.raven.RavenServlet;
import gs.spri.raven.RavenStateException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

public class RavenLoginServlet extends RavenServlet {

    private static final long serialVersionUID = 6459642286645404697L;
    private HttpServletRequest request;
    String access;

    @Override
    protected void performAction(RavenServlet.Action a, HttpSession session,
            HttpServletResponse response) throws IOException, ServletException {
        // Store the username in the session and redirect to the raven login url
        session.setAttribute("cudl-raven-username", this.getUserName(session));

        access = (String) session.getAttribute("access");
        // See LoginController
        response.sendRedirect("/auth/raven/login?access=" + access);
        //super.doGet( request, response);
        System.out.println("RavenLoginServlet-" + access);
    }

    // Called when authentication is not possible because a user could not be
    // authenticated.
    @Override
    protected void reportRavenAuthenticationException(
            RavenServlet.Action action,
            javax.servlet.http.HttpServletResponse res,
            RavenAuthenticationException cause) throws IOException {

        cause.printStackTrace();
        res.sendRedirect("/auth/login?error=There was a problem logging into Raven.&access=" + access);
    }

    // Called when authentication is not possible because of a protocol error.
    protected void reportRavenException(RavenServlet.Action action,
            javax.servlet.http.HttpServletResponse res, RavenException cause) throws IOException {

        cause.printStackTrace();
        res.sendRedirect("/auth/login?error=There was a problem logging into Raven.&access=" + access);
    }

    // Called when a token is received from Raven before the application has
    // requested one.
    protected void reportRavenStateException(RavenServlet.Action action,
            javax.servlet.http.HttpServletResponse res,
            RavenStateException cause) throws IOException {

        cause.printStackTrace();
        res.sendRedirect("/auth/login?error=There was a problem logging into Raven.&access=" + access);
    }

    // Called when a ServletException is thrown.
    protected void reportServletException(RavenServlet.Action action,
            javax.servlet.http.HttpServletResponse res,
            javax.servlet.ServletException cause) throws IOException {

        cause.printStackTrace();
        res.sendRedirect("/auth/login?error=There was a problem logging into Raven.&access=" + access);
    }

}
