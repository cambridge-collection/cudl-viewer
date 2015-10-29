package ulcambridge.foundations.viewer;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.CollectionsMockDao;
import ulcambridge.foundations.viewer.dao.ItemsJSONDao;

import javax.servlet.RequestDispatcher;

public class SiteViewControllerTest extends TestCase {

	public void testHandleRequest() {
		SiteViewController c = new SiteViewController();

		JSONReader reader = new MockJSONReader();
		
		ItemsJSONDao jsondao = new ItemsJSONDao();
		jsondao.setJSONReader(reader);
				
		ItemFactory itemFactory = new ItemFactory();
		itemFactory.setItemsDao(jsondao);	
		
		CollectionFactory collectionFactory = new CollectionFactory();
		CollectionsDao collectionsdao = new CollectionsMockDao();
		collectionFactory.setCollectionsDao(collectionsdao);	

		c.setItemFactory(itemFactory);
		c.setCollectionFactory(collectionFactory);		
		
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

	public void testHandle404() {
		SiteViewController c = new SiteViewController();
		ModelAndView modelAndView = c.handle404();
		assertTrue(modelAndView!=null);
	}

	public void testHandle500() {
		SiteViewController c = new SiteViewController();

		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setAttribute(RequestDispatcher.ERROR_EXCEPTION,
				new RuntimeException("boom"));

		ModelAndView mav = c.handle500(req);

		assertModelAttributeValue(mav, "errorMessage", "boom");
		assertModelAttributeAvailable(mav, "errorTraceback");
	}
}
