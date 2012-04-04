package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Unit test
 * 
 */
public class SearchControllerTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchControllerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchControllerTest.class);
	}

	/**
	 * Tests the SearchController object
	 */
	public void testSearchController() {

		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter("keyword", "Elementary Mathematics");
		req.addParameter("facet-collection", "Newton Papers");
		req.addParameter("facet-subject", "Algebra - Early works to 1800");
		
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		SearchController c = new SearchController(new MockSearch());
		ModelAndView m = null;
		try {
			m = c.processSearch(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SearchQuery q = (SearchQuery) m.getModelMap().get("query");
		SearchResultSet r = (SearchResultSet) m.getModelMap().get("results");
		
		// Expect one result 
		assertEquals(r.getNumberOfResults(), 1);
		assertEquals(r.getResults().size(), 1);
		assertEquals(r.getSpellingSuggestedTerm(), "spellingSuggestedTerm");
		assertEquals(r.getFacets().size(), 3);
		assertEquals(r.getQueryTime(), 2.3f);
		assertEquals(r.getError(), "error");
		assertEquals(q.getKeyword(), "Elementary Mathematics");
		assertEquals(q.getFacets().size(), 2);
		assertEquals(q.getURLParameters(), "keyword=Elementary+Mathematics&amp;facet-subject=Algebra+-+Early+works+to+1800&amp;facet-collection=Newton+Papers");
		assertEquals(q.getURLParametersWithExtraFacet("bob", "bobvalue"), "keyword=Elementary+Mathematics&amp;facet-subject=Algebra+-+Early+works+to+1800&amp;facet-collection=Newton+Papers&amp;facet-bob=bobvalue");
		assertEquals(q.getURLParametersWithoutFacet("subject"), "keyword=Elementary+Mathematics&amp;facet-collection=Newton+Papers");
	}
	
	/**
	 * Always returns the same result set to any query. Uses
	 * Results.xml instead of connecting to a real search service.
	 * 
	 * @author jennie
	 * 
	 */
	private class MockSearch implements Search {

		@Override
		public SearchResultSet makeSearch(SearchQuery searchQuery) {
			
			// Read document from File
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Element dom = null;
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();

				dom = (Element) db.parse("src/test/resources/Results.xml").getDocumentElement();;

			} catch (Exception e) {
				e.printStackTrace();

			}
			
			// Get first result
			NodeList docHits = dom.getElementsByTagName("docHit");
			Node node = docHits.item(0);
			
			SearchResult result = new SearchResult(node);
			ArrayList<SearchResult> results = new ArrayList<SearchResult> ();
			results.add(result);
			
			// Build facet list
			Facet f = new Facet("field", "band");
			Facet f2 = new Facet("field", "band2", 10);

			ArrayList<Facet> facets = new ArrayList<Facet>();
			facets.add(f);
			facets.add(f2);	
			
			FacetGroup g = new FacetGroup("field", "fieldLabel", facets);
		
			ArrayList<FacetGroup> facetGroups = new ArrayList<FacetGroup> ();
			facetGroups.add(g);
			
			return new SearchResultSet(1, "spellingSuggestedTerm", 2.3f,
					results, facetGroups, "error");
		}

	}	
}
