package ulcambridge.foundations.viewer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Items;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit test for simple App.
 */
@ExtendWith(MockitoExtension.class)
public class DocumentViewControllerTest {

    private static final String ITEM_ID = "MS-ADD-04004";

    @Mock ItemsDao itemsDao;

    /**
     * test the class DocumentViewController
     */
    @Test
    public void testDocumentViewController() {
        when(itemsDao.getItem(ITEM_ID)).thenReturn(Items.getExampleItem(ITEM_ID));
        CollectionsDao collectionsdao = new MockCollectionsDao();

        URI rootUri = URI.create("http://testurl.testingisthebest.com:8080");
        URI iiifImageServer = URI.create("http://images.digital.library.example.com/iiif/");
        Optional<Map<String, String>> downloadSizes = Optional.empty();

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/view/" + ITEM_ID);
        req.setProtocol(rootUri.getScheme());
        req.setServerName(rootUri.getHost());
        req.setServerPort(rootUri.getPort());

        DocumentViewController c = new DocumentViewController(
            new CollectionFactory(collectionsdao, "true"),
            itemsDao,
            rootUri,
            iiifImageServer,
            downloadSizes
        );

        ModelAndView mDoc = c.handleRequest(ITEM_ID, req);

        assertEquals(ITEM_ID, mDoc.getModelMap().get("docId"));
        assertEquals(0, mDoc.getModelMap().get("page"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("docURL"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("canonicalURL"));
        assertNotNull(mDoc.getModelMap().get("downloadSizes"));
        assertEquals(new HashMap<>(), mDoc.getModelMap().get("downloadSizes"));
    }

}
