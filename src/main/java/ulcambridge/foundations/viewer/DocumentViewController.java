package ulcambridge.foundations.viewer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DocumentViewController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	// on path /view/
	@RequestMapping(value = "/")
	public ModelAndView handleRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/collections");
		return modelAndView;
	}
	
	
	// on path /view/{docId}
	@RequestMapping(value = "/{docId}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId, HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;
		if (docURL.lastIndexOf("/")==docURL.length()-1){
			//cut off last character. 
			docURL = docURL.substring(0, docURL.length()-2);	
		}
		
		ModelAndView modelAndView = new ModelAndView("jsp/document");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", 1); // defaults to first page. 
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("requestURL", requestURL);
		
		return modelAndView;
	}
	
	// on path /view/{docId}/{page}
	@RequestMapping(value = "/{docId}/{page}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			@PathVariable("page") String page, HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;
		if (docURL.lastIndexOf("/")==docURL.length()-1){
			//cut off last character. 
			docURL = docURL.substring(0, docURL.length()-2);	
		}
		// cut off the page part of the url. 
		docURL = docURL.substring(0, docURL.lastIndexOf("/"));

		ModelAndView modelAndView = new ModelAndView("jsp/document");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", page);
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("requestURL", requestURL);
		return modelAndView;
	}

}