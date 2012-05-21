package ulcambridge.foundations.viewer;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

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
		ArrayList<Item> featuredItems = new ArrayList<Item> (); 
		String[] itemIds = Properties.getString("collection.featuredItems").split("\\s*,\\s*");
		for (int i=0; i<itemIds.length; i++) {
		  String itemId = itemIds[i];
		  featuredItems.add(ItemFactory.getItemFromId(itemId));
		}
		modelAndView.addObject("featuredItems", featuredItems);
		return modelAndView;
	}

	// on path /collections/{collectionId}
	@RequestMapping(value = "/{collectionId}")
	public ModelAndView handleRequest(HttpServletResponse response, @PathVariable("collectionId") String collectionId,
			HttpServletRequest request) {
		
		Collection collection = CollectionFactory.getCollectionFromId(collectionId);
		
		ModelAndView modelAndView = new ModelAndView("jsp/collection-"+collection.getType());
		
		modelAndView.addObject("collection", collection);

		return modelAndView;
		
	}

}