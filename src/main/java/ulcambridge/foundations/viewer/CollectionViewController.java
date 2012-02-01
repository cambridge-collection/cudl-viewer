package ulcambridge.foundations.viewer;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Collection;

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

		ModelAndView modelAndView = new ModelAndView("jsp/collections");
		return modelAndView;
	}

	// on path /collections/{collectionId}
	@RequestMapping(value = "/{collectionId}")
	public ModelAndView handleRequest(HttpServletResponse response, @PathVariable("collectionId") String collectionId,
			HttpServletRequest request) {
		
		//TODO 
		// Set attributes for the text, and html and images we want to read in.  
		// change collections page to be a single template 
		// think about collection type - virtual vs real collections
		// also display for exhibitions
		
		// Find our what type of collection it is
		//String collectionType = "organisation"; //TODO enum
		
						
		Collection collection = CollectionFactory.getCollectionFromId(collectionId);
				
		ModelAndView modelAndView = new ModelAndView("jsp/collection-"+collection.getType());
		
		modelAndView.addObject("collection", collection);
		
		return modelAndView;
		
	}

}