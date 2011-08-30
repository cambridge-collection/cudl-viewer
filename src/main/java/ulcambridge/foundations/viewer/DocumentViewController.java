package ulcambridge.foundations.viewer;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads in the URL specified by the newtonDoc parameter and filters the content
 * to only display what we want.
 * 
 * @author jennie
 * 
 */
public class DocumentViewController extends HttpServlet {
	
   private static final transient Log log = 
		    LogFactory.getLog(DocumentViewController.class);
	  
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* Read in the part of the url structure that contains the document id */
		String documentId = request.getRequestURI();
		
		/* Read in the part of the url structure that contains the page number */
		log.debug("requestURI: "+request.getRequestURI());
		log.debug("servlet Path: "+request.getServletPath());
		
		/* FIXME Redirect to view with appropriate parameters */
		
		String destination = "/"+request.getServletPath()+"/document-view.jsp";
		 
		//RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
		//rd.forward(request, response);
		response.sendRedirect(destination);
		
	}
	
}
