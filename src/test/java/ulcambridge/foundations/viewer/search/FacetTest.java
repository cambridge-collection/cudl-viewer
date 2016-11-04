package ulcambridge.foundations.viewer.search;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test
 */
public class FacetTest{

    /**
     * Tests the Facet object
     */
    @Test
    public void testFacet() {

        Facet f = new Facet("field", "band", 3, 1);
        assertEquals("band", f.getBand());
        assertEquals("field", f.getField());
        assertEquals("Field", f.getFieldLabel());
        assertEquals(3, f.getOccurences());
        assertEquals(1, f.getRank());

        Facet f2 = new Facet("field2", "band2", 2, 2);
        assertEquals(2, f2.getOccurences());
        assertFalse(f.equals(f2));
        assertTrue(f.equals(f));
        assertTrue(f.compareTo(f2)<0);
        assertTrue(f.compareTo(f)==0);
        assertTrue(f2.compareTo(f)>0);
    }
}
