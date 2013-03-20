package ulcambridge.foundations.viewer.search;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	public ModelAndView processSearch(@Valid SearchForm searchForm,
			BindingResult bindingResult) throws MalformedURLException {

		// Perform XTF Search
		SearchResultSet results = this.search.makeSearch(searchForm);

		// Remove any results that the viewer does not know about.
		ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();
		Iterator<SearchResult> resultIt = results.getResults().iterator();

		while (resultIt.hasNext()) {
			SearchResult result = resultIt.next();

			Item item = itemFactory.getItemFromId(result.getId());
			if (item != null) {
				refinedResults.add(result);
			}
		}

		// Build the (SearchResultSet) facets from the results
		List<FacetGroup> facets = getFacetsFromResults(refinedResults);

		results = new SearchResultSet(refinedResults.size(),
				results.getSpellingSuggestedTerm(), results.getQueryTime(),
				refinedResults, facets, results.getError());

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
	public ModelAndView advancedSearch(@Valid @ModelAttribute SearchForm searchInput,
			BindingResult bindingResult, HttpSession session) throws MalformedURLException {
		
		ModelAndView modelAndView = new ModelAndView("jsp/search-advanced");
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
	public ModelAndView processAdvancedSearch(@ModelAttribute @Valid SearchForm searchForm,
			BindingResult bindingResult, HttpSession session) throws MalformedURLException {
		
		// Perform XTF Search
		SearchResultSet results = this.search.makeSearch(searchForm);

		// Remove any results that the viewer does not know about.
		ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();
		Iterator<SearchResult> resultIt = results.getResults().iterator();

		while (resultIt.hasNext()) {
			SearchResult result = resultIt.next();

			Item item = itemFactory.getItemFromId(result.getId());
			if (item != null) {
				refinedResults.add(result);
			}
		}

		// Build the (SearchResultSet) facets from the results
		List<FacetGroup> facets = getFacetsFromResults(refinedResults);

		results = new SearchResultSet(refinedResults.size(),
				results.getSpellingSuggestedTerm(), results.getQueryTime(),
				refinedResults, facets, results.getError());

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

		// If session has timed out make search again.
		// if (results == null) {
		ModelAndView search = processSearch(searchForm, bindingResult);
		SearchResultSet results = (SearchResultSet) search.getModel().get(
				"results");
		// }

		// Put chosen search results into an array.
		JSONArray jsonArray = new JSONArray();

		if (results != null) {

			List<SearchResult> searchResults = results.getResults();

			if (startIndex < 0) {
				startIndex = 0;
			} else if (endIndex >= searchResults.size()) {
				endIndex = searchResults.size(); // if end Index is too large
													// cap at max
													// size
			}

			for (int i = startIndex; i < endIndex; i++) {
				SearchResult searchResult = searchResults.get(i);
				Item item = itemFactory.getItemFromId(searchResult.getId());

				// Make a JSON object that contains information about the
				// matching item and information about the result snippets 
				// and pages that match.
				JSONObject itemJSON = new JSONObject();
				itemJSON.put("item", item.getSimplifiedJSON());

				// Make an array for the snippets.
				JSONArray resultsArray = new JSONArray();
				List<DocHit> docHits = searchResult.getDocHits();

				for (int j = 0; j < docHits.size(); j++) {
					DocHit docHit = docHits.get(j);
					JSONObject snippetJSON = new JSONObject();
					snippetJSON.put("startPage", docHit.getStartPage());
					snippetJSON.put("startPageLabel",
							docHit.getStartPageLabel());
					JSONArray snippetArray = new JSONArray();
					snippetArray.add(docHit.getSnippetHTML());
					snippetJSON.put("snippetStrings", snippetArray);

					resultsArray.add(snippetJSON);
				}

				itemJSON.put("snippets", resultsArray);
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

	/**
	 * Make a list of facetgroups from the facets that exist in the list of
	 * search results.
	 * 
	 * @param results
	 * @return
	 */
	private List<FacetGroup> getFacetsFromResults(List<SearchResult> results) {

		Hashtable<String, FacetGroup> facetGroups = new Hashtable<String, FacetGroup>();

		Iterator<SearchResult> searchResultIterator = results.iterator();
		while (searchResultIterator.hasNext()) {

			SearchResult result = searchResultIterator.next();
			Iterator<Facet> facets = result.getFacets().iterator();
			while (facets.hasNext()) {
				Facet facet = facets.next();
				String field = facet.getField(); // like "subject" or "date"

				if (facetGroups.containsKey(field)) {
					// add band to facetgroup if already in our set
					FacetGroup group = facetGroups.get(field);
					try {
						group.add(facet);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					// add facetgroup to our set
					ArrayList<Facet> facetObjs = new ArrayList<Facet>();
					facetObjs.add(facet);

					FacetGroup group = new FacetGroup(field,
							facet.getFieldLabel(), facetObjs);
					facetGroups.put(field, group);
				}

			}
		}

		return new ArrayList<FacetGroup>(facetGroups.values());

	}

}
