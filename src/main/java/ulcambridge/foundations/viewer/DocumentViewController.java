package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
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
			HttpServletResponse response) {

		Item item = ItemFactory.getItemFromId(docId);
		JSONObject json = item.getJSON();

		// Write out JSON file.
		response.setContentType("application/json");
		PrintStream out = null;
		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()));
			out.print(json.toString());
			
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
		if (!urlExists(jsonRootURL + docId + ".json")) {
			return new ModelAndView("jsp/errors/404");
		}

		Collection docCollection = null;
		Iterator<Collection> collectionIterator = CollectionFactory
				.getCollections().iterator();
		while (collectionIterator.hasNext()) {
			Collection collection = collectionIterator.next();
			if (collection.getItemIds().contains(docId)) {
				docCollection = collection;

				// Stop if this is an organisational collection, else keep
				// looking
				if (collection.getTitle().startsWith("organisation")) {
					break;
				}
			}
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
		if (docCollection != null) {
			modelAndView.addObject("collectionURL", docCollection.getURL());
			modelAndView.addObject("collectionTitle", docCollection.getTitle());
		}

		return modelAndView;
	}

	/**
	 * Checks to see if the specified URl exists
	 * 
	 * @param jsonURL
	 * @return
	 */
	private boolean urlExists(String urlString) {

		try {
			URL url = new URL(urlString);

			if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			/* do nothing */
		}
		return false;

	}
}