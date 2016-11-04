package ulcambridge.foundations.viewer.search;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;

/**
 * Unit test
 */
public class SearchResultTest{
    /**
     * Tests the SearchResultSet object
     */
    @Test
    public void testSearchResultSet() {

        // Read document from File
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Element dom = null;
        try {

            final DocumentBuilder db = dbf.newDocumentBuilder();

            dom = db.parse("src/test/resources/Results.xml").getDocumentElement();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException (e);
        }

        // Get first result
        final NodeList docHits = dom.getElementsByTagName("docHit");
        final Element node = (Element) docHits.item(0);

        final XTFSearch xtfSearch = new XTFSearch();
        final SearchResult result = xtfSearch.createSearchResult(node);

        assertEquals("MS-ADD-04004", result.getFileId());
        assertEquals("Newton's Waste Book", result.getTitle());
        assertEquals(1, result.getStartPage());
        assertEquals("front cover", result.getStartPageLabel());
        assertEquals(1, result.getSnippets().size());
    }
}
