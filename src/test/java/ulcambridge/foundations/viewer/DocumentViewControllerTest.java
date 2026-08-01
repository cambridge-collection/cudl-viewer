package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Items;

import java.net.URI;
import java.nio.file.Path;
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
        Optional<Map<String, String>> socialImageDimensions = Optional.empty();

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/view/" + ITEM_ID);
        req.setProtocol(rootUri.getScheme());
        req.setServerName(rootUri.getHost());
        req.setServerPort(rootUri.getPort());

        DocumentViewController c = new DocumentViewController(
            new CollectionFactory(collectionsdao, "true", Path.of("src/test/resources/cudl-data/"), false, ""),
            itemsDao,
            rootUri,
            iiifImageServer,
            downloadSizes,
            socialImageDimensions,
            false
        );

        ModelAndView mDoc = c.handleRequest(ITEM_ID, req);

        assertEquals(ITEM_ID, mDoc.getModelMap().get("docId"));
        assertEquals(0, mDoc.getModelMap().get("page"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("docURL"));
        assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("canonicalURL"));
        assertNotNull(mDoc.getModelMap().get("downloadSizes"));
        assertEquals(new HashMap<>(), mDoc.getModelMap().get("downloadSizes"));
        Assertions.assertNull(mDoc.getModelMap().get("socialImageDimensions"));
    }

    private ModelAndView renderItem(Item item, boolean showUnreleasedContent) {
        when(itemsDao.getItem(ITEM_ID)).thenReturn(item);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/view/" + ITEM_ID);

        DocumentViewController c = new DocumentViewController(
            new CollectionFactory(new MockCollectionsDao(), "true",
                Path.of("src/test/resources/cudl-data/"), false, ""),
            itemsDao,
            URI.create("http://testurl.testingisthebest.com:8080"),
            URI.create("http://images.digital.library.example.com/iiif/"),
            Optional.empty(),
            Optional.empty(),
            showUnreleasedContent
        );

        return c.handleRequest(ITEM_ID, req);
    }

    /**
     * The notice is driven by the item JSON, not by which directory the file was loaded
     * from, so an item with no isReleased is reported unreleased.
     */
    @Test
    public void itemUnreleasedComesFromTheItemJSON() {
        assertEquals(true, renderItem(Items.getExampleItem(ITEM_ID), true)
            .getModelMap().get("itemUnreleased"));
    }

    @Test
    public void releasedItemIsNotReportedUnreleased() {
        JSONObject json = new JSONObject().put("pages",
            new JSONArray().put(new JSONObject().put("isReleased", true)));

        assertEquals(false, renderItem(Items.getExampleItem(ITEM_ID, json), true)
            .getModelMap().get("itemUnreleased"));
    }

    /**
     * The item now loads whatever the flag says, so the flag has to reach the JSP for it
     * to gate the notice.
     */
    @Test
    public void theFlagReachesTheModelSoTheNoticeCanBeGated() {
        assertEquals(true, renderItem(Items.getExampleItem(ITEM_ID), true)
            .getModelMap().get("showUnreleasedContent"));
        assertEquals(false, renderItem(Items.getExampleItem(ITEM_ID), false)
            .getModelMap().get("showUnreleasedContent"));
    }

}
