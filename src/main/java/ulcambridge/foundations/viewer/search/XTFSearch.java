package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ulcambridge.foundations.viewer.model.Properties;

public class XTFSearch implements Search {

	// Request XTF keyword search (raw=1 to return XML)
	// Read XML result into Model
	@Override
	public SearchResultSet makeSearch(SearchQuery searchQuery) {

		// Remove unsupported facets
		Map<String, String> facets = removeUnsupportedFacets(searchQuery
				.getFacets());

		// Construct the URL we are going to use to query XTF
		String searchXTFURL = buildQueryURL(searchQuery.getKeyword(), searchQuery.getFileID(), facets);

		// parse search results into a SearchResultSet
		return parseSearchResults(getDocument(searchXTFURL));

	}

	protected Document getDocument(String url) {

		// Read document from URL and put results in Document.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			return db.parse(url);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	protected String buildQueryURL(String keyword, String fileID, Map<String, String> facets) {

		String xtfURL = Properties.getString("xtfURL");
		String searchXTFURL = xtfURL + "search?raw=1";

		if (keyword != null && keyword.equals("")) {
			searchXTFURL += "&browse-all=yes";
		} else {
			searchXTFURL += "&keyword=" + keyword;
		}

		searchXTFURL += "&fileID=" + fileID;
		
		Iterator<String> facetTypes = facets.keySet().iterator();
		int facetCount = 0;
		while (facetTypes.hasNext()) {
			String facetType = facetTypes.next();
			String facetValue = facets.get(facetType);
			facetCount++;
			searchXTFURL = searchXTFURL + ";f" + facetCount + "-" + facetType
					+ "=" + facetValue;
		}

		System.out.println("request: "+searchXTFURL);
		return searchXTFURL;
	}

	/**
	 * collection facet is not supported by XTF so remove from our query.
	 * 
	 * @param facets
	 * @return
	 */
	protected Map<String, String> removeUnsupportedFacets(
			Map<String, String> facets) {

		// Request XTF keyword search (raw=1 to return XML)
		Hashtable<String, String> xtfFacetQuery = new Hashtable<String, String>();
		xtfFacetQuery.putAll(facets);

		if (xtfFacetQuery.containsKey("collection")) {
			xtfFacetQuery.remove("collection");
		}

		return xtfFacetQuery;
	}

	/**
	 * Parse the XML dom document and put the results into a list of
	 * SearchResult objects.
	 * 
	 * @param dom
	 * @return List of the search results
	 */
	protected SearchResultSet parseSearchResults(Document dom) {

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

		// Add in all the (docHit) results into a Hashtable by Item Number
		NodeList docHits = docEle.getElementsByTagName("docHit");
		Hashtable<String, SearchResult> docHitsByItem = new Hashtable<String, SearchResult>();
		if (docHits != null) {
			for (int i = 0; i < docHits.getLength(); i++) {

				Element node = (Element) docHits.item(i);
				Element meta = (Element) node.getElementsByTagName("meta")
						.item(0);

				Element itemIdElement = (Element) meta.getElementsByTagName(
						"fileID").item(0);

				// Sometimes results may appear without any metadata, ignore
				// these.
				if (itemIdElement != null) {
					String itemId = itemIdElement.getTextContent();
					itemId = itemId.replaceAll("<.*>",""); // remove tags
					itemId = itemId.replaceAll("\\s+",""); // remove whitespace
					
					System.out.println("itemid: "+itemId);
					SearchResult result = docHitsByItem.get(itemId);
					if (result == null) {
						result = new SearchResult(node);
						docHitsByItem.put(itemId, result);
					} else {
						List<String> snippetList = new ArrayList<String>();

						NodeList snippetNodes = node
								.getElementsByTagName("snippet");
												
						Integer startPage=1; // default
						try {
						    startPage = new Integer(meta
								.getElementsByTagName("startPage").item(0)
								.getTextContent());						
						} catch (Exception e) { /* ignore, use default value */}
						
						for (int j = 0; j < snippetNodes.getLength(); j++) {
							Element snippetNode = (Element) snippetNodes
									.item(j);
							if (snippetNode != null) {
								snippetList.add(getValueInHTML(snippetNode));
							}
						}

						if (result != null && result.getId() != null) {
							result.insertSnippets(startPage, snippetList);							
							docHitsByItem.put(itemId, result);
						}
					}
				}
			}

			results = new ArrayList<SearchResult>(docHitsByItem.values());
			// ensure results are in the right order by score.
			if (results.size() > 0) {
				Collections.sort(results);
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

	private String getValueInHTML(Node node) {

		if (node.getNodeType() == Node.TEXT_NODE) {
			// if this is a snippet, bold the matching word(s).
			if (node.getParentNode().getNodeName().equals("term")) {
				return "<b>" + node.getNodeValue().replaceAll("<.*>", "")
						+ "</b>";
			}
			// remove complete and partial tags as much as possible
			String noCompleteTags = node.getNodeValue().replaceAll("<.*>", "");
			return noCompleteTags.replaceAll("<\\w*|\\w*>", "");
		}

		NodeList children = node.getChildNodes();
		StringBuffer textValue = new StringBuffer();
		if (node.getNodeValue() == null && children != null) {

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				textValue.append(getValueInHTML(child));
			}

			return textValue.toString();
		}

		return "";

	}

}
