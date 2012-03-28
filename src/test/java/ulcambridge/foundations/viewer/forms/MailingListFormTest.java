package ulcambridge.foundations.viewer.forms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test 
 */
public class MailingListFormTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public MailingListFormTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MailingListFormTest.class);
	}

	/**
	 * Tests the MailingListForm object
	 */
	public void testMailingListForm() {
		
		MailingListForm f = new MailingListForm();

		f.setEmail("email");
		f.setName("name");
		
		assertEquals(f.getEmail(),"email");
		assertEquals(f.getName(),"name");
	}
}
