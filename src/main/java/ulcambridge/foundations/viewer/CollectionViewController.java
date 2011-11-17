package ulcambridge.foundations.viewer;

import javax.servlet.http.HttpServletRequest;

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
	public ModelAndView handleViewRequest() throws Exception {

		ModelAndView modelAndView = new ModelAndView("jsp/errors/404");
		return modelAndView;
	}

	// on path /collections/{collectionId}
	@RequestMapping(value = "/{collectionId}")
	public ModelAndView handleRequest(@PathVariable("collectionId") String collectionId,
			HttpServletRequest request) {

		if (collectionId.equalsIgnoreCase("newton")) {
			ModelAndView modelAndView = new ModelAndView("jsp/collection-newton");
			return modelAndView;
		}
		
		return new ModelAndView("jsp/errors/404");
	}

}