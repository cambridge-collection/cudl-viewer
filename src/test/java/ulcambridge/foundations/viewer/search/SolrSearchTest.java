package ulcambridge.foundations.viewer.search;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Solr-sourced result-list logic: per-page search results,
 * the item-level collection mapper, field guards, malformed-doc isolation and
 * the docs-only collection query.
 */
public class SolrSearchTest {

    private static final URI SEARCH_URL = URI.create("http://search.example.com/");
    private static final URI IMAGE_URL = URI.create("http://images.example.com/iiif/");
    private static final String APPEND = ".jp2/full/!180,180/0/default.jpg";

    private SolrSearch newSolr() {
        return new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false);
    }

    /** A SolrSearch whose Solr call is stubbed with a fixed response. */
    private SolrSearch withStubbedResponse(final JSONObject response, final String[] capturedUrl) {
        return new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false) {
            @Override
            protected JSONObject getJSON(String url) {
                capturedUrl[0] = url;
                return response;
            }
        };
    }

    private JSONObject pageHitDoc() {
        return new JSONObject()
            .put("fileID", "MS-ADD-03958")
            .put("id", "MS-ADD-03958-5")
            .put("sequence", new JSONArray().put(5))
            .put("label", new JSONArray().put("3r"))
            .put("documentTitle", new JSONArray().put("Early Papers"))
            .put("documentShelfLocator", new JSONArray().put("MS Add. 3958"))
            .put("abstract", new JSONArray().put("<p>A gathering of notes.</p>"))
            .put("IIIFImageURL", new JSONArray().put("MS-ADD-03958-000-00005"))
            .put("thumbnailImageOrientation", new JSONArray().put("portrait"))
            .put("itemReleased", true);
    }

    @Test
    public void createSearchResult_mapsAllTileFieldsFromSolrDoc() {
        SearchResult r = newSolr().createSearchResult(pageHitDoc(), new JSONObject());

        assertEquals("MS-ADD-03958", r.getFileId());
        assertEquals("Early Papers", r.getTitle());
        assertEquals("MS Add. 3958", r.getShelfLocator());
        assertEquals("A gathering of notes.", r.getAbstractShort());
        assertEquals(5, r.getStartPage());
        assertEquals("3r", r.getStartPageLabel());
        assertEquals("portrait", r.getThumbnailOrientation());
        assertTrue(r.isReleased());
        // Raw image id resolves against the image server + appendToThumbnail suffix.
        assertEquals(
            "http://images.example.com/iiif/MS-ADD-03958-000-00005.jp2/full/!180,180/0/default.jpg",
            r.getThumbnailURL());
    }

    @Test
    public void createSearchResult_guardsMissingFields() {
        // Only a fileID present; everything else must fall back safely, not NPE.
        JSONObject doc = new JSONObject().put("fileID", "MS-EMPTY").put("itemReleased", false);
        SearchResult r = newSolr().createSearchResult(doc, new JSONObject());

        assertEquals("MS-EMPTY", r.getFileId());
        assertEquals("Unknown", r.getTitle());
        assertEquals("", r.getShelfLocator());
        assertEquals("", r.getAbstractShort());
        assertEquals(1, r.getStartPage());
        assertEquals("", r.getStartPageLabel());
        assertEquals("iiif", r.getMainDisplay());
        assertEquals("landscape", r.getThumbnailOrientation());
        assertEquals("/img/no-thumbnail.jpg", r.getThumbnailURL());
        assertFalse(r.isReleased());
    }

    @Test
    public void createSearchResult_readsPerPageMainDisplayAndReleaseFlag() {
        JSONObject doc = pageHitDoc()
            .put("mainDisplay", new JSONArray().put("rti"))
            .put("itemReleased", false);
        SearchResult r = newSolr().createSearchResult(doc, new JSONObject());

        assertEquals("rti", r.getMainDisplay());
        assertFalse(r.isReleased());
    }

    @Test
    public void createSearchResult_readsReleaseFlagWrappedInArray() {
        // Solr returns most fields as single-element arrays; optBoolean would miss this.
        JSONObject doc = pageHitDoc().put("itemReleased", new JSONArray().put(false));
        assertFalse(newSolr().createSearchResult(doc, new JSONObject()).isReleased());
    }

    @Test
    public void createSearchResult_fallsBackToDocumentPrefixedFields() {
        // The documentAbstract/isReleased spellings are set on only a handful of docs
        // but must still be honoured where they are.
        JSONObject doc = pageHitDoc();
        doc.remove("abstract");
        doc.remove("itemReleased");
        doc.put("documentAbstract", new JSONArray().put("<p>Legacy field.</p>"))
           .put("isReleased", false);
        SearchResult r = newSolr().createSearchResult(doc, new JSONObject());

        assertEquals("Legacy field.", r.getAbstractShort());
        assertFalse(r.isReleased());
    }

    @Test
    public void parseSearchResults_skipsMalformedDocWithoutAbortingBatch() {
        JSONArray docs = new JSONArray();
        docs.put(pageHitDoc());
        docs.put("this is not a document object"); // malformed entry
        docs.put(pageHitDoc().put("fileID", "MS-ADD-99999"));

        JSONObject response = new JSONObject()
            .put("responseHeader", new JSONObject().put("QTime", 12))
            .put("response", new JSONObject().put("numFound", 3).put("docs", docs))
            .put("highlighting", new JSONObject())
            .put("facet_counts", new JSONObject().put("facet_fields", new JSONObject()));

        SearchResultSet set = newSolr().parseSearchResults(response);

        // Two good docs survive; the malformed one is skipped, not fatal.
        assertEquals(2, set.getResults().size());
    }

    @Test
    public void getCollectionItems_buildsItemLevelQueryAndMapsItems() {
        JSONObject itemDoc = new JSONObject()
            .put("fileID", "MS-CHI-BONES-CUL-00297")
            .put("documentTitle", new JSONArray().put("Oracle Bones"))
            .put("documentShelfLocator", new JSONArray().put("CUL297"))
            .put("abstract", new JSONArray().put("<p>Ancient inscribed bones.</p>"))
            .put("documentThumbnailUrl", new JSONArray().put("MS-CHI-BONES-CUL-00297-000-00001"))
            .put("documentThumbnailOrientation", new JSONArray().put("portrait"))
            .put("mainDisplay", new JSONArray().put("rti"))
            .put("itemReleased", true);

        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        String[] capturedUrl = new String[1];
        List<JSONObject> items = withStubbedResponse(response, capturedUrl)
            .getCollectionItems("newton", 8, 8).getItems();

        // The query targets item-level docs in collection order, paginated.
        assertTrue(capturedUrl[0].contains("collection-slug:newton"));
        assertTrue(capturedUrl[0].contains("itemLevel:true"));
        assertTrue(capturedUrl[0].contains("collection_sort"));
        assertTrue(capturedUrl[0].contains("start=8"));
        assertTrue(capturedUrl[0].contains("rows=8"));

        assertEquals(1, items.size());
        JSONObject item = items.get(0);
        assertEquals("MS-CHI-BONES-CUL-00297", item.getString("id"));
        assertEquals("Oracle Bones", item.getString("title"));
        assertEquals("CUL297", item.getString("shelfLocator"));
        assertEquals("Ancient inscribed bones.", item.getString("abstractShort"));
        assertEquals("portrait", item.getString("thumbnailOrientation"));
        assertEquals("rti", item.getString("mainDisplay"));
        assertFalse(item.getBoolean("unreleased"));
        // Item-level thumbnail resolved from documentThumbnailUrl.
        assertEquals(
            "http://images.example.com/iiif/MS-CHI-BONES-CUL-00297-000-00001.jp2/full/!180,180/0/default.jpg",
            item.getString("thumbnailURL"));
    }

    @Test
    public void getCollectionItems_marksUnreleasedItems() {
        JSONObject itemDoc = new JSONObject()
            .put("fileID", "MS-ADD-03975")
            .put("documentTitle", new JSONArray().put("Unreleased item"))
            .put("itemReleased", false);
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        List<JSONObject> items = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8).getItems();

        assertTrue(items.get(0).getBoolean("unreleased"));
    }

    @Test
    public void getCollectionItems_treatsDocWithNoReleaseFieldAsReleased() {
        JSONObject itemDoc = new JSONObject().put("fileID", "MS-ADD-03975");
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        List<JSONObject> items = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8).getItems();

        assertFalse(items.get(0).getBoolean("unreleased"));
    }

    @Test
    public void getCollectionItems_reportsWholeCollectionTotalAlongsideThePage() {
        // numFound is the collection total, not the page size: one page of 8 out of
        // 141368. The carousel paginates against it, so it must survive the mapping.
        JSONArray docs = new JSONArray();
        for (int i = 0; i < 8; i++) {
            docs.put(new JSONObject().put("fileID", "MS-ADD-0000" + i));
        }
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 141368).put("docs", docs));

        CollectionItemsPage page = withStubbedResponse(response, new String[1])
            .getCollectionItems("genizah", 0, 8);

        assertEquals(141368, page.getTotal());
        assertEquals(8, page.getItems().size());
    }

    @Test
    public void getCollectionItems_returnsEmptyPageWhenSolrUnavailable() {
        // getJSON returns null on IO error; must not throw. A zero total means the
        // client renders no pagination rather than paginating over nothing.
        CollectionItemsPage page = withStubbedResponse(null, new String[1])
            .getCollectionItems("newton", 0, 8);

        assertTrue(page.getItems().isEmpty());
        assertEquals(0, page.getTotal());
    }
}
