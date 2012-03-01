package ulcambridge.foundations.viewer;

import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
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
import ulcambridge.foundations.viewer.model.SearchResult;
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
			HttpServletResponse response) throws Exception {

		// Request XTF keyword search (raw=1 to return XML)
		Document dom = makeSearch(query);

		// Read XML result into Model
		SearchResultSet results = parseSearchResults(dom);		

		ModelAndView modelAndView = new ModelAndView("jsp/search");
		modelAndView.addObject("results", results);
		return modelAndView;
	}

	// Request XTF keyword search (raw=1 to return XML)
	// Read XML result into Model
	private Document makeSearch(String query) throws MalformedURLException {

		String xtfURL = Properties.getString("xtfURL");

		// Read document from URL and put results in Document.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(xtfURL + "search?raw=1;keyword=" + query);

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
		if (docHits != null && docHits.getLength() > 0) {
			for (int i = 0; i < docHits.getLength(); i++) {

				Node node = docHits.item(i);
				SearchResult result = new SearchResult(node);
				results.add(result);
			}			
		}
		
		NodeList params = docEle.getElementsByTagName("param");
		String query = "";
		if (params != null && params.getLength() > 0) {
			for (int i = 0; i < params.getLength(); i++) {
				Element node = (Element) params.item(i);
						
				query = node.getAttribute("value");
			}
		}
		
		// Get the number of results
		int totalDocs = Integer.parseInt(docEle.getAttribute("totalDocs"));
		float queryTime = Float.parseFloat(docEle.getAttribute("queryTime"));
			
		return new SearchResultSet(totalDocs, queryTime, query, results);
	}

}