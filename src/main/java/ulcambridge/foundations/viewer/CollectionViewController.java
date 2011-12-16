package ulcambridge.foundations.viewer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for viewing a collection.
 * 
 * @author jennie
 * 
 */
@Controller
public class CollectionViewController {

	protected final Log logger = LogFactory.getLog(getClass());
		
	// on path /collections/
	@RequestMapping(value = "/")
	public ModelAndView handleViewRequest(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		ModelAndView modelAndView = new ModelAndView("jsp/collections");
		return modelAndView;
	}

	// on path /collections/{collectionId}
	@RequestMapping(value = "/{collectionId}")
	public ModelAndView handleRequest(HttpServletResponse response, @PathVariable("collectionId") String collectionId,
			HttpServletRequest request) {
		response.setContentType("text/html;charset=utf-8");
		ModelAndView modelAndView = new ModelAndView("jsp/collection-"+collectionId);
		return modelAndView;
		
	}

}