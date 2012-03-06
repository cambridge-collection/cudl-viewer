package ulcambridge.foundations.viewer;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ulcambridge.foundations.viewer.model.Properties;
import ulcambridge.foundations.viewer.model.SearchQuery;
import ulcambridge.foundations.viewer.model.SearchResult;
import ulcambridge.foundations.viewer.model.SearchResultFacet;
import ulcambridge.foundations.viewer.model.SearchResultSet;

/**
 * Controller for viewing a collection.
 * 
 * @author jennie
 * 
 */
@Controller
public class SearchController {

	protected final Log logger = LogFactory.getLog(getClass());

	// on path /search/
	@RequestMapping(value = "/search")
	public ModelAndView handleViewRequest(@RequestParam("query") String query,
			@RequestParam("facets") String facets,
			@RequestParam("sort") String sort, HttpServletRequest request)
			throws Exception {

		// Record Query
		// expects facets specified as facet=subject:maths,date:1900 etc.
		String[] facetArray = facets.split("\\s*,\\s*");
		Hashtable<String, String> facetQuery = new Hashtable<String, String>();
		if (facets != null && facets.trim().length()>0) {
			for (int i = 0; i < facetArray.length; i++) {
				String[] facetTypeValue = facetArray[i].split("\\s*:\\s*");
				for (int j = 0; j < facetTypeValue.length; j++) {
					String facetType = facetTypeValue[0];
					String facetValue = facetTypeValue[1];
					facetQuery.put(facetType, facetValue);
				}
			}
		}
		SearchQuery searchQuery = new SearchQuery(query, facetQuery, sort);

		// Request XTF keyword search (raw=1 to return XML)
		Document dom = makeSearch(searchQuery);

		// Read XML result into Model
		SearchResultSet results = parseSearchResults(dom);

		ModelAndView modelAndView = new ModelAndView("jsp/search");
		modelAndView.addObject("query", searchQuery);
		modelAndView.addObject("results", results);
		return modelAndView;
	}

	// Request XTF keyword search (raw=1 to return XML)
	// Read XML result into Model

	private Document makeSearch(SearchQuery query) throws MalformedURLException {

		String xtfURL = Properties.getString("xtfURL");

		String searchXTFURL = xtfURL + "search?raw=1;keyword="
				+ query.getKeyword();

		Map<String, String> facets = query.getFacets();
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

		// Get the root element
		Element docEle = dom.getDocumentElement();
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();

		// Add in all the (docHit) results into a List.
		NodeList docHits = docEle.getElementsByTagName("docHit");
		if (docHits != null) {
			for (int i = 0; i < docHits.getLength(); i++) {

				Node node = docHits.item(i);
				SearchResult result = new SearchResult(node);
				results.add(result);
			}
		}

		// Get facets
		List<Node> facetNodes = getNodes("facet", docEle.getChildNodes());

		List<SearchResultFacet> facets = new ArrayList<SearchResultFacet>();
		if (facetNodes != null) {
			for (int i = 0; i < facetNodes.size(); i++) {
				Element node = (Element) facetNodes.get(i);
				facets.add(new SearchResultFacet(node));
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
				results, facets);
	}

	/**
	 * Looks through the list of nodes and returns the node(s) with the
	 * specified name.
	 * 
	 * @param nodeName
	 * @param nodes
	 * @return
	 */
	private List<Node> getNodes(String nodeName, NodeList nodes) {

		ArrayList<Node> matches = new ArrayList<Node>();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node child = nodes.item(i);
			if (child.getNodeName().equals(nodeName)) {
				matches.add(child);
			}
		}
		return matches;
	}
}
