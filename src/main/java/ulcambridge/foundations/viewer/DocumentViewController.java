package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for viewing a specific document or a specific page within a
 * document. Documents and pages are specified in the url requested.
 * 
 * @author jennie
 * 
 */
@Controller
public class DocumentViewController {

	protected final Log logger = LogFactory.getLog(getClass());
	private static JSONReader reader = new JSONReader();
	
	private CollectionFactory collectionFactory;
	private ItemFactory itemFactory;
	
	@Autowired
	public void setCollectionFactory(CollectionFactory factory) {
		this.collectionFactory = factory;
	}
	
	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}	
	
	// on path /view/{docId}
	@RequestMapping(value = "/{docId}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			HttpServletRequest request) {

		return setupDocumentView(docId, 1, request);
	}

	// on path /view/{docId}/{page}
	@RequestMapping(value = "/{docId}/{page}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			@PathVariable("page") int page, HttpServletRequest request) {
	
		return setupDocumentView(docId, page, request);
	}

	// on path /view/{docId}.json
	@RequestMapping(value = "/{docId}.json")
	public ModelAndView handleJSONRequest(@PathVariable("docId") String docId,
			HttpServletResponse response) throws JSONException {

		Item item = itemFactory.getItemFromId(docId);
		JSONObject json = item.getJSON();

		// Write out JSON file.
		response.setContentType("application/json");
		PrintStream out = null;
		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()), true, "UTF-8");
			out.print(json.toString(1));
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {}		
		}

		return null;
	}

	private ModelAndView setupDocumentView(String docId, int page,
			HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;

		if (docURL.lastIndexOf("/") == docURL.length() - 1) {
			// cut off last character.
			docURL = docURL.substring(0, docURL.length() - 1);
		}

		// JSON file needs to be a relative URL for Ajax.
		String jsonURL =  "/view/"+ docId + ".json";

		// check doc exists, if not return 404 page.
		String jsonRootURL = Properties.getString("jsonURL");
		if (!reader.urlExists(jsonRootURL + docId + ".json")) {
			return new ModelAndView("jsp/errors/404");
		}

		
		List<Collection> docCollections = new ArrayList<Collection>();
		Collection organisationalCollection = null; 
				
		Iterator<Collection> collectionIterator = collectionFactory.getCollections().iterator();
		while (collectionIterator.hasNext()) {
			Collection collection = collectionIterator.next();
			if (collection.getItemIds().contains(docId)) {
				docCollections.add(collection);
				// Stop if this is an organisational collection, else keep
				// looking
				if (collection.getType().startsWith("organisation")) {
					organisationalCollection = collection;	
				}				
			}
		}
		
		// If the item is not in any organisational collection set 
		// the first item in the collections list as it's organisational collection. 
		if (organisationalCollection == null && docCollections.size()>0 ) { 
			organisationalCollection = docCollections.get(0);
		}

		// Get proxyURL (if we are using a proxy)
		String proxyURL = "";
		if (Properties.getString("useProxy").equals("true")) {
			proxyURL = Properties.getString("proxyURL");
		}

		ModelAndView modelAndView = new ModelAndView("jsp/document");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", page); 
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("jsonURL", jsonURL);
		modelAndView.addObject("requestURL", requestURL);
		modelAndView.addObject("proxyURL", proxyURL);
		modelAndView.addObject("organisationalCollection", organisationalCollection);
		modelAndView.addObject("collections", docCollections);
		
		Item item = itemFactory.getItemFromId(docId);
		modelAndView.addObject("itemTitle", item.getTitle());
		modelAndView.addObject("itemAuthors", new JSONArray(item.getAuthorNames()));
		modelAndView.addObject("itemAuthorsFullform", new JSONArray(item.getAuthorNamesFullForm()));
		modelAndView.addObject("itemAbstract", item.getAbstract());
		
		return modelAndView;
	}


}