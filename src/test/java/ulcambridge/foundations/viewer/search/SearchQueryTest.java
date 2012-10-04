package ulcambridge.foundations.viewer.search;

import java.util.HashMap;
import ulcambridge.foundations.viewer.search.SearchQuery; 

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test
 */
public class SearchQueryTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchQueryTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchQueryTest.class);
	}

	/**
	 * Tests the SearchQuery object
	 */
	public void testSearchQuery() {

		HashMap<String, String> facetMap = new HashMap<String, String>();
		facetMap.put("subject", "test subject");
		facetMap.put("date", "test date");
		
		SearchQuery q = new SearchQuery("keyword", "", facetMap);

		assertEquals(q.getKeyword(), "keyword");
		assertEquals(q.getKeywordDisplay(), "keyword");
		assertEquals(q.getFacets().size(), 2);
		assertEquals(q.getFacets(), facetMap);
		assertEquals(q.getURLParameters(), "keyword=keyword&amp;facet-subject=test+subject&amp;facet-date=test+date");
		assertEquals(q.getURLParametersWithExtraFacet("bob", "bobvalue"), "keyword=keyword&amp;facet-subject=test+subject&amp;facet-date=test+date&amp;facet-bob=bobvalue");
		assertEquals(q.getURLParametersWithoutFacet("subject"), "keyword=keyword&amp;facet-date=test+date");

	}
}
