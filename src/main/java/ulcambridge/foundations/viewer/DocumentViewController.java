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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.EssayItem;
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
		// '0' is special page value indicating the item-level view
		return handleRequest(docId, 0, request);
	}

	// on path /view/{docId}/{page}
	@RequestMapping(value = "/{docId}/{page}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			@PathVariable("page") int page, HttpServletRequest request) {

		// force docID to uppercase
		docId = docId.toUpperCase();
		
		// Show a different view based on the itemType for this item.
		Item item = itemFactory.getItemFromId(docId);
		String itemType = item.getType();

		if (itemType.equals("essay")) {
			return setupEssayView((EssayItem) item, page, request);
		} else {
			// default to document view.
			return setupDocumentView(item, page, request);
		}

	}

	// on path /view/thumbnails/{docId}.json?page=?&start=?&limit=?
	@RequestMapping(value = "/thumbnails/{docId}.json")
	public ModelAndView handleThumbnailJSONRequest(
			@PathVariable("docId") String docId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			HttpServletResponse response) throws JSONException {

		// force docID to uppercase
		docId = docId.toUpperCase();
		
		Item item = itemFactory.getItemFromId(docId);
		JSONObject json = new JSONObject(item.getJSON(),
				JSONObject.getNames(item.getJSON()));
		JSONArray pages = json.getJSONArray("pages");

		if (limit != null && limit > 0 && limit < pages.length()
				&& start != null) {

			// build smaller pages array.
			JSONArray pagesSubset = new JSONArray();

			for (int i = start; i < (start + limit) && i < pages.length(); i++) {
				pagesSubset.put(pages.get(i));
			}

			json.put("pages", pagesSubset);
		}

		writeJSONOut(json, response);

		return null;
	}

	// on path /view/{docId}.json
	@RequestMapping(value = "/{docId}.json")
	public ModelAndView handleJSONRequest(@PathVariable("docId") String docId,
			HttpServletResponse response) throws JSONException {

		// force docID to uppercase
		docId = docId.toUpperCase();
		
		Item item = itemFactory.getItemFromId(docId);
		JSONObject json = item.getJSON();

		writeJSONOut(json, response);

		return null;

	}

	private void writeJSONOut(JSONObject json, HttpServletResponse response)
			throws JSONException {

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
			} catch (Exception e) {
			}
		}
	}

	/**
	 * View for displaying documents or manuscripts.
	 * 
	 * @param docId
	 * @param page
	 * @param request
	 * @return
	 */
	private ModelAndView setupDocumentView(Item item, int page,
			HttpServletRequest request) {

		// check doc exists, if not return 404 page.
		if (item == null) {
			return new ModelAndView("jsp/errors/404");
		}

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;

		if (docURL.lastIndexOf("/") == docURL.length() - 1) {
			// cut off last character.
			docURL = docURL.substring(0, docURL.length() - 1);
		}

		// JSON file needs to be a relative URL for Ajax.
		String jsonURL = "/view/" + item.getId() + ".json";
		String jsonThumbnailsURL = "/view/thumbnails/" + item.getId() + ".json";

		List<Collection> docCollections = getCollection(item.getId());
		Collection organisationalCollection = getBreadcrumbCollection(docCollections);

		//Get imageServer
		String imageServer = Properties.getString("imageServer");		
			
		//Get services
		String services = Properties.getString("services");

		// forward to the correct location based on type of item 
		// and page number, where page=0 is the item-level metadata
		ModelAndView modelAndView;
		if (page == 0) {
			modelAndView = new ModelAndView("jsp/document");
		} else {
			modelAndView = new ModelAndView("jsp/page");
			
		}
		
		/** URLs **/
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("jsonURL", jsonURL);
		modelAndView.addObject("jsonThumbnailsURL", jsonThumbnailsURL);
		modelAndView.addObject("requestURL", requestURL);		
		modelAndView.addObject("imageServer", imageServer);
		modelAndView.addObject("services", services);		

		/** Collection information **/
		modelAndView.addObject("organisationalCollection",
				organisationalCollection);
		modelAndView.addObject("collections", docCollections);
		
		// Get parent collection if there is one.
		Collection parent = null;
		if (organisationalCollection.getParentCollectionId() != null) {
			parent = collectionFactory
					.getCollectionFromId(organisationalCollection
							.getParentCollectionId());
		}
		modelAndView.addObject("parentCollection", parent);
		
		/** Item Information **/
		modelAndView.addObject("docId", item.getId());
		modelAndView.addObject("itemTitle", item.getTitle());
		modelAndView.addObject("itemAuthors",
				new JSONArray(item.getAuthorNames()));
		modelAndView.addObject("itemAuthorsFullform",
				new JSONArray(item.getAuthorNamesFullForm()));
		modelAndView.addObject("itemAbstract", item.getAbstract());

		modelAndView.addObject("itemFactory", itemFactory);
				
		/** Page Information **/
		modelAndView.addObject("page", page);
		
		// get the page label, if not at the item-page
		if (page > 0) {
			List<String> pageLabels = item.getPageLabels();  
			String label = "";
			if (pageLabels.size() > page - 1) {
				label = pageLabels.get(page - 1);
			}
			modelAndView.addObject("pageLabel", label);
			
			List<String> thumbnailURLs = item.getPageThumbnailURLs();
			String thumbnailURL = "";
			if (thumbnailURLs.size() > page - 1) {
				thumbnailURL = thumbnailURLs.get(page - 1);
			}
			modelAndView.addObject("thumbnailURL", thumbnailURL);
		} else {
			modelAndView.addObject("thumbnailURL", item.getThumbnailURL());
		}

		return modelAndView;
	}

	/**
	 * View for displaying essay data.
	 * 
	 * @param docId
	 * @param page
	 * @param request
	 * @return
	 */
	private ModelAndView setupEssayView(EssayItem item, int page,
			HttpServletRequest request) {

		if (item == null) {
			return new ModelAndView("jsp/errors/404");
		}

		List<Collection> docCollections = getCollection(item.getId());
		Collection organisationalCollection = getBreadcrumbCollection(docCollections);

		// Get parent collection if there is one.
		Collection parent = null;
		if (organisationalCollection.getParentCollectionId() != null) {
			parent = collectionFactory
					.getCollectionFromId(organisationalCollection
							.getParentCollectionId());
		}

		ModelAndView modelAndView = new ModelAndView("jsp/essay");

		modelAndView.addObject("docId", item.getId());
		modelAndView.addObject("organisationalCollection",
				organisationalCollection);
		modelAndView.addObject("collections", docCollections);

		modelAndView.addObject("itemTitle", item.getTitle());
		modelAndView.addObject("itemAuthors",
				new JSONArray(item.getAuthorNames()));
		modelAndView.addObject("itemAuthorsFullform",
				new JSONArray(item.getAuthorNamesFullForm()));
		modelAndView.addObject("itemAbstract", item.getAbstract());
		modelAndView.addObject("itemThumbnailURL", item.getThumbnailURL());
		modelAndView.addObject("itemThumbnailOrientation",
				item.getThumbnailOrientation());

		modelAndView.addObject("itemFactory", itemFactory);
		modelAndView.addObject("content", item.getContent());
		modelAndView.addObject("relatedItems", item.getItemReferences());
		modelAndView.addObject("parentCollection", parent);

		modelAndView.addObject("essayItem", item);
		return modelAndView;
	}

	/**
	 * Get a list of collections that this item is in.
	 * 
	 * @param docId
	 * @return
	 */
	private List<Collection> getCollection(String docId) {

		List<Collection> docCollections = new ArrayList<Collection>();

		Iterator<Collection> collectionIterator = collectionFactory
				.getCollections().iterator();
		while (collectionIterator.hasNext()) {
			Collection thisCollection = collectionIterator.next();
			if (thisCollection.getItemIds().contains(docId)) {
				docCollections.add(thisCollection);
			}
		}

		return docCollections;
	}

	/**
	 * Get the collection from this list we want to appear in the breadcrumb
	 * trail.
	 * 
	 * @param collections
	 * @return
	 */
	private Collection getBreadcrumbCollection(List<Collection> collections) {

		Collection collection = null;

		Iterator<Collection> collectionIterator = collections.iterator();
		while (collectionIterator.hasNext()) {

			Collection thisCollection = collectionIterator.next();

			// Stop if this is an organisational collection, else keep
			// looking
			if (thisCollection.getType().startsWith("organisation")) {
				collection = thisCollection;
			}

		}

		// If the item is not in any organisational collection set
		// the first item in the collections list as it's organisational
		// collection.
		if (collection == null && collections.size() > 0) {
			collection = collections.get(0);
		}

		return collection;
	}

}
