package ulcambridge.foundations.viewer.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.model.Collection;
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
	
	/**
	 * Constructor, set in search-servlet.xml.
	 * 
	 * @param search to use for queries. e.g. SearchXTF. 
	 */
	public SearchController(Search search) {
		this.search = search;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ModelAndView processSearch(HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException {

		// Build SearchQuery object from the search parameters.
		Enumeration<String> paramNames = request.getParameterNames();
		String keywordQuery = "";
		Hashtable<String, String> facetQuery = new Hashtable<String, String>();

		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName != null && paramName.equals("keyword")) {
				keywordQuery = request.getParameter(paramName);
			} else if (paramName != null && paramName.matches("^facet-.+$")) {
				facetQuery.put(paramName.substring(6),
						request.getParameter(paramName));
			}
		}

		SearchQuery searchQuery = new SearchQuery(keywordQuery, facetQuery);

		// Perform XTF Search
		SearchResultSet results = this.search.makeSearch(searchQuery);

		// Apply collection facet
		if (facetQuery.containsKey("collection")) {
			results = applyCollectionFacet(searchQuery, results);
		}

		// Remove any results that the viewer does not know about. 
		ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();
		Iterator<SearchResult> resultIt = results.getResults().iterator();

		while (resultIt.hasNext()) {
			SearchResult result = resultIt.next();
			Item item = ItemFactory.getItemFromId(result.getId());
			if (item != null) {
				refinedResults.add(result);
			}
		}		

		// Build the (SearchResultSet) facets from the results
		List<FacetGroup> facets = getFacetsFromResults(refinedResults);

		results = new SearchResultSet(refinedResults.size(),
				results.getSpellingSuggestedTerm(), results.getQueryTime(),
				refinedResults, facets, results.getError());

		// Add collection facet into search results
		results.addFacetGroup(0, getCollectionFacet(results));

		ModelAndView modelAndView = new ModelAndView("jsp/search-results");
		modelAndView.addObject("query", searchQuery);
		modelAndView.addObject("results", results);
		return modelAndView;
	}

	/**
	 * Refines the searchResults, removing any results from
	 * the SearchResultSet that do not match the specified collection.  
	 *  
	 * @param facetName
	 * @param results
	 * @return
	 */
	private SearchResultSet applyCollectionFacet(SearchQuery searchQuery, SearchResultSet results) {

		String title = searchQuery.getFacets().get("collection");
		Collection collection = CollectionFactory.getCollectionFromTitle(title);
		List<String> itemIds = collection.getItemIds();

		ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();

		Iterator<SearchResult> resultIt = results.getResults().iterator();
		while (resultIt.hasNext()) {
			SearchResult result = resultIt.next();

			if (itemIds.contains(result.getId())) {
				refinedResults.add(result);
			}
		}

		return new SearchResultSet(refinedResults.size(),
				results.getSpellingSuggestedTerm(), results.getQueryTime(),
				refinedResults, results.getFacets(), results.getError());

	}

	/**
	 * Make a new collection Facet containing those collections that exist in
	 * the results
	 * 
	 * @param results
	 * @return
	 */
	private FacetGroup getCollectionFacet(SearchResultSet results) {

		Hashtable<Collection, Integer> collectionsInResults = new Hashtable<Collection, Integer>();

		// Go through all the collections and see if there are any
		// results that match ids in these collections.

		Iterator<Collection> allCollectionsIterator = CollectionFactory
				.getCollections().iterator();
		while (allCollectionsIterator.hasNext()) {

			Collection collection = allCollectionsIterator.next();
			List<String> itemIds = collection.getItemIds();

			// Go through all the search results looking for a match for this
			// collection
			Iterator<SearchResult> searchResultIterator = results.getResults()
					.iterator();
			while (searchResultIterator.hasNext()) {
				SearchResult result = searchResultIterator.next();
				if (itemIds.contains(result.getId())) {

					// Found item in this collection
					if (!collectionsInResults.containsKey(collection)) {
						collectionsInResults
								.put(collection, Integer.valueOf(1));
					} else {
						int count = collectionsInResults.get(collection)
								.intValue();
						count++;
						collectionsInResults.put(collection,
								Integer.valueOf(count)); // overwrites.
					}
				}
			}
		}

		// Now go through the collections found and make a facet
		String field = "collection";
		String fieldLabel = "Collection";
		List<Facet> bands = new ArrayList<Facet>();

		Enumeration<Collection> collectionsEnum = collectionsInResults.keys();
		while (collectionsEnum.hasMoreElements()) {
			Collection collection = collectionsEnum.nextElement();
			Integer count = collectionsInResults.get(collection);
			Facet facet = new Facet(field, collection.getTitle(), count);
			bands.add(facet);
		}

		return new FacetGroup(field, fieldLabel, bands);

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
