package ulcambridge.foundations.viewer.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ulcambridge.foundations.viewer.forms.SearchForm;

/**
 * Unit test
 */
public class SearchUtilTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchUtilTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchUtilTest.class);
	}

	/**
	 * Tests the SearchUtil object
	 */
	public void testSearchUtil() {
		
		SearchForm form = new SearchForm();
		form.setKeyword("keyword");
		form.setFacetSubject("test subject");
		form.setFacetDate("test date");

		assertEquals(SearchUtil.getURLParameters(form), "keyword=keyword&amp;fileID=&amp;facetDate=test+date&amp;facetSubject=test+subject");
		assertEquals(SearchUtil.getURLParametersWithExtraFacet(form, "bob", "bobvalue"), "keyword=keyword&amp;fileID=&amp;facetDate=test+date&amp;facetSubject=test+subject&amp;facetBob=bobvalue");
		assertEquals(SearchUtil.getURLParametersWithoutFacet(form, "subject"), "keyword=keyword&amp;fileID=&amp;facetDate=test+date");

	}
}
