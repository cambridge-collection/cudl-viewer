package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test
 */
public class FacetGroupTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public FacetGroupTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(FacetGroupTest.class);
	}

	/**
	 * Tests the Facet Group object
	 */
	public void testFacetGroup() {

		// Note a facet group should contain all the same field
		Facet f = new Facet("field", "band");
		Facet f2 = new Facet("field", "band2", 10);

		ArrayList<Facet> facets = new ArrayList<Facet>();
		facets.add(f);
		facets.add(f2);
		
		FacetGroup g = new FacetGroup("field", "fieldLabel", facets);
		
		assertEquals(g.getFacets().size(), 2);
		assertEquals(g.getFacets().contains(f), true);
		assertEquals(g.getFacets().contains(f2), true);
		assertEquals(g.getField(), "field");
		assertEquals(g.getBands().size(), 2);
		assertEquals(g.getFacetWithBand("band2"), f2);
		assertEquals(g.getFieldLabel(), "fieldLabel");
		assertEquals(g.getNumBands(), 2);
		
		Facet f3 = new Facet("field", "band3", 5);
		try {
			g.add(f3);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		assertEquals(g.getFacets().size(), 3);
		
		// Test rejection
		Facet f4 = new Facet("field_different", "band3", 5);
		try {
			g.add(f4);
		} catch (Exception e) { 
			// expecting exception
		}
		assertEquals(g.getFacets().size(), 3);
		
	}
}
