package ulcambridge.foundations.viewer.search;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

/**
 * Unit test
 */
public class XTFSearchTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public XTFSearchTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(XTFSearchTest.class);
	}

	/**
	 * Tests the SearchController object
	 */
	public void testSearchController() {

		HashMap<String, String> facetMap = new HashMap<String, String>();
		facetMap.put("subject", "test subject");
		facetMap.put("date", "test date");		
		facetMap.put("collection", "test collection");	
		
		SearchQuery q = new SearchQuery("keyword", facetMap);

		XTFSearch s = new TestableXTFSearch();
		SearchResultSet r = s.makeSearch(q);

		// Expect one result
		assertEquals(r.getNumberOfResults(), 1);
		assertEquals(r.getResults().size(), 1);
		assertEquals(r.getSpellingSuggestedTerm(), "");
		assertEquals(r.getQueryTime(), 0.018f);
		assertEquals(r.getError(), "");
		assertEquals(r.getResults().get(0).getId(), "MS-ADD-04004");
		assertEquals(r.getResults().get(0).getSnippets().keys().nextElement().toString(), "MS-ADD-04004");
		assertEquals(r.getResults().get(0).getSnippets().size(), 1);

	}

	/**
	 * Always returns the same document as a result to any query. Uses
	 * Results.xml instead of connecting to a real XTF instance.
	 * 
	 * @author jennie
	 * 
	 */
	private class TestableXTFSearch extends XTFSearch {

		@Override
		protected Document getDocument(String url) {

			// Read document from URL and put results in Document.
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				return db.parse("src/test/resources/Results.xml");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
