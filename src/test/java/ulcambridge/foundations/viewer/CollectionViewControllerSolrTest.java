package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.search.CollectionItemsPage;
import ulcambridge.foundations.viewer.search.Search;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Verifies the collection carousel AJAX endpoint sources its tiles from Solr
 * (via {@link Search#getCollectionItems}), returns the {@code {request, items,
 * total}} response shape, and that rendering the page itself makes no Solr query.
 */
public class CollectionViewControllerSolrTest {

    @Test
    public void handleItemsAjaxRequest_sourcesTilesFromSolrAndPreservesShape() throws Exception {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao(), "true", Path.of("cudl-data/"), false, "");
        Search search = mock(Search.class);
        JSONObject item = new JSONObject().put("id", "MS-1").put("title", "T").put("unreleased", true);
        when(search.getCollectionItems(eq("treasures"), anyInt(), anyInt()))
            .thenReturn(new CollectionItemsPage(List.of(item), 1));

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

    @Test
    public void handleItemsAjaxRequest_reportsTheSolrTotalForPagination() throws Exception {
        Search search = mock(Search.class);
        when(search.getCollectionItems(eq("genizah"), anyInt(), anyInt()))
            .thenReturn(new CollectionItemsPage(List.of(new JSONObject().put("id", "MS-1")), 141368));

        String body = organisationController(search).handleItemsAjaxRequest("genizah", 0, 8);

        // Not the 2 ids listed in the collection file: the carousel's tiles come from
        // Solr, so its page count has to come from the same place or trailing pages
        // render empty. It rides on the response that carries the tiles.
        assertEquals(141368, new JSONObject(body).getInt("total"));
    }

    @Test
    public void handleItemsAjaxRequest_reportsZeroTotalWhenSolrUnavailable() throws Exception {
        Search search = mock(Search.class);
        when(search.getCollectionItems(eq("genizah"), anyInt(), anyInt()))
            .thenReturn(CollectionItemsPage.empty());

        JSONObject data = new JSONObject(
            organisationController(search).handleItemsAjaxRequest("genizah", 0, 8));

        // No items to paginate over, so no pages; must not throw or omit the field.
        assertEquals(0, data.getInt("total"));
        assertEquals(0, data.getJSONArray("items").length());
    }

    @Test
    public void handleRequest_rendersTheCollectionPageWithoutQueryingSolr() {
        // The carousel fetches its items and its total together over AJAX, so the
        // render must not query Solr — this is where the count query used to run.
        Search search = mock(Search.class);

        ModelAndView modelAndView = organisationController(search).handleRequest("genizah", 1);

        assertEquals("genizah",
            ((Collection) modelAndView.getModel().get("collection")).getId());
        verifyNoInteractions(search);
    }

    private static final Path TEST_JSON_DIR = Path.of("src/test/resources/cudl-data/");

    private CollectionViewController organisationController(Search search) {
        CollectionFactory collectionFactory = new CollectionFactory(
            new OrganisationCollectionsDao(), "true", TEST_JSON_DIR, false, "");
        return new CollectionViewController(
            collectionFactory, mock(ItemsDao.class), search, "./html", false, "");
    }

    /** An organisation collection whose two item ids both have JSON in test resources. */
    private static class OrganisationCollectionsDao implements CollectionsDao {

        @Override
        public List<String> getCollectionIds() {
            return List.of("genizah");
        }

        @Override
        public Collection getCollection(String collectionId) {
            return new Collection("genizah", "Cairo Genizah",
                new ArrayList<>(List.of("MS-ADD-03958", "MS-ADD-04004")),
                "collections/genizah/summary.html",
                "collections/genizah/sponsors.html",
                "organisation", "", "");
        }
    }
}
