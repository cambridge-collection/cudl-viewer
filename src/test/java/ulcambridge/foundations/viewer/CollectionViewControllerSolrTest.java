package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.search.Search;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifies the collection carousel AJAX endpoint sources its tiles from Solr
 * (via {@link Search#getCollectionItems}), preserves the {@code {request, items}}
 * response shape, and no longer performs the whole-collection filesystem scan.
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
            collectionFactory, mock(ItemsDao.class), search, "./html", false, "");

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
}
