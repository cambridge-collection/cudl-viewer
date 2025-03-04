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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Profile("!test")
public class SolrSearch implements Search {

    private static final Logger LOG = LoggerFactory.getLogger(SolrSearch.class.getName());

    private final URI searchURL;
    private final BiMap<String, String> displayNameToFacetNameMap = HashBiMap.create();
    private final BiMap<String, String> facetNameToDisplayNameMap;
    private final ArrayList<String> facetNamesInOrder = new ArrayList<>();
    private final DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver;

    public SolrSearch(@Qualifier("searchURL") URI searchURL, @Qualifier("thumbnailImageURLResolver") DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver) {
        Assert.notNull(searchURL, "searchURL is required");
        this.searchURL = searchURL;
        this.displayNameToFacetNameMap.put("Collection", "facet-collection");
        this.displayNameToFacetNameMap.put("Subject", "facet-subjects");
        this.displayNameToFacetNameMap.put("Date", "facet-creations-century");
        this.displayNameToFacetNameMap.put("Place","facet-origin-place");
        this.displayNameToFacetNameMap.put("Languages","facet-languages");
        this.displayNameToFacetNameMap.put("Page_Has_Transcription","facet-pageHasTranscription");
        this.displayNameToFacetNameMap.put("Page_Has_Translation","facet-pageHasTranslation");
        this.facetNameToDisplayNameMap = displayNameToFacetNameMap.inverse();
        this.thumbnailImageURLResolver = thumbnailImageURLResolver;

        this.facetNamesInOrder.add("facet-collection");
        this.facetNamesInOrder.add("facet-subjects");
        this.facetNamesInOrder.add("facet-pageHasTranscription");
        this.facetNamesInOrder.add("facet-pageHasTranslation");
        this.facetNamesInOrder.add("facet-origin-place");
        this.facetNamesInOrder.add("facet-languages");
        this.facetNamesInOrder.add("facet-creations-century");
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
            in = new URL( url ).openStream();
            return new JSONObject(IOUtils.toString( in , StandardCharsets.UTF_8));
        } catch (IOException e) {
            // error from Solr API - e.g. {"detail":"Query contains too many nested clauses; maxClauseCount is set to 1024"}
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

        if (docs != null) {
            for (int i = 0; i < docs.length(); i++) {
                final JSONObject doc = docs.getJSONObject(i);
                results.add(createSearchResult(doc, json.getJSONObject("highlighting")));
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
     * Creates a new SearchResult from the given JSON result
     */
    public SearchResult createSearchResult(final JSONObject result, final JSONObject highlighting) {


        int score = 0;
        String itemType = "bookormanuscript"; // default

        // Build a title from available data:
        String title = "Unknown";
        String id = result.getString("fileID");

        int startPage = result.getJSONArray("sequence").getInt(0);
        String startPageLabel = result.getJSONArray("label").getString(0);

        List<String> snippets = new ArrayList<>();
        JSONObject highlights = highlighting.getJSONObject(result.getString("id"));
        for (String key: highlights.keySet()) {
            snippets.add(highlights.getJSONArray(key).getString(0));
        }

        String thumbnailURL = "/img/no-thumbnail.jpg";
        if (result.has("IIIFImageURL")) {
            thumbnailURL = result.getJSONArray("IIIFImageURL").getString(0);
        }
        String thumbnailOrientation = "landscape";
        if (result.has("thumbnailImageOrientation")) {
            thumbnailOrientation = result.getJSONArray("thumbnailImageOrientation").getString(0);
        }

        return new SearchResult(title, id, startPage, startPageLabel,
            snippets, score, itemType, thumbnailURL, thumbnailOrientation);

    }
}
