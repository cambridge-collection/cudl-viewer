package ulcambridge.foundations.viewer.search;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

import ulcambridge.foundations.viewer.forms.SearchForm;

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
		
		SearchForm form = new SearchForm();
		form.setKeyword("keyword");
		form.setFacetSubject("test subject");
		form.setFacetDate("test date");
		form.setFacetCollection("test collection");

		XTFSearch s = new TestableXTFSearch();
		SearchResultSet r = s.makeSearch(form);

		// Expect one result
		assertEquals(1, r.getNumberOfResults());
		assertEquals(1, r.getResults().size());
		assertEquals("", r.getSpellingSuggestedTerm());
		assertEquals("", r.getError());
		assertEquals("MS-ADD-04004", r.getResults().get(0).getFileId());
		assertEquals(1, r.getResults().get(0).getSnippets().size());

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
