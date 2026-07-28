package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.search.Search;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifies the collection carousel AJAX endpoint, and the virtual collection
 * page's server-side item tiles, both source items from Solr (via
 * {@link Search#getCollectionItems}) instead of loading each item from the
 * filesystem via ItemsDao.
 */
public class CollectionViewControllerSolrTest {

    @Test
    public void handleItemsAjaxRequest_sourcesTilesFromSolrAndPreservesShape() throws Exception {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao(), "true", Path.of("cudl-data/"), false, "");
        Search search = mock(Search.class);
        JSONObject item = new JSONObject().put("id", "MS-1").put("title", "T").put("unreleased", true);
        when(search.getCollectionItems(eq("treasures"), anyInt(), anyInt()))
            .thenReturn(List.of(item));

        CollectionViewController controller = new CollectionViewController(
            collectionFactory, search, "./html", false, "");

        String body = controller.handleItemsAjaxRequest("treasures", 0, 8);
        JSONObject data = new JSONObject(body);

        assertEquals("treasures", data.getJSONObject("request").getString("collectionId"));
        JSONArray items = data.getJSONArray("items");
        assertEquals(1, items.length());
        assertEquals("MS-1", items.getJSONObject(0).getString("id"));
        assertTrue(items.getJSONObject(0).getBoolean("unreleased"));

        // Solr is the item source; the (start, rows) pagination is derived from start/end.
        verify(search).getCollectionItems("treasures", 0, 8);
    }

    @Test
    public void handleRequest_virtualCollectionSourcesItemsFromSolr() throws Exception {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao(), "true", Path.of("cudl-data/"), false, "");
        Search search = mock(Search.class);
        JSONObject item = new JSONObject().put("id", "MS-1").put("title", "T").put("unreleased", true);
        when(search.getCollectionItems(eq("treasures"), anyInt(), anyInt()))
            .thenReturn(List.of(item));

        CollectionViewController controller = new CollectionViewController(
            collectionFactory, search, "./html", false, "");

        ModelAndView modelAndView = controller.handleRequest("treasures", 1);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) modelAndView.getModel().get("items");
        assertEquals(1, items.size());
        assertEquals("MS-1", items.get(0).get("id"));
        assertEquals(Boolean.TRUE, items.get(0).get("unreleased"));

        // Whole virtual collection is fetched in one page (no server-side pagination),
        // sized off the collection's own item count rather than a fixed page size.
        verify(search).getCollectionItems(eq("treasures"), eq(0), anyInt());
    }
}
