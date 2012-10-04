package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test
 */
public class SearchResultSetTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchResultSetTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchResultSetTest.class);
	}

	/**
	 * Tests the SearchResult object
	 */
	public void testSearchResult() {
		
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
		Element node = (Element) docHits.item(0);
		
		XTFSearch xtfSearch = new XTFSearch();		
		SearchResult result = xtfSearch.createSearchResult(node);
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
		
		SearchResultSet r = new SearchResultSet(1, "spellingSuggestedTerm", 2.3f,
				results, facetGroups, "error");
				
		assertEquals(r.getError(), "error");
		assertEquals(r.getSpellingSuggestedTerm(), "spellingSuggestedTerm");
		assertEquals(r.getQueryTime(), 2.3f);
		assertEquals(r.getNumberOfResults(), 1);
		assertEquals(r.getResults().size(), 1);
		assertEquals(r.getFacets().size(), 1);
		
		// Build second facet list
		Facet f3 = new Facet("field2", "band3", 50);
		Facet f4 = new Facet("field2", "band4", 1);

		ArrayList<Facet> facets2 = new ArrayList<Facet>();
		facets2.add(f3);
		facets2.add(f4);
		
		FacetGroup g2 = new FacetGroup("field2", "fieldLabel2", facets2);		
		
		r.addFacetGroup(0, g2);
		assertEquals(r.getFacets().size(),2);
		
		
	}
}
