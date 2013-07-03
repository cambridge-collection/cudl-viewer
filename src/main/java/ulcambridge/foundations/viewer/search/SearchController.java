package ulcambridge.foundations.viewer.search;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for viewing a collection.
 * 
 * @author jennie
 * 
 */
@Controller
public class SearchController {

	protected final Log logger = LogFactory.getLog(getClass());
	private Search search;
	private ItemFactory itemFactory;

	/**
	 * Constructor, set in search-servlet.xml.
	 * 
	 * @param search
	 *            to use for queries. e.g. SearchXTF.
	 */
	public SearchController(Search search) {
		this.search = search;
	}

	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}

	// on /search path
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ModelAndView processSearch(@Valid SearchForm searchForm)
			throws MalformedURLException {

		// Perform XTF Search
		SearchResultSet results = this.search.makeSearch(searchForm, 1, 1);

		ModelAndView modelAndView = new ModelAndView("jsp/search-results");
		modelAndView.addObject("form", searchForm);
		modelAndView.addObject("results", results);

		return modelAndView;
	}

	/**
	 * Advanced Search Query Page
	 * 
	 * /search/advanced/query
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/advanced/query")
	public ModelAndView advancedSearch(
			@Valid @ModelAttribute SearchForm searchForm,
			BindingResult bindingResult, HttpSession session)
			throws MalformedURLException {

		ModelAndView modelAndView = new ModelAndView("jsp/search-advanced");
		modelAndView.addObject("form", searchForm);
		return modelAndView;
	}

	/**
	 * Advanced Search Results Page Performs search and displays results.
	 * 
	 * on /search/advanced/results path
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/advanced/results")
	public ModelAndView processAdvancedSearch(
			@ModelAttribute @Valid SearchForm searchForm,
			BindingResult bindingResult, HttpSession session)
			throws MalformedURLException {

		// Perform XTF Search
		SearchResultSet results = this.search.makeSearch(searchForm, 1, 1);

		ModelAndView modelAndView = new ModelAndView(
				"jsp/search-advancedresults");
		modelAndView.addObject("form", searchForm);
		modelAndView.addObject("results", results);

		return modelAndView;
	}

	// on path /search/JSON?start=<startIndex>&end=<endIndex>&search params
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/JSON")
	public ModelAndView handleItemsAjaxRequest(@Valid SearchForm searchForm,
			BindingResult bindingResult, HttpServletResponse response,
			@RequestParam("start") int startIndex,
			@RequestParam("end") int endIndex, HttpServletRequest request)
			throws MalformedURLException {

		SearchResultSet results = this.search.makeSearch(searchForm,
				startIndex, endIndex);

		// Put chosen search results into an array.
		JSONArray jsonArray = new JSONArray();

		if (results != null) {

			List<SearchResult> searchResults = results.getResults();

			for (int i = 0; i < searchResults.size(); i++) {
				SearchResult searchResult = searchResults.get(i);
				Item item = itemFactory.getItemFromId(searchResult.getFileId());

				// Make a JSON object that contains information about the
				// matching item and information about the result snippets
				// and pages that match.
				JSONObject itemJSON = new JSONObject();
				itemJSON.put("item", item.getSimplifiedJSON());
				itemJSON.put("startPage", searchResult.getStartPage());
				itemJSON.put("startPageLabel", searchResult.getStartPageLabel());
				itemJSON.put("itemType", searchResult.getType());

				// Make an array for the snippets.
				JSONArray resultsArray = new JSONArray();

				for (String snippet : searchResult.getSnippets()) {
					resultsArray.add(snippet.trim());
				}

				itemJSON.put("snippets", resultsArray);

				// Page Thumbnails.  Use specified thumbnail if possible. 
				if (searchResult.getThumbnailURL() != null) {
					
					String pageThumbnail = searchResult.getThumbnailURL();
					if (Properties.getString("useProxy").equals("true")) {
						pageThumbnail = Properties
								.getString("proxyURL") + pageThumbnail;
					}					
					itemJSON.put("pageThumbnailURL", pageThumbnail);
					
				} else {
					String pageThumbnail = "";
					org.json.JSONObject page = null;
					try {
						page = (org.json.JSONObject) item.getJSON()
								.getJSONArray("pages")
								.get(searchResult.getStartPage() - 1);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					try {
						if (page != null && page.get("displayImageURL") != null) {

							// FIXME super hacky way to get the page thumbnail
							// URL until we can read it from json.
							pageThumbnail = page.get("displayImageURL")
									.toString()
									.replace(".dzi", "_files/8/0_0.jpg");

							if (Properties.getString("useProxy").equals("true")) {
								pageThumbnail = Properties
										.getString("proxyURL") + pageThumbnail;
							}

						}
					} catch (JSONException e) {

						// displayImageURL not found, which throws an exception.
						pageThumbnail = "/images/collectionsView/no-thumbnail.jpg";

					}

					itemJSON.put("pageThumbnailURL", pageThumbnail);

				}// End Page Thumbnails

				jsonArray.add(itemJSON);
			}
		}

		// Write out JSON file.
		response.setContentType("application/json");
		PrintStream out = null;

		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()), true, "UTF-8");

			out.print(jsonArray);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		return null;

	}

}
