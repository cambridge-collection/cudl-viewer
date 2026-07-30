package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.search.Search;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    @Test
    public void handleRequest_paginatesOrganisationCarouselAgainstSolrItemCount() {
        Search search = mock(Search.class);
        when(search.getCollectionItemCount("genizah")).thenReturn(141368);

        ModelAndView modelAndView = organisationController(search).handleRequest("genizah", 1);

        // Not the 2 ids listed in the collection file: the carousel's tiles come from
        // Solr, so its page count has to come from the same place or trailing pages
        // render empty.
        assertEquals(141368, modelAndView.getModel().get("collectionSize"));
    }

    @Test
    public void handleRequest_fallsBackToCollectionItemCountWhenSolrUnavailable() {
        Search search = mock(Search.class);
        when(search.getCollectionItemCount("genizah")).thenReturn(-1);

        ModelAndView modelAndView = organisationController(search).handleRequest("genizah", 1);

        assertEquals(2, modelAndView.getModel().get("collectionSize"));
    }

    @Test
    public void handleRequest_doesNotQuerySolrForCollectionTypesWithoutACarousel() {
        // MockCollectionsDao's collection is "virtual" — tiles are rendered server-side.
        Search search = mock(Search.class);
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao(), "true", TEST_JSON_DIR, false, "");
        CollectionViewController controller = new CollectionViewController(
            collectionFactory, mock(ItemsDao.class), search, "./html", false, "");

        ModelAndView modelAndView = controller.handleRequest("treasures", 1);

        assertEquals(1, modelAndView.getModel().get("collectionSize"));
        verify(search, never()).getCollectionItemCount(anyString());
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
