package ulcambridge.foundations.viewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DocumentViewController {

	protected final Log logger = LogFactory.getLog(getClass());

	
	// on path /view/{docId}
	@RequestMapping(value = "/{docId}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId) {
		logger.info("Returning doc view");

		ModelAndView modelAndView = new ModelAndView("jsp/document-view");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", 1); // defaults to first page. 
		return modelAndView;
	}
	
	// on path /view/{docId}/{page}
	@RequestMapping(value = "/{docId}/{page}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			@PathVariable("page") String page) {
		logger.info("Returning page view");

		ModelAndView modelAndView = new ModelAndView("jsp/document-view");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", page);
		return modelAndView;
	}

}