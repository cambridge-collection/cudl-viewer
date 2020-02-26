package ulcambridge.foundations.viewer.search;

import org.junit.Test;
import org.w3c.dom.Document;
import ulcambridge.foundations.viewer.forms.SearchForm;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class XTFSearchTest {
    @Test
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

        public TestableXTFSearch() {
            super(URI.create("http://xtf.example.com"));
        }

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
