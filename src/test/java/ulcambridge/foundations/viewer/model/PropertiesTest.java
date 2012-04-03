package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;

/**
 * Unit test for testing properties object
 */
public class PropertiesTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public PropertiesTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PropertiesTest.class);
	}

	/**
	 * Tests the Properties object
	 */
	public void testProperties() {
	
		assertEquals(Properties.getString("GoogleAnalyticsId"), "UA-10976633-2");
		assertEquals(Properties.getString("newton.title"),"Newton Papers");
	}
}