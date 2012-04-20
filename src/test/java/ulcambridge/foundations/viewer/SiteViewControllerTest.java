package ulcambridge.foundations.viewer;

import org.springframework.web.servlet.ModelAndView;

import junit.framework.TestCase;

public class SiteViewControllerTest extends TestCase {

	public void testHandleRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleNewsRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleNewsRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleAboutRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleAboutRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleHelpRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleHelpRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleTermsRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleTermsRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleContributorsRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleContributorsRequest();
		assertTrue(modelAndView!=null);
	}

	public void testHandleNoJavascriptRequest() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handleNoJavascriptRequest("testurl");
		assertEquals(modelAndView.getModelMap().get("requestURL"), "testurl");
	}

	public void testHandle404() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handle404();
		assertTrue(modelAndView!=null);
	}

	public void testHandle500() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handle500();
		assertTrue(modelAndView!=null);
	}

}
