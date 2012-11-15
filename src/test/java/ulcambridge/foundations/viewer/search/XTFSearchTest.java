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
		
		SearchQuery q = new SearchQuery("keyword", "", facetMap);

		XTFSearch s = new TestableXTFSearch();
		SearchResultSet r = s.makeSearch(q);

		// Expect one result
		assertEquals(1, r.getNumberOfResults());
		assertEquals(1, r.getResults().size());
		assertEquals("", r.getSpellingSuggestedTerm());
		assertEquals(0.018f, r.getQueryTime());
		assertEquals("", r.getError());
		assertEquals("MS-ADD-04004", r.getResults().get(0).getId());
		
		// Snippets is a Hashtable of pagenumber  -> List<String> snippets. 
		assertEquals(1, r.getResults().get(0).getDocHits().get(0).getStartPage());
		assertEquals(1, r.getResults().get(0).getDocHits().size());

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
