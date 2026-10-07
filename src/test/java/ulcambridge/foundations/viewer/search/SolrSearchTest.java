package ulcambridge.foundations.viewer.search;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Collection;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        return new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 200);
    }

    /** A SolrSearch whose Solr call is stubbed with a fixed response. */
    private SolrSearch withStubbedResponse(final JSONObject response, final String[] capturedUrl) {
        return new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 200) {
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
            .put("isReleased", true);
    }

    private JSONObject searchResponse(final JSONObject facetFields) {
        JSONObject json = new JSONObject()
            .put("responseHeader", new JSONObject().put("QTime", 12))
            .put("response", new JSONObject().put("numFound", 0).put("docs", new JSONArray()));
        if (facetFields != null) {
            json.put("facet_counts", new JSONObject().put("facet_fields", facetFields));
        }
        return json;
    }

    private SearchForm formExpanding(final String... names) {
        SearchForm form = new SearchForm();
        form.setKeyword("bees");
        form.setExpandFacet(List.of(names));
        return form;
    }

    private String searchUrl(final SearchForm form, final boolean withFacets) {
        String[] url = new String[1];
        withStubbedResponse(searchResponse(new JSONObject()), url).makeSearch(form, 0, 20, withFacets);
        return url[0];
    }

    private String facetSearchUrl(final SearchForm form) {
        String[] url = new String[1];
        withStubbedResponse(searchResponse(new JSONObject()), url).makeFacetSearch(form);
        return url[0];
    }

    private JSONArray facetValues(final List<String> values) {
        JSONArray counts = new JSONArray();
        values.forEach((v) -> counts.put(v).put(1));
        return counts;
    }

    private List<String> numbered(final String prefix, final int count) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            values.add(prefix + i);
        }
        return values;
    }

    private FacetGroup group(final SearchResultSet set, final String field) {
        for (FacetGroup g : set.getFacets()) {
            if (g.getField().equals(field)) { return g; }
        }
        return null;
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
        JSONObject doc = new JSONObject().put("fileID", "MS-EMPTY").put("isReleased", false);
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
            .put("isReleased", false);
        SearchResult r = newSolr().createSearchResult(doc, new JSONObject());

        assertEquals("rti", r.getMainDisplay());
        assertFalse(r.isReleased());
    }

    @Test
    public void createSearchResult_readsReleaseFlagWrappedInArray() {
        // Solr returns most fields as single-element arrays; optBoolean would miss this.
        JSONObject doc = pageHitDoc().put("isReleased", new JSONArray().put(false));
        assertFalse(newSolr().createSearchResult(doc, new JSONObject()).isReleased());
    }

    @Test
    public void createSearchResult_treatsADocWithNoReleaseFieldAsUnreleased() {
        // Every page doc should carry isReleased. If one does not, withhold it rather
        // than presenting unreleased content as released.
        JSONObject doc = pageHitDoc();
        doc.remove("isReleased");

        assertFalse(newSolr().createSearchResult(doc, new JSONObject()).isReleased());
    }

    @Test
    public void createSearchResult_readsItemStatusForTheBadgeWording() {
        JSONObject doc = pageHitDoc().put("itemStatus", new JSONArray().put("released"));
        assertEquals("released", newSolr().createSearchResult(doc, new JSONObject()).getItemStatus());

        assertEquals("embargoed", newSolr()
            .createSearchResult(pageHitDoc().put("itemStatus", "embargoed"), new JSONObject())
            .getItemStatus());
    }

    @Test
    public void createSearchResult_treatsADocWithNoItemStatusAsDraft() {
        assertEquals("draft", newSolr().createSearchResult(pageHitDoc(), new JSONObject()).getItemStatus());
    }

    @Test
    public void createSearchResult_fallsBackToDocumentPrefixedAbstract() {
        // The documentAbstract spelling is set on only a handful of docs but must
        // still be honoured where it is.
        JSONObject doc = pageHitDoc();
        doc.remove("abstract");
        doc.put("documentAbstract", new JSONArray().put("<p>Legacy field.</p>"));
        SearchResult r = newSolr().createSearchResult(doc, new JSONObject());

        assertEquals("Legacy field.", r.getAbstractShort());
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

        SearchResultSet set = newSolr().parseSearchResults(response, Set.of());

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
            .put("isReleased", true);

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
            .put("isReleased", false);
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        List<JSONObject> items = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8).getItems();

        assertTrue(items.get(0).getBoolean("unreleased"));
    }

    @Test
    public void getCollectionItems_carriesItemStatusForTheTileBadge() {
        JSONObject itemDoc = new JSONObject()
            .put("fileID", "MS-ADD-03975")
            .put("isReleased", false)
            .put("itemStatus", new JSONArray().put("draft"));
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        List<JSONObject> items = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8).getItems();

        assertEquals("draft", items.get(0).getString("itemStatus"));
    }

    @Test
    public void getCollectionItems_treatsDocWithNoReleaseFieldAsUnreleased() {
        JSONObject itemDoc = new JSONObject().put("fileID", "MS-ADD-03975");
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 1)
                .put("docs", new JSONArray().put(itemDoc)));

        List<JSONObject> items = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8).getItems();

        assertTrue(items.get(0).getBoolean("unreleased"));
        assertEquals("draft", items.get(0).getString("itemStatus"));
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
        // Distinguishable from a collection that simply has no indexed items, which
        // the server-side rendered virtual pages need in order to report an outage.
        assertFalse(page.isAvailable());
    }

    @Test
    public void getCollectionItems_reportsAnEmptyButAnsweredCollectionAsAvailable() {
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 0).put("docs", new JSONArray()));

        CollectionItemsPage page = withStubbedResponse(response, new String[1])
            .getCollectionItems("newton", 0, 8);

        assertTrue(page.getItems().isEmpty());
        assertTrue(page.isAvailable());
    }

    @Test
    public void getCollectionItems_retriesUnsortedWhenTheCollectionHasNoSortField() {
        // The API rejects the sorted query with 400 when {slug}_sort does not exist,
        // which is how a collection with no indexed items behaves. getJSON returns null
        // for that just as it does for an outage, so the unsorted retry is what tells
        // "empty collection" apart from "Solr is down".
        List<String> urls = new ArrayList<>();
        SolrSearch solr = new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 200) {
            @Override
            protected JSONObject getJSON(String url) {
                urls.add(url);
                if (url.contains("sort")) { return null; }
                return new JSONObject().put("response",
                    new JSONObject().put("numFound", 0).put("docs", new JSONArray()));
            }
        };

        CollectionItemsPage page = solr.getCollectionItems("arthurschnitzler", 0, 20);

        assertEquals(2, urls.size());
        assertTrue(urls.get(0).contains("sort"));
        assertFalse(urls.get(1).contains("sort"));
        // Answered, just empty: no outage to report.
        assertTrue(page.isAvailable());
        assertTrue(page.getItems().isEmpty());
    }

    @Test
    public void getCollectionItems_isUnavailableWhenTheUnsortedRetryAlsoFails() {
        // Both attempts failing is a real outage, not a missing sort field.
        List<String> urls = new ArrayList<>();
        SolrSearch solr = new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 200) {
            @Override
            protected JSONObject getJSON(String url) {
                urls.add(url);
                return null;
            }
        };

        CollectionItemsPage page = solr.getCollectionItems("treasures", 0, 20);

        assertEquals(2, urls.size());
        assertFalse(page.isAvailable());
    }

    @Test
    public void getCollectionItems_appliesTheFilterAndListsFacetsWithChoices() {
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 2).put("docs", new JSONArray()
                .put(pageHitDoc())))
            .put("facet_counts", new JSONObject().put("facet_fields", new JSONObject()
                .put("facet-subjects", new JSONArray().put("Fens -- Maps").put(2).put("Ely -- Maps").put(1))
                .put("facet-creations-century", new JSONArray().put("1800s C.E.").put(3))
                .put("facet-languages", new JSONArray().put("English").put(3))));
        String[] url = new String[1];

        CollectionItemsPage page = withStubbedResponse(response, url).getCollectionItems("maps", 0, 8,
            CollectionFilter.of("fen & ely", "Languages::English"));

        assertTrue(url[0].contains("q=fen%20%26%20ely"), url[0]);
        assertTrue(url[0].contains("fq=facet-languages:%22English%22"), url[0]);
        assertTrue(url[0].contains("collection_sort"), url[0]);
        // Subject has a choice; Date has one value so is left out; Languages has one
        // value but is the selected facet, so stays.
        List<String> names = new ArrayList<>();
        page.getFacets().forEach((f) -> names.add(f.getString("name")));
        assertEquals(List.of("Subject", "Languages"), names);
        assertEquals(2, page.getFacets().get(0).getJSONArray("values").getJSONObject(0).getInt("count"));
        assertEquals(1, page.getItems().size());
    }

    @Test
    public void getCollectionItems_escapesQuotesInFacetValues() {
        String[] url = new String[1];
        withStubbedResponse(new JSONObject().put("response", new JSONObject().put("numFound", 0)), url)
            .getCollectionItems("maps", 0, 8, CollectionFilter.of("", "Subject::A \"quoted\" value"));

        assertTrue(url[0].contains("facet-subjects:%22A%20%5C%22quoted%5C%22%20value%22"), url[0]);
    }

    @Test
    public void getCollectionItems_unfilteredReturnsNoFacets() {
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 0))
            .put("facet_counts", new JSONObject().put("facet_fields", new JSONObject()
                .put("facet-subjects", new JSONArray().put("A").put(1).put("B").put(1))));

        assertTrue(withStubbedResponse(response, new String[1])
            .getCollectionItems("maps", 0, 8).getFacets().isEmpty());
    }

    @Test
    public void getCollectionItems_limitsFacetValuesWhenWantedAndSkipsThemOtherwise() {
        List<String> urls = new ArrayList<>();
        SolrSearch solr = new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 200) {
            @Override
            protected JSONObject getJSON(String url) {
                urls.add(url);
                return null;
            }
        };

        solr.getCollectionItems("maps", 0, 8, CollectionFilter.none());
        solr.getCollectionItems("maps", 0, 8);

        // Sorted call and unsorted retry for each request
        assertEquals(4, urls.size());
        assertTrue(urls.get(0).contains("facet.limit=200"), urls.get(0));
        assertTrue(urls.get(1).contains("facet.limit=200"), urls.get(1));
        assertFalse(urls.get(0).contains("facet=false"), urls.get(0));
        assertFalse(urls.get(2).contains("facet.limit"), urls.get(2));
        assertTrue(urls.get(2).contains("facet=false"), urls.get(2));
        assertTrue(urls.get(3).contains("facet=false"), urls.get(3));
    }

    @Test
    public void getCollectionItems_listsEveryFacetValueSolrReturns() {
        JSONArray subjects = new JSONArray();
        for (int i = 0; i < 250; i++) {
            subjects.put("Subject " + i).put(1);
        }
        JSONObject response = new JSONObject()
            .put("response", new JSONObject().put("numFound", 250))
            .put("facet_counts", new JSONObject().put("facet_fields", new JSONObject()
                .put("facet-subjects", subjects)));

        JSONArray values = withStubbedResponse(response, new String[1])
            .getCollectionItems("maps", 0, 8, CollectionFilter.none())
            .getFacets().get(0).getJSONArray("values");

        assertEquals(250, values.length());
        assertEquals("Subject 249", values.getJSONObject(249).getString("value"));
    }

    @Test
    public void makeSearch_asksSolrForOneMoreFacetValueThanItShows() {
        String url = searchUrl(new SearchForm(), true);

        assertTrue(url.contains("facet.limit=201"), url);
        assertFalse(url.contains("expand"), url);
        assertFalse(url.contains("rows="), url);
        assertFalse(url.contains("facet=false"), url);
    }

    @Test
    public void makeSearch_usesTheConfiguredFacetLimit() {
        String[] url = new String[1];
        SolrSearch solr = new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 10) {
            @Override
            protected JSONObject getJSON(String requested) {
                url[0] = requested;
                return searchResponse(new JSONObject()
                    .put("facet-subjects", facetValues(numbered("Subject ", 11))));
            }
        };

        FacetGroup subjects = group(solr.makeSearch(new SearchForm(), 0, 20), "Subject");

        assertTrue(url[0].contains("facet.limit=11"), url[0]);
        assertEquals(10, subjects.getFacets().size());
        assertTrue(subjects.hasMore());
    }

    @Test
    public void constructor_rejectsAFacetLimitBelowOne() {
        assertThrows(IllegalArgumentException.class,
            () -> new SolrSearch(SEARCH_URL, IMAGE_URL, APPEND, false, 0));
    }

    @Test
    public void makeSearch_ignoresExpandFacet() {
        String url = searchUrl(formExpanding("Subject"), true);

        assertTrue(url.contains("facet.limit=201"), url);
        assertFalse(url.contains("rows="), url);
        assertFalse(url.contains("f.facet-subjects"), url);
    }

    @Test
    public void makeFacetSearch_asksForTheExpandedFacetsFullListAndNoResults() {
        String url = facetSearchUrl(formExpanding("Subject"));

        assertTrue(url.contains("rows=0"), url);
        assertTrue(url.contains("facet.limit=201"), url);
        assertTrue(url.contains("f.facet-subjects.facet.limit=-1"), url);
        assertFalse(url.contains("f.facet-origin-place"), url);
        assertFalse(url.contains("expand"), url);
    }

    @Test
    public void makeFacetSearch_expandsEachKnownFacetAndIgnoresUnknownNames() {
        String url = facetSearchUrl(formExpanding("Subject", "Place", "Nonsense"));

        assertTrue(url.contains("rows=0"), url);
        assertTrue(url.contains("f.facet-subjects.facet.limit=-1"), url);
        assertTrue(url.contains("f.facet-origin-place.facet.limit=-1"), url);
        assertEquals(2, StringUtils.countOccurrencesOf(url, ".facet.limit=-1"), url);
    }

    @Test
    public void makeFacetSearch_keepsEveryValueOfTheExpandedFacets() {
        SolrSearch solr = withStubbedResponse(searchResponse(new JSONObject()
            .put("facet-subjects", facetValues(numbered("Subject ", 500)))), new String[1]);

        assertEquals(500, group(solr.makeFacetSearch(formExpanding("Subject")), "Subject").getFacets().size());
        assertEquals(200, group(solr.makeSearch(formExpanding("Subject"), 0, 20), "Subject").getFacets().size());
    }

    @Test
    public void makeSearch_pageChangesAskSolrNotToCountFacets() {
        String url = searchUrl(new SearchForm(), false);

        assertTrue(url.contains("facet=false"), url);
    }

    @Test
    public void parseSearchResults_keepsTheFirst200ValuesAndSaysThereAreMore() {
        SearchResultSet set = newSolr().parseSearchResults(searchResponse(new JSONObject()
            .put("facet-subjects", facetValues(numbered("Subject ", 201)))
            .put("facet-origin-place", facetValues(numbered("Place ", 200)))), Set.of());

        FacetGroup subjects = group(set, "Subject");
        assertEquals(200, subjects.getFacets().size());
        assertEquals("Subject 199", subjects.getFacets().get(199).getBand());
        assertTrue(subjects.hasMore());

        FacetGroup places = group(set, "Place");
        assertEquals(200, places.getFacets().size());
        assertFalse(places.hasMore());
    }

    @Test
    public void parseSearchResults_keepsEveryValueOfAnExpandedFacet() {
        SearchResultSet set = newSolr().parseSearchResults(searchResponse(new JSONObject()
            .put("facet-subjects", facetValues(numbered("Subject ", 500)))
            .put("facet-origin-place", facetValues(numbered("Place ", 201)))),
            Set.of("facet-subjects"));

        assertEquals(500, group(set, "Subject").getFacets().size());
        assertFalse(group(set, "Subject").hasMore());
        assertEquals(200, group(set, "Place").getFacets().size());
        assertTrue(group(set, "Place").hasMore());
    }

    @Test
    public void parseSearchResults_trimsAfterSkippingSubCollectionValues() {
        List<String> values = new ArrayList<>(List.of("A::sub", "B::sub"));
        values.addAll(numbered("Collection ", 199));

        FacetGroup collections = group(newSolr().parseSearchResults(searchResponse(new JSONObject()
            .put("facet-collection", facetValues(values))), Set.of()), "Collection");

        assertEquals(199, collections.getFacets().size());
        assertEquals("Collection 0", collections.getFacets().get(0).getBand());
        // 201 values came back, so there are more even though 2 were skipped
        assertTrue(collections.hasMore());
    }

    @Test
    public void parseSearchResults_skipsSubCollectionValuesInEveryField() {
        FacetGroup places = group(newSolr().parseSearchResults(searchResponse(new JSONObject()
            .put("facet-origin-place", facetValues(List.of("Cambridge", "England::Cambridge")))),
            Set.of()), "Place");

        assertEquals(1, places.getFacets().size());
        assertEquals("Cambridge", places.getFacets().get(0).getBand());
    }

    @Test
    public void parseSearchResults_returnsNoFacetGroupsWhenSolrCountedNone() {
        assertTrue(newSolr().parseSearchResults(searchResponse(null), Set.of()).getFacets().isEmpty());
    }

    private JSONObject collectionDoc(final String id, final String title,
                                     final boolean released, final String status) {
        return new JSONObject()
            .put("id", id)
            .put("name.full", new JSONArray().put(title))
            .put("isReleased", released)
            .put("status", status);
    }

    private JSONObject collectionsResponse(final JSONObject... docs) {
        return new JSONObject().put("response", new JSONObject()
            .put("numFound", docs.length).put("docs", new JSONArray(List.of(docs))));
    }

    private List<Collection> topLevelCollections(final JSONObject response) {
        return withStubbedResponse(response, new String[1]).getTopLevelCollections();
    }

    @Test
    public void getTopLevelCollections_mapsDocToCollection() {
        Collection c = topLevelCollections(collectionsResponse(
            collectionDoc("baskerville", "Baskerville Books and Archives", false, "draft"))).get(0);

        assertEquals("baskerville", c.getId());
        assertEquals("Baskerville Books and Archives", c.getTitle());
        assertEquals("/collections/baskerville", c.getURL());
        assertTrue(c.isUnreleased());
        assertEquals("draft", c.getStatus());
    }

    @Test
    public void getTopLevelCollections_keepsResponseOrder() {
        List<Collection> collections = topLevelCollections(collectionsResponse(
            collectionDoc("zeta", "Zeta", true, "released"),
            collectionDoc("alpha", "Alpha", true, "released"),
            collectionDoc("mu", "Mu", true, "released")));

        assertEquals(List.of("zeta", "alpha", "mu"),
            collections.stream().map(Collection::getId).collect(Collectors.toList()));
        assertFalse(collections.get(0).isUnreleased());
    }

    @Test
    public void getTopLevelCollections_requestsTopLevelOnly() {
        String[] url = new String[1];
        withStubbedResponse(collectionsResponse(collectionDoc("a", "A", true, "released")), url)
            .getTopLevelCollections();

        assertEquals("http://search.example.com/collections?topLevel=true", url[0]);
    }

    @Test
    public void getTopLevelCollections_emptyWhenRequestFails() {
        assertTrue(topLevelCollections(null).isEmpty());
    }

    @Test
    public void getTopLevelCollections_emptyWhenNoDocs() {
        assertTrue(topLevelCollections(new JSONObject()).isEmpty());
        assertTrue(topLevelCollections(new JSONObject().put("response", new JSONObject())).isEmpty());
    }

    @Test
    public void getTopLevelCollections_emptyWhenDocsEmpty() {
        assertTrue(topLevelCollections(collectionsResponse()).isEmpty());
    }

    @Test
    public void getTopLevelCollections_emptyWhenAnyDocLacksARequiredField() {
        for (String field : List.of("id", "name.full", "isReleased", "status")) {
            JSONObject bad = collectionDoc("b", "B", true, "released");
            bad.remove(field);

            assertTrue(topLevelCollections(collectionsResponse(
                collectionDoc("a", "A", true, "released"), bad,
                collectionDoc("c", "C", true, "released"))).isEmpty(), field);
        }
    }
}
