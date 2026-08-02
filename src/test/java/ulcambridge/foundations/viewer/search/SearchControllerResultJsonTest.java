package ulcambridge.foundations.viewer.search;

import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies that {@code /search/JSON} builds each result tile purely from the
 * Solr-sourced {@link SearchResult} (no {@code itemDAO}), with the expected shape
 * and {@code unreleased} flag, and that one bad result cannot abort the batch.
 */
public class SearchControllerResultJsonTest {

    private SearchResult result(String fileId, boolean released, String mainDisplay) {
        return new SearchResult(
            "Title " + fileId, fileId, 3, "3r",
            ImmutableList.of("a <b>snippet</b>"), 0, "bookormanuscript",
            "http://img/" + fileId + ".jp2/full/!180,180/0/default.jpg", "portrait",
            "Shelf " + fileId, "Short abstract", mainDisplay, released,
            released ? "released" : "draft");
    }

    private SearchController controller(SearchResultSet resultSet) {
        Search search = mock(Search.class);
        when(search.makeSearch(any(SearchForm.class), anyInt(), anyInt())).thenReturn(resultSet);
        return new SearchController(mock(CollectionFactory.class), search, "./html");
    }

    private SearchResultSet resultSetOf(List<SearchResult> results) {
        return new SearchResultSet(results.size(), "", 1f, results, new ArrayList<>(), "");
    }

    @Test
    public void searchJson_buildsItemShapeFromSolrResult() throws Exception {
        List<SearchResult> results = new ArrayList<>();
        results.add(result("MS-A", true, "iiif"));
        results.add(result("MS-B", false, "rti"));

        ResponseEntity<String> response =
            controller(resultSetOf(results)).handleItemsAjaxRequest(new SearchForm(), new SearchController.Range());

        JSONArray items = new JSONArray(response.getBody());
        assertEquals(2, items.length());

        JSONObject first = items.getJSONObject(0);
        JSONObject item = first.getJSONObject("item");
        assertEquals("MS-A", item.getString("id"));
        assertEquals("Title MS-A", item.getString("title"));
        assertEquals("Shelf MS-A", item.getString("shelfLocator"));
        assertEquals("Short abstract", item.getString("abstractShort"));
        assertEquals("iiif", item.getString("mainDisplay"));
        assertFalse(item.getBoolean("unreleased"));
        assertEquals("released", item.getString("itemStatus"));
        assertEquals(3, first.getInt("startPage"));
        assertEquals("3r", first.getString("startPageLabel"));
        assertEquals("http://img/MS-A.jp2/full/!180,180/0/default.jpg", first.getString("pageThumbnailURL"));

        // Second result is unreleased -> badged.
        JSONObject second = items.getJSONObject(1).getJSONObject("item");
        assertTrue(second.getBoolean("unreleased"));
        assertEquals("draft", second.getString("itemStatus"));
    }

    @Test
    public void searchJson_oneBadResultDoesNotAbortBatch() throws Exception {
        // A SearchResult with null snippets would make copyOf fail at construction,
        // so instead simulate a result-level failure via a subclass that throws.
        List<SearchResult> results = new ArrayList<>();
        results.add(result("MS-GOOD-1", true, "iiif"));
        results.add(new SearchResult(
            "boom", "MS-BAD", 1, "1r", ImmutableList.of(), 0, "bookormanuscript",
            null, null, null, null, null, true, "released") {
            @Override
            public String getTitle() {
                throw new RuntimeException("simulated bad result");
            }
        });
        results.add(result("MS-GOOD-2", true, "iiif"));

        ResponseEntity<String> response =
            controller(resultSetOf(results)).handleItemsAjaxRequest(new SearchForm(), new SearchController.Range());

        JSONArray items = new JSONArray(response.getBody());
        // The bad result is skipped; the two good results still render.
        assertEquals(2, items.length());
    }
}
