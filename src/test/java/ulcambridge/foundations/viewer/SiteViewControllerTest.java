package ulcambridge.foundations.viewer;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsMockDao;
import ulcambridge.foundations.viewer.dao.ItemsJSONDao;

import javax.servlet.RequestDispatcher;

import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class SiteViewControllerTest extends TestCase {

    private SiteViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new CollectionsMockDao());

        return new SiteViewController(collectionFactory);
    }

    public void testHandleRequest() {

        ModelAndView modelAndView = createController().handleRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandleNewsRequest() {
        ModelAndView modelAndView = createController().handleNewsRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandleAboutRequest() {
        ModelAndView modelAndView = createController().handleAboutRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandleHelpRequest() {
        ModelAndView modelAndView = createController().handleHelpRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandleTermsRequest() {
        ModelAndView modelAndView = createController().handleTermsRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandleContributorsRequest() {
        ModelAndView modelAndView = createController().handleContributorsRequest();
        assertTrue(modelAndView!=null);
    }

    public void testHandle404() {
        ModelAndView modelAndView = createController().handle404();
        assertTrue(modelAndView!=null);
    }

    public void testHandle500() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setAttribute(RequestDispatcher.ERROR_EXCEPTION,
                new RuntimeException("boom"));

        ModelAndView mav = createController().handle500(req);

        assertModelAttributeValue(mav, "errorMessage", "boom");
        assertModelAttributeAvailable(mav, "errorTraceback");
    }
}
