package ulcambridge.foundations.viewer.search;

import java.util.stream.Collectors;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Item;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Profile("!test")
public class SolrSearch implements Search {

    private static final Logger LOG = LoggerFactory.getLogger(SolrSearch.class.getName());

    private static final int SOLR_CONNECT_TIMEOUT_MS = 5_000;
    private static final int SOLR_READ_TIMEOUT_MS = 15_000;

    private final URI searchURL;
    private final URI imageServerURL;
    private final String appendToThumbnail;
    private final BiMap<String, String> displayNameToFacetNameMap = HashBiMap.create();
    private final BiMap<String, String> facetNameToDisplayNameMap;
    private final ArrayList<String> facetNamesInOrder = new ArrayList<>();

    public SolrSearch(@Qualifier("searchURL") URI searchURL,
                      @Qualifier("imageServerURL") URI imageServerURL,
                      @Value("${appendToThumbnail}") String appendToThumbnail,
                      @Value("${showReleaseStatus:false}") boolean showReleaseStatus) {
        Assert.notNull(searchURL, "searchURL is required");
        Assert.notNull(imageServerURL, "imageServerURL is required");
        Assert.notNull(appendToThumbnail, "appendToThumbnail is required");
        this.searchURL = searchURL;
        this.imageServerURL = imageServerURL;
        this.appendToThumbnail = appendToThumbnail;
        this.displayNameToFacetNameMap.put("Collection", "facet-collection");
        this.displayNameToFacetNameMap.put("Subject", "facet-subjects");
        this.displayNameToFacetNameMap.put("Date", "facet-creations-century");
        this.displayNameToFacetNameMap.put("Place","facet-origin-place");
        this.displayNameToFacetNameMap.put("Languages","facet-languages");
        this.displayNameToFacetNameMap.put("Page_Has_Transcription","facet-pageHasTranscription");
        this.displayNameToFacetNameMap.put("Page_Has_Translation","facet-pageHasTranslation");
        if (showReleaseStatus) {
            this.displayNameToFacetNameMap.put("Item_Status", "facet-itemStatus");
        }
        this.facetNameToDisplayNameMap = displayNameToFacetNameMap.inverse();

        this.facetNamesInOrder.add("facet-collection");
        this.facetNamesInOrder.add("facet-subjects");
        this.facetNamesInOrder.add("facet-pageHasTranscription");
        this.facetNamesInOrder.add("facet-pageHasTranslation");
        this.facetNamesInOrder.add("facet-origin-place");
        this.facetNamesInOrder.add("facet-languages");
        this.facetNamesInOrder.add("facet-creations-century");
        if (showReleaseStatus) {
            this.facetNamesInOrder.add("facet-itemStatus");
        }
    }

    /**
     * Returns the 'maxDocs' number of results starting at the first one.
     * Maxdocs is specified inside Solr configuration.
     */
    @Override
    public SearchResultSet makeSearch(final SearchForm searchForm) {
        return makeSearch(searchForm, 1, 1);
    }

    /**
     * Request to Solr API.  Returns a max number of results - currently 20 results, set within Solr.
     */
    @Override
    public SearchResultSet makeSearch(final SearchForm searchForm,
                                      final int start,
                                      final int end) {

        // Construct the URL we are going to use to query Solr
        final String searchSolrURL = buildQueryURL(searchForm, start, end);

        // if the query URL is null return empty result set.
        if (searchSolrURL == null) {
            return new SearchResultSet(0, "", 0f,
                new ArrayList<>(), new ArrayList<>(),
                "A problem occurred making the search (Solr).");
        }

        // parse search results into a SearchResultSet
        return parseSearchResults(getJSON(searchSolrURL));
    }

    @Override
    public Map<String, String> getFacetNameMap() {
        return displayNameToFacetNameMap;
    }

    protected JSONObject getJSON(final String url) {

        InputStream in = null;
        try {
            // Timeouts are explicit because HttpURLConnection defaults to waiting
            // indefinitely: a Solr that accepts the connection then stalls would
            // otherwise hang a page render, and virtual collections query Solr on
            // the render path.
            final URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(SOLR_CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(SOLR_READ_TIMEOUT_MS);
            in = connection.getInputStream();
            return new JSONObject(IOUtils.toString( in , StandardCharsets.UTF_8));
        } catch (IOException e) {
            // error from Solr API - e.g. {"detail":"Query contains too many nested clauses; maxClauseCount is set to 1024"}
            LOG.warn("Solr request failed: {} — {}", url, e.getMessage());
            return null;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected String buildQueryURL(final SearchForm searchForm, final int start, final int end) {
        final UriComponentsBuilder uriB = UriComponentsBuilder.fromUri(this.searchURL.resolve("items"));
        HashMap<String, String> QueryTerms = new HashMap<String, String>();

        uriB.queryParam("start", start);

        // Expand/contract facet
        if (searchForm.getExpandFacet() != null) {
            uriB.queryParam("expand", searchForm.getExpandFacet());
        }

        // Keywords
        if (searchForm.getKeyword() != null) {
            QueryTerms.put("keyword", searchForm.getKeyword());
        }

        if (searchForm.getFullText() != null) {
            QueryTerms.put("textual_content", searchForm.getFullText());
        }

        // Join
        String textJoin = "AND";
        if (searchForm.getTextJoin() != null) {
            if ("or".equals(searchForm.getTextJoin())) {
                textJoin = "OR";
            }
        }

        if (searchForm.getExcludeText() != null) {
            // Form field currently does not appear
            //QueryTerms.put("text-exclude", searchForm.getExcludeText());
        }

        // File ID
//        if (searchForm.getFileID() != null) {
//            uriB.queryParam("fileID", searchForm.getFileID());
//        }

        // Classmark
        if (searchForm.getShelfLocator() != null) {
            // remove all punctuation and run a search-shelfLocator
            // search (for full and partial classmark match)
//            final String sLoc = searchForm.getShelfLocator().replaceAll("\\W+", " ");
            QueryTerms.put("shelfLocator", searchForm.getShelfLocator());
        }

        // Metadata
        if (searchForm.getTitle() != null) {
            QueryTerms.put("title", searchForm.getTitle());
        }

        if (searchForm.getAuthor() != null) {
            QueryTerms.put("name", searchForm.getAuthor());
        }

        if (searchForm.getSubject() != null) {
            QueryTerms.put("subjects", searchForm.getSubject());
        }

        if (searchForm.getLanguage() != null) {
            QueryTerms.put("languageStrings", searchForm.getLanguage());
        }

        if (searchForm.getPlace() != null) {
            QueryTerms.put("origin-place", searchForm.getPlace());
        }

        if (searchForm.getLocation() != null) {
            QueryTerms.put("physicalLocation", searchForm.getLocation());
        }

        if (searchForm.getYearStart() != null) {
            QueryTerms.put("yearStart", Integer.toString(searchForm.getYearStart()));
        }

        if (searchForm.getYearEnd() != null) {
            QueryTerms.put("yearEnd", Integer.toString(searchForm.getYearEnd()));
        }

        if (searchForm.getFacets() != null) {
            for (Map.Entry<String, String> facet : searchForm.getFacets().entrySet()) {
                uriB.queryParam("fq", String.format("%s:\"%s\"", displayNameToFacetNameMap.get(facet.getKey()), facet.getValue()));
            }
        }

        String query = "";

        Map<String, String> QueryTermsFiltered = QueryTerms.entrySet()
            .stream()
            .filter(map -> map.getValue() != "")
            .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));

        Iterator<Map.Entry<String, String>> iterator = QueryTermsFiltered.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue().trim();
            String field_prefix = "";
            if (value != "") {
                String search_clause = "";

                // Get all quote-delimited phrases
                List<String> phrases = new ArrayList<String>();
                Matcher m = Pattern.compile("\"[^\"]*\"")
                    .matcher(value);
                while (m.find()) {
                    phrases.add(m.group());
                }

                // Remove quote-delimited phrases and trim space on param value
                // Process any non-quoted words
                value = value.replaceAll("\"[^\"]*\"", "").trim();
                ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(value.split("\\s+")));
                tokens.removeAll(Arrays.asList("", null));

                ArrayList<String> searchTokens = new ArrayList<String>();
                searchTokens.addAll(phrases);
                searchTokens.addAll(tokens);

                if (searchTokens.size() != 0) {
                    Iterator<String> words = searchTokens.iterator();
                    while (words.hasNext()) {
                        String word = words.next();
                        if (key != "keyword") {
                            field_prefix = key + ":";
                        }
                        search_clause += field_prefix + word;
                        if (words.hasNext()) {
                            if (key == "textual_content") {
                                search_clause += " " + textJoin + " ";
                            } else {
                                search_clause += " AND ";
                            }
                        }
                    }
                    String result = "";
                    if (key == "textual_content") {
                        query += "(" + search_clause + ")";
                    } else {
                        query += search_clause;
                    }
                    if (iterator.hasNext()) {
                        query += " AND ";
                    }
                }
            }
        }
        uriB.queryParam("q", query);

        System.out.println("****** URL: " + uriB.toUriString());
        return uriB.toUriString();
    }

    /**
     * Parse the JSON and put the results into a list of
     * SearchResult objects.
     *
     * @param json
     * @return List of the search results
     */
    protected SearchResultSet parseSearchResults(final JSONObject json) {

        if (json == null) {
            return new SearchResultSet(0, "", 0f,
                new ArrayList<>(), new ArrayList<>(),
                "A problem occurred making the search (solr).");
        }

        // Get the root element
        final ArrayList<SearchResult> results = new ArrayList<>();

        // Catch any errors //TODO
//        if (!"crossQueryResult".equals(docEle.getNodeName())) {
//            return new SearchResultSet(
//                0,
//                "",
//                0f,
//                new ArrayList<>(),
//                new ArrayList<>(),
//                "Too many results, try a smaller range, eliminating wildcards, or making them more specific. ");
//        }

        // Add in all the (docs) results into a Hashtable by Item Number
        final JSONArray docs = json.getJSONObject("response").getJSONArray("docs");

        final JSONObject highlighting = json.optJSONObject("highlighting");

        if (docs != null) {
            for (int i = 0; i < docs.length(); i++) {
                // Isolate each document: a single malformed/unresolvable doc must
                // not abort the whole result set (previously this 500'd the list).
                try {
                    final JSONObject doc = docs.getJSONObject(i);
                    results.add(createSearchResult(doc, highlighting));
                } catch (Exception e) {
                    LOG.warn("Skipping malformed Solr search doc at index {}: {}", i, e.getMessage());
                }
            }

            // ensure results are in the right order by score.
            if (results.size() > 0) {
                Collections.sort(results);
            }
        }

        // Get general search result data
        final int totalDocs = json.getJSONObject("response").getInt("numFound");
        final float queryTime = json.getJSONObject("responseHeader").getFloat("QTime");
//        final Element spelling = null; // TODO
        String suggestedTerm = "";
//        if (spelling != null) {
//            Element suggestion = (Element) spelling.getElementsByTagName(
//                "suggestion").item(0);
//            suggestedTerm = suggestion.getAttribute("suggestedTerm");
//        }

        // facets
        final ArrayList<FacetGroup> facetGroups = new ArrayList<>();

        JSONObject facetFields = json.getJSONObject("facet_counts").getJSONObject("facet_fields");
        for (String facetName: facetNamesInOrder) {

            if (!facetFields.has(facetName)) { continue; }

            JSONArray fields = facetFields.getJSONArray(facetName);
            final ArrayList<Facet> facets = new ArrayList<>();

            final int facetGroupTotalGroups = 0;// TODO
            final int facetGroupOccurrences = 0;// TODO

            // Find out if this is a supported facet, and if not discard:
            String displayName = facetNameToDisplayNameMap.get(facetName);
            if (displayName==null || displayName.isEmpty()) {
                continue;
            }

            for (int i = 0; i < fields.length(); i=i+2) {
                String band = fields.getString(i);
                int band_count = fields.getInt(i+1);
                // Note: Do not show any bands that contain ::
                // e.g. Ignore sub-collections that will be Collection::subcollection
                if (band.contains("::")) {
                    continue;
                }
                final Facet facet = new Facet(displayName, band, band_count, i);
                facets.add(facet);
            }

            final FacetGroup facetGroup = new FacetGroup(displayName, facets, facetGroupOccurrences, facetGroupTotalGroups);
            facetGroups.add(facetGroup);
        }

        return new SearchResultSet(totalDocs, suggestedTerm, queryTime,
            results, facetGroups, "");
    }

    /**
     * Creates a new SearchResult for a matched page from the given Solr document.
     * All display fields are sourced from the Solr doc itself (no filesystem item
     * load); reads are guarded so a doc missing an expected field can't NPE.
     */
    public SearchResult createSearchResult(final JSONObject result, final JSONObject highlighting) {

        int score = 0;
        String itemType = "bookormanuscript"; // default

        String id = firstString(result, "fileID");
        if (id == null) { id = ""; }

        int startPage = 1;
        JSONArray sequence = result.optJSONArray("sequence");
        if (sequence != null && sequence.length() > 0) {
            startPage = sequence.optInt(0, 1);
        }

        String startPageLabel = firstString(result, "label");
        if (startPageLabel == null) { startPageLabel = ""; }

        List<String> snippets = new ArrayList<>();
        String docId = firstString(result, "id");
        JSONObject highlights = (highlighting != null && docId != null)
            ? highlighting.optJSONObject(docId) : null;
        if (highlights != null) {
            for (String key : highlights.keySet()) {
                JSONArray snippetArr = highlights.optJSONArray(key);
                if (snippetArr != null && snippetArr.length() > 0) {
                    snippets.add(snippetArr.getString(0));
                }
            }
        }

        // Search hits are per-page, so the thumbnail and mainDisplay come from the
        // matched page doc (IIIFImageURL / per-page mainDisplay).
        String title = firstString(result, "documentTitle");
        if (title == null) { title = firstString(result, "title"); }
        if (title == null) { title = "Unknown"; }

        String shelfLocator = firstString(result, "documentShelfLocator");
        if (shelfLocator == null) { shelfLocator = ""; }

        String abstractShort = Item.makeShortAbstract(firstAbstract(result));

        String mainDisplay = firstString(result, "mainDisplay");
        if (mainDisplay == null) { mainDisplay = "iiif"; }

        boolean released = isReleased(result);
        String itemStatus = itemStatus(result);

        String thumbnailURL = resolveThumbnail(firstString(result, "IIIFImageURL"));

        String thumbnailOrientation = firstString(result, "thumbnailImageOrientation");
        if (thumbnailOrientation == null) { thumbnailOrientation = "landscape"; }

        return new SearchResult(title, id, startPage, startPageLabel,
            snippets, score, itemType, thumbnailURL, thumbnailOrientation,
            shelfLocator, abstractShort, mainDisplay, released, itemStatus);
    }

    /**
     * Fetches one page of a collection's items directly from Solr, in collection
     * order. Returns one {@code item} JSON object per item-level (cover) doc, ready
     * for the collection carousel client. This replaces the per-item filesystem
     * item load and the whole-collection unreleased scan.
     *
     * <p>Note: this parses only {@code response.docs} and {@code response.numFound}.
     * The collection query returns no {@code highlighting} or {@code facet_counts},
     * so {@link #parseSearchResults} cannot be reused here.
     */
    @Override
    public CollectionItemsPage getCollectionItems(final String slug, final int start, final int rows) {
        JSONObject json = getJSON(collectionItemsURL(slug, start, rows, true));
        if (json == null) {
            // The search API rejects the sorted query outright when the collection has
            // no {slug}_sort field, which is the case for a collection it has never
            // indexed any items for. Retrying unsorted tells that apart from Solr being
            // unreachable: an answer of no items is an empty collection, not an outage.
            LOG.info("Sorted item query failed for collection '{}'; retrying unsorted", slug);
            json = getJSON(collectionItemsURL(slug, start, rows, false));
        }
        if (json == null) { return CollectionItemsPage.empty(); }

        final JSONObject response = json.optJSONObject("response");
        if (response == null) { return CollectionItemsPage.empty(); }

        // numFound is the total for the whole collection, not just this page: it is
        // what the carousel paginates against, and it rides on this same response.
        final int total = response.optInt("numFound", 0);

        final List<JSONObject> items = new ArrayList<>();
        final JSONArray docs = response.optJSONArray("docs");
        for (int i = 0; docs != null && i < docs.length(); i++) {
            try {
                items.add(itemObjectFromSolrDoc(docs.getJSONObject(i), true));
            } catch (Exception e) {
                LOG.warn("Skipping malformed collection Solr doc at index {}: {}", i, e.getMessage());
            }
        }
        return new CollectionItemsPage(items, total);
    }

    /**
     * Query for a collection's item-level docs, in collection order when {@code sorted}.
     * Note the search API rewrites {@code collection_sort} to the collection's own sort
     * field ({@code {slug}_sort}); Solr itself has no {@code collection_sort} field, and
     * the API rejects the query when the collection's field does not exist.
     */
    private String collectionItemsURL(final String slug, final int start, final int rows,
                                      final boolean sorted) {
        final UriComponentsBuilder uriB = UriComponentsBuilder.fromUri(this.searchURL.resolve("items"));
        uriB.queryParam("fq", "collection-slug:" + slug);
        uriB.queryParam("fq", "itemLevel:true");
        if (sorted) {
            uriB.queryParam("sort", "collection_sort asc");
        }
        uriB.queryParam("start", Math.max(0, start));
        uriB.queryParam("rows", Math.max(0, rows));
        return uriB.toUriString();
    }

    /**
     * Builds the per-item {@code item} JSON object (the shape both result-list
     * clients render) from a Solr doc. When {@code itemLevelThumbnail} is true the
     * item-level cover thumbnail ({@code documentThumbnailUrl}) is used; otherwise
     * the matched-page thumbnail ({@code IIIFImageURL}).
     */
    public JSONObject itemObjectFromSolrDoc(final JSONObject doc, final boolean itemLevelThumbnail) {
        final JSONObject item = new JSONObject();

        String id = firstString(doc, "fileID");
        item.put("id", id != null ? id : "");

        String title = firstString(doc, "documentTitle");
        if (title == null) { title = firstString(doc, "title"); }
        item.put("title", title != null ? title : "Unknown");

        String shelfLocator = firstString(doc, "documentShelfLocator");
        item.put("shelfLocator", shelfLocator != null ? shelfLocator : "");

        item.put("abstractShort", Item.makeShortAbstract(firstAbstract(doc)));

        String mainDisplay = firstString(doc, "mainDisplay");
        item.put("mainDisplay", mainDisplay != null ? mainDisplay : "iiif");

        item.put("unreleased", !isReleased(doc));
        item.put("itemStatus", itemStatus(doc));

        String rawThumbnail = itemLevelThumbnail
            ? firstString(doc, "documentThumbnailUrl")
            : firstString(doc, "IIIFImageURL");
        item.put("thumbnailURL", resolveThumbnail(rawThumbnail));

        String orientation = itemLevelThumbnail
            ? firstString(doc, "documentThumbnailOrientation")
            : firstString(doc, "thumbnailImageOrientation");
        item.put("thumbnailOrientation", orientation != null ? orientation : "landscape");

        if (doc.has("authors")) {
            item.put("authors", doc.get("authors"));
        }

        return item;
    }

    /**
     * Resolves a raw Solr image id (e.g. {@code MS-ADD-03958-001-00001}, with no
     * suffix) to a display thumbnail URL, mirroring the item path's
     * {@code {IIIFImageURL}+appendToThumbnail} then {@code imageServerURL.resolve(...)}.
     */
    private String resolveThumbnail(final String rawImageId) {
        if (rawImageId == null || rawImageId.isEmpty()) {
            return "/img/no-thumbnail.jpg";
        }
        return this.imageServerURL.resolve(rawImageId + this.appendToThumbnail).toString();
    }

    /**
     * Reads an item's abstract. {@code documentAbstract} is the item-level field and is
     * preferred; {@code abstract} is the fallback for indexes built before it was added.
     */
    private static String firstAbstract(final JSONObject doc) {
        final String value = firstString(doc, "documentAbstract");
        return value != null ? value : firstString(doc, "abstract");
    }

    /**
     * Reads an item's release flag from {@code isReleased}. Every page doc carries it, and
     * an item-level doc is that item's first page, so it is present on both doc shapes.
     * Only an explicit true counts as released; anything else falls back to unreleased,
     * which badges the result rather than hiding it.
     */
    private static boolean isReleased(final JSONObject doc) {
        return "true".equalsIgnoreCase(firstString(doc, "isReleased"));
    }

    /**
     * Reads an item's release status from {@code itemStatus}. Unrecognised values are
     * passed through: more may be added upstream, and this only reaches badge wording.
     */
    private static String itemStatus(final JSONObject doc) {
        final String status = firstString(doc, "itemStatus");
        return (status == null || status.isBlank()) ? Item.DEFAULT_STATUS : status;
    }

    /**
     * Reads the first value of a Solr field as a String. Solr returns most fields
     * as single-element arrays but some (e.g. {@code id}, {@code isReleased}) as
     * scalars, so both shapes are handled. Returns null when the field is absent
     * or empty.
     */
    private static String firstString(final JSONObject doc, final String key) {
        if (doc == null || !doc.has(key)) { return null; }
        final Object value = doc.opt(key);
        if (value == null || JSONObject.NULL.equals(value)) { return null; }
        if (value instanceof JSONArray) {
            final JSONArray array = (JSONArray) value;
            return array.length() > 0 ? String.valueOf(array.get(0)) : null;
        }
        return String.valueOf(value);
    }
}
