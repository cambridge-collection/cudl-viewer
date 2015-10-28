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

		assertEquals(SearchUtil.getURLParameters(form).contains("keyword=keyword"),true);
		assertEquals(SearchUtil.getURLParameters(form).contains("facetDate=test%20date"),true);
		assertEquals(SearchUtil.getURLParameters(form).contains("facetSubject=test%20subject"),true);
		
		assertEquals(SearchUtil.getURLParametersWithExtraFacet(form, "bob", "bobvalue").contains("facetBob=bobvalue"), true);
		assertEquals(SearchUtil.getURLParametersWithExtraFacet(form, "bob", "bobvalue").contains("keyword=keyword"),true);
		assertEquals(SearchUtil.getURLParametersWithExtraFacet(form, "bob", "bobvalue").contains("facetDate=test%20date"),true);
		assertEquals(SearchUtil.getURLParametersWithExtraFacet(form, "bob", "bobvalue").contains("facetSubject=test%20subject"),true);
		
		assertEquals(SearchUtil.getURLParametersWithoutFacet(form, "subject").contains("keyword=keyword"),true);
		assertEquals(SearchUtil.getURLParametersWithoutFacet(form, "subject").contains("facetDate=test%20date"),true);
		assertEquals(SearchUtil.getURLParametersWithoutFacet(form, "subject").contains("acetSubject=test%20subject"),false);
			
	}
}
