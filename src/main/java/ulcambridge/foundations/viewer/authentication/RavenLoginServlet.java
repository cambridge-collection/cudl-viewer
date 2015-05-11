package ulcambridge.foundations.viewer.authentication;

import gs.spri.raven.RavenServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RavenLoginServlet extends RavenServlet
{	
	private static final long serialVersionUID = 6459642286645404697L;	

    protected void performAction(RavenServlet.Action a, HttpSession session, HttpServletResponse response)
throws IOException, ServletException
{    
    // Store the username in the session and redirect to the raven login url 
    session.setAttribute("cudl-raven-username", this.getUserName(session));      
    
    // See LoginController
    response.sendRedirect("/auth/raven/login");
    
}

}