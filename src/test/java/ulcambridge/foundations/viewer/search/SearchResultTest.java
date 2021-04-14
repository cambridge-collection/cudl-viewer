package ulcambridge.foundations.viewer.search;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.common.truth.Truth;
import java.net.URI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Unit test
 */
public class SearchResultTest {
    /**
     * Tests the SearchResultSet object
     */
    @Test
    public void testSearchResultSet() {

        // Read document from File
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Element dom;
        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("src/test/resources/Results.xml").getDocumentElement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Get first result
        final NodeList docHits = dom.getElementsByTagName("docHit");
        final Element node = (Element) docHits.item(0);

        final XTFSearch xtfSearch = new XTFSearch(URI.create("http://xtf.example.com"));
        final SearchResult result = xtfSearch.createSearchResult(node);

        assertEquals("MS-ADD-04004", result.getFileId());
        assertEquals("Newton's Waste Book", result.getTitle());
        assertEquals(1, result.getStartPage());
        assertEquals("front cover", result.getStartPageLabel());
        assertEquals(1, result.getSnippets().size());
    }

    private static SearchResult newSearchResult(String title) {
        return new SearchResult(title, "", 0, "", ImmutableList.of(), -1, "", null, null);
    }

    @Test
    public void testEqualsIsTrueForDistinctMatchingObjects() {
        Truth.assertThat(newSearchResult("foo")).isEqualTo(newSearchResult("foo"));
    }

    @Test
    public void testEqualsIsFalseForDistinctNonMatchingObjects() {
        Truth.assertThat(newSearchResult("foo")).isNotEqualTo(newSearchResult("bar"));
    }

    @Test
    public void testHashCodeIsEqualForMatchingObjects() {
        Truth.assertThat(newSearchResult("foo").hashCode())
            .isEqualTo(newSearchResult("foo").hashCode());
    }

    @Test
    public void testHashCodeIsNotEqualForNonMatchingObjects() {
        Truth.assertThat(newSearchResult("foo").hashCode())
            .isNotEqualTo(newSearchResult("bar").hashCode());
    }
}
