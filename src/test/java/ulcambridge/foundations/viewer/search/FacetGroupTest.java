package ulcambridge.foundations.viewer.search;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test
 */
public class FacetGroupTest {
    /**
     * Tests the Facet Group object
     */
    @Test
    public void testFacetGroup() {

        // Note a facet group should contain all the same field
        Facet f = new Facet("field", "band", 3, 1);
        Facet f2 = new Facet("field", "band2", 2, 2);

        ArrayList<Facet> facets = new ArrayList<Facet>();
        facets.add(f);
        facets.add(f2);

        FacetGroup g = new FacetGroup("field", facets, 5, 12);

        assertEquals(2, g.getFacets().size());
        assertTrue(g.getFacets().contains(f));
        assertTrue(g.getFacets().contains(f2));
        assertEquals("field", g.getField());
        assertEquals(2, g.getBands().size());
        assertEquals(f2, g.getFacetWithBand("band2"));
        assertEquals("Field", g.getFieldLabel());
        assertEquals(2, g.getNumBands());
        assertEquals(12, g.getTotalGroups());
    }
}
