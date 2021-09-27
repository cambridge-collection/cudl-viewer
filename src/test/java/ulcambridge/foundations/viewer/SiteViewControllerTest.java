package ulcambridge.foundations.viewer;

import javax.servlet.RequestDispatcher;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.admin.RefreshCache;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class SiteViewControllerTest {

    private SiteViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao());

        return new SiteViewController(collectionFactory, "./html", null);
    }

    @Test
    public void testHandleRequest() {

        ModelAndView modelAndView = createController().handleRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandleNewsRequest() {
        ModelAndView modelAndView = createController().handleNewsRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandleAboutRequest() {
        ModelAndView modelAndView = createController().handleAboutRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandleHelpRequest() {
        ModelAndView modelAndView = createController().handleHelpRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandleTermsRequest() {
        ModelAndView modelAndView = createController().handleTermsRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandleContributorsRequest() {
        ModelAndView modelAndView = createController().handleContributorsRequest();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandle404() {
        ModelAndView modelAndView = createController().handle404();
        assertTrue(modelAndView!=null);
    }

    @Test
    public void testHandle500() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        Exception e = new RuntimeException("boom");
        req.setAttribute(RequestDispatcher.ERROR_EXCEPTION, e);

        ModelAndView mav = createController().handle500(req);

        assertModelAttributeValue(mav, "exception", e);
    }
}
