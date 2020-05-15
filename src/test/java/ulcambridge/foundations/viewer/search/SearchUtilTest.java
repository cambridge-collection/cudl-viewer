package ulcambridge.foundations.viewer.search;

import org.junit.Test;
import ulcambridge.foundations.viewer.forms.SearchForm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test
 */
public class SearchUtilTest {
    /**
     * Tests the SearchUtil object
     */
    @Test
    public void testSearchUtil() {

        final SearchForm form = new SearchForm();
        form.setKeyword("keyword");
        form.setFacetSubject("test subject");
        form.setFacetDate("test date");
        form.setFacetLanguage("test language");

        assertTrue(SearchUtil.getURLParameters(form).contains("keyword=keyword"));
        assertTrue(SearchUtil.getURLParameters(form).contains("facetDate=test%20date"));
        assertTrue(SearchUtil.getURLParameters(form).contains("facetSubject=test%20subject"));
        assertTrue(SearchUtil.getURLParameters(form).contains("facetLanguage=test%20language"));

        assertTrue(SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue").contains("facetBob=bobvalue")
        );
        assertTrue(SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue").contains("keyword=keyword")
        );
        assertTrue(SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue").contains("facetDate=test%20date")
        );
        assertTrue(SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue").contains("facetSubject=test%20subject")
        );
        assertTrue(SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue").contains("facetLanguage=test%20language")
        );

        assertTrue(SearchUtil.getURLParametersWithoutFacet(
            form, "subject").contains("keyword=keyword")
        );
        assertTrue(SearchUtil.getURLParametersWithoutFacet(
            form, "subject").contains("facetDate=test%20date")
        );
        assertFalse(SearchUtil.getURLParametersWithoutFacet(
            form, "subject").contains("facetSubject=test%20subject")
        );
        assertFalse(SearchUtil.getURLParametersWithoutFacet(
            form, "language").contains("facetLanguage=test%20language")
        );
    }
}
