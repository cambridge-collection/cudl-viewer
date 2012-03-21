package ulcambridge.foundations.viewer.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
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
public class SearchController {

	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ModelAndView processSearch(HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException {

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

		// TODO properly separate which search terms are passed to which search.
		// and combine the results properly.

		// Request XTF keyword search (raw=1 to return XML)
		Hashtable<String, String> xtfFacetQuery = new Hashtable<String, String>();
		xtfFacetQuery.putAll(facetQuery);

		if (xtfFacetQuery.containsKey("collection")) {
			xtfFacetQuery.remove("collection");
		}

		Document dom = makeXTFSearch(searchQuery.getKeyword(), xtfFacetQuery);

		// Read XML result into Model
		SearchResultSet results = parseSearchResults(dom);

		// apply collection facet
		if (facetQuery.containsKey("collection")) {

			String title = facetQuery.get("collection");
			Collection collection = CollectionFactory
					.getCollectionFromTitle(title);
			List<String> itemIds = collection.getItemIds();

			ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();

			Iterator<SearchResult> resultIt = results.getResults().iterator();
			while (resultIt.hasNext()) {
				SearchResult result = resultIt.next();

				if (itemIds.contains(result.getId())) {
					refinedResults.add(result);
				}
			}

			results = new SearchResultSet(refinedResults.size(),
					results.getSpellingSuggestedTerm(), results.getQueryTime(),
					refinedResults, results.getFacets(), "");

		}

		// make sure all results are known about by the interface
		// and not just for example in XTF.
		ArrayList<SearchResult> refinedResults = new ArrayList<SearchResult>();
		Iterator<SearchResult> resultIt = results.getResults().iterator();

		while (resultIt.hasNext()) {
			SearchResult result = resultIt.next();
			Item item = ItemFactory.getItemFromId(result.getId());
			if (item != null) {
				refinedResults.add(result);
			}
		}

		// Build the facets from the results, not from the XTF summary. 
		List<FacetGroup> facets = getFacetsFromResults(refinedResults);
		
		results = new SearchResultSet(refinedResults.size(),
				results.getSpellingSuggestedTerm(), results.getQueryTime(),
				refinedResults, facets, "");

		// Add collection facet into search results
		results.addFacetGroup(0,getCollectionFacet(results));

		ModelAndView modelAndView = new ModelAndView("jsp/search-results");
		modelAndView.addObject("query", searchQuery);
		modelAndView.addObject("results", results);
		return modelAndView;
	}

	// Request XTF keyword search (raw=1 to return XML)
	// Read XML result into Model

	private Document makeXTFSearch(String keyword, Map<String, String> facets)
			throws MalformedURLException {

		String xtfURL = Properties.getString("xtfURL");
		String searchXTFURL = xtfURL + "search?raw=1";
		
		if (keyword!=null && keyword.equals("")) {
			searchXTFURL += "&browse-all=yes";
		} else {
			searchXTFURL += "&keyword=" + keyword;
		}
		

		Iterator<String> facetTypes = facets.keySet().iterator();
		int facetCount = 0;
		while (facetTypes.hasNext()) {
			String facetType = facetTypes.next();
			String facetValue = facets.get(facetType);
			facetCount++;
			searchXTFURL = searchXTFURL + ";f" + facetCount + "-" + facetType
					+ "=" + facetValue;
		}

		// Read document from URL and put results in Document.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(searchXTFURL);

			return dom;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	/**
	 * Parse the XML dom document and put the results into a list of
	 * SearchResult objects.
	 * 
	 * @param dom
	 * @return List of the search results
	 */
	private SearchResultSet parseSearchResults(Document dom) {

		// Check input - XTF may be down.
		if (dom == null) {
			return new SearchResultSet(0, "", 0f,
					new ArrayList<SearchResult>(), new ArrayList<FacetGroup>(),
					"A problem occurred making the search (xtf).");
		}

		// Get the root element
		Element docEle = dom.getDocumentElement();
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();

		// Catch any errors
		if (!docEle.getNodeName().equals("crossQueryResult")) {
			return new SearchResultSet(
					0,
					"",
					0f,
					new ArrayList<SearchResult>(),
					new ArrayList<FacetGroup>(),
					"Too many results, try a smaller range, eliminating wildcards, or making them more specific. ");
		}

		// Add in all the (docHit) results into a List.
		NodeList docHits = docEle.getElementsByTagName("docHit");
		if (docHits != null) {
			for (int i = 0; i < docHits.getLength(); i++) {

				Node node = docHits.item(i);
				SearchResult result = new SearchResult(node);
				results.add(result);
			}
		}

		// Get general search result data
		int totalDocs = Integer.parseInt(docEle.getAttribute("totalDocs"));
		float queryTime = Float.parseFloat(docEle.getAttribute("queryTime"));
		Element spelling = (Element) docEle.getElementsByTagName("spelling")
				.item(0);
		String suggestedTerm = "";
		if (spelling != null) {
			Element suggestion = (Element) spelling.getElementsByTagName(
					"suggestion").item(0);
			suggestedTerm = suggestion.getAttribute("suggestedTerm");
		}

		return new SearchResultSet(totalDocs, suggestedTerm, queryTime,
				results, null, "");
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
					
					FacetGroup group = new FacetGroup(field, facet.getFieldLabel(), facetObjs);
					facetGroups.put(field, group);
				}

			}
		}
		
		return new ArrayList<FacetGroup>(facetGroups.values());

	}
}
