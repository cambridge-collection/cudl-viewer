package ulcambridge.foundations.viewer.search;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Unit test
 */
public class SearchResultSetTest{
    /**
     * Tests the SearchResult object
     */
    @Test
    public void testSearchResult() {

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
        Element node = (Element) docHits.item(0);

        XTFSearch xtfSearch = new XTFSearch();
        SearchResult result = xtfSearch.createSearchResult(node);
        ArrayList<SearchResult> results = new ArrayList<SearchResult> ();
        results.add(result);

        // Build facet list
        Facet f = new Facet("field", "band", 3, 1);
        Facet f2 = new Facet("field", "band2", 2, 2);

        ArrayList<Facet> facets = new ArrayList<Facet>();
        facets.add(f);
        facets.add(f2);

        FacetGroup g = new FacetGroup("field", facets, 5);

        ArrayList<FacetGroup> facetGroups = new ArrayList<FacetGroup> ();
        facetGroups.add(g);

        SearchResultSet r = new SearchResultSet(1, "spellingSuggestedTerm", 2.3f,
                results, facetGroups, "error");

        assertEquals("error", r.getError());
        assertEquals("spellingSuggestedTerm", r.getSpellingSuggestedTerm());
        assertEquals(2.3f, r.getQueryTime(), 0.01f);
        assertEquals(1, r.getNumberOfResults());
        assertEquals(1, r.getResults().size());
        assertEquals(1, r.getFacets().size());

        // Build second facet list
        Facet f3 = new Facet("field2", "band3", 50, 3);
        Facet f4 = new Facet("field2", "band4", 1, 4);

        ArrayList<Facet> facets2 = new ArrayList<Facet>();
        facets2.add(f3);
        facets2.add(f4);

        FacetGroup g2 = new FacetGroup("field2", facets2, 51);

        r.addFacetGroup(0, g2);
        assertEquals(2, r.getFacets().size());
    }
}
