package ulcambridge.foundations.viewer.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test
 */
public class FacetTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public FacetTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(FacetTest.class);
	}

	/**
	 * Tests the Facet object
	 */
	public void testFacet() {

		Facet f = new Facet("field", "band", 3, 1);
		assertEquals(f.getBand(), "band");
		assertEquals(f.getField(), "field");
		assertEquals(f.getFieldLabel(), "Field");
		assertEquals(f.getOccurences(), 3);
		assertEquals(f.getRank(), 1);

		Facet f2 = new Facet("field2", "band2", 2, 2);
		assertEquals(f2.getOccurences(), 2);
		assertEquals(f.equals(f2), false);
		assertEquals(f.equals(f), true);
		assertEquals(f.compareTo(f2)<0, true);
		assertEquals(f.compareTo(f)==0, true);
		assertEquals(f2.compareTo(f)>0, true);
	}
}
