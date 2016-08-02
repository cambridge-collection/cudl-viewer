package ulcambridge.foundations.viewer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.CollectionsMockDao;
import ulcambridge.foundations.viewer.dao.ItemsJSONDao;

import java.net.URI;

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
    public void testDocumentViewController() throws Throwable {

        JSONReader reader = new MockJSONReader();

        ItemsJSONDao jsondao = new ItemsJSONDao(reader);

        CollectionsDao collectionsdao = new CollectionsMockDao();

        ItemFactory itemFactory = new ItemFactory(jsondao);

        URI rootUri = URI.create("http://testurl.testingisthebest.com:8080");

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/view/MS-ADD-04004");
        req.setProtocol(rootUri.getScheme());
        req.setServerName(rootUri.getHost());
        req.setServerPort(rootUri.getPort());

        DocumentViewController c = new DocumentViewController(
            new CollectionFactory(collectionsdao), collectionsdao, itemFactory,
            URI.create("http://testurl.testingisthebest.com:8080"));

        ModelAndView mDoc = c.handleRequest("MS-ADD-04004", req);

        assertEquals("MS-ADD-04004", mDoc.getModelMap().get("docId"));
        assertEquals(0, mDoc.getModelMap().get("page"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("docURL"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("canonicalURL"));
    }

}
