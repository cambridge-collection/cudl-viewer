package ulcambridge.foundations.viewer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

/**
 * Unit test for simple App.
 */
public class DocumentViewControllerTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public DocumentViewControllerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(DocumentViewControllerTest.class);
	}

	/**
	 * test the class DocumentViewController
	 */
	public void testDocumentViewController() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setRequestURI("http://testurl.testingisthebest.com/view/MS-ADD-4004");
		MockServletContext context = new MockServletContext();

		DocumentViewController c = new DocumentViewController();
		//ModelAndView mDoc = c.handleRequest("MS-ADD-4004", req, context);
		//ModelAndView mPage = c.handleRequest("MS-ADD-4004", "3", req, context);
/*
		assertEquals(mDoc.getModelMap().get("docId"), "MS-ADD-4004");
		assertEquals(mDoc.getModelMap().get("page"), 1);
		assertEquals(mDoc.getModelMap().get("docURL"),
				"http://cudl.lib.cam.ac.uk/view/MS-ADD-4004");
		assertEquals(mDoc.getModelMap().get("proxuURL"), "");*/
	}
}
