package ulcambridge.foundations.viewer.search;

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
public class SearchResultTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchResultTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchResultTest.class);
	}

	/**
	 * Tests the SearchResultSet object
	 */
	public void testSearchResultSet() {
		
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

		assertEquals(result.getId(), "MS-ADD-04004");
		assertEquals(result.getTitle(), "Newton's Waste Book");		
		assertEquals(result.getFacets().size(), 2);	
		assertEquals(result.getSnippets().size(), 1);	
	}
}
