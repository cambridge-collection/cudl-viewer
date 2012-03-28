package ulcambridge.foundations.viewer.forms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test
 */
public class FeedbackFormTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public FeedbackFormTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(FeedbackFormTest.class);
	}

	/**
	 * Tests the FeedbackForm object
	 */
	public void testFeedbackForm() {
		
		FeedbackForm f = new FeedbackForm();
		f.setComment("comment");
		f.setEmail("email");
		f.setName("name");
		
		assertEquals(f.getComment(),"comment");
		assertEquals(f.getEmail(),"email");
		assertEquals(f.getName(),"name");
	}
}
