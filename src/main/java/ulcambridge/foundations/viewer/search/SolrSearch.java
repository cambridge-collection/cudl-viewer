package ulcambridge.foundations.viewer.search;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.io.IOUtils;
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
    private final DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver;

    public SolrSearch(@Qualifier("searchURL") URI searchURL, @Qualifier("thumbnailImageURLResolver") DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver) {
        Assert.notNull(searchURL, "searchURL is required");
        this.searchURL = searchURL;
        this.displayNameToFacetNameMap.put("Collection", "collection_str");
        this.displayNameToFacetNameMap.put("Subject", "subjects_str");
        this.displayNameToFacetNameMap.put("Date", "creations-century_str");
        this.displayNameToFacetNameMap.put("Place","creations-places_str");
        this.displayNameToFacetNameMap.put("Languages","languageStrings_str");
        this.displayNameToFacetNameMap.put("Page_Has_Transcription","pageHasTranscription");
        this.displayNameToFacetNameMap.put("Page_Has_Translation","pageHasTranslation");
        this.facetNameToDisplayNameMap = displayNameToFacetNameMap.inverse();
        this.thumbnailImageURLResolver = thumbnailImageURLResolver;
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

        uriB.queryParam("start", start);

        // Expand/contract facet
        if (searchForm.getExpandFacet() != null) {
            uriB.queryParam("expand", searchForm.getExpandFacet());
        }

        // Keywords
        if (searchForm.getKeyword() != null) {
            uriB.queryParam("q",searchForm.getKeyword());
        }

//        if (searchForm.getFullText() != null) {
//            uriB.queryParam("text", searchForm.getFullText());
//        }
//        if (searchForm.getExcludeText() != null) {
//            uriB.queryParam("text-exclude", searchForm.getExcludeText());
//        }

        // Join
//        if (searchForm.getTextJoin() != null) {
//            if ("or".equals(searchForm.getTextJoin())) {
//                uriB.queryParam("text-join", "or");
//            } else {
//                uriB.queryParam("text-join", "");
//            }
//        }

        // File ID
//        if (searchForm.getFileID() != null) {
//            uriB.queryParam("fileID", searchForm.getFileID());
//        }

        // Classmark
//        if (searchForm.getShelfLocator() != null) {
//            // remove all punctuation and run a search-shelfLocator
//            // search (for full and partial classmark match)
//            final String sLoc = searchForm.getShelfLocator().replaceAll("\\W+", " ");
//            uriB.queryParam("search-shelfLocator", sLoc);
//        }

        // Metadata
//        if (searchForm.getTitle() != null) {
//            uriB.queryParam("title", searchForm.getTitle());
//        }
//
//        if (searchForm.getAuthor() != null) {
//            uriB.queryParam("nameFullForm", searchForm.getAuthor());
//        }
//
//        if (searchForm.getSubject() != null) {
//            uriB.queryParam("subjectFullForm", searchForm.getSubject());
//        }
//
//        if (searchForm.getLanguage() != null) {
//            uriB.queryParam("languageString", searchForm.getLanguage());
//        }
//
//        if (searchForm.getPlace() != null) {
//            uriB.queryParam("placeFullForm", searchForm.getPlace());
//        }
//
//        if (searchForm.getLocation() != null) {
//            uriB.queryParam("physicalLocation", searchForm.getLocation());
//        }
//
//        if (searchForm.getYearStart() != null) {
//            uriB.queryParam("year", searchForm.getYearStart());
//        }
//
//        if (searchForm.getYearEnd() != null) {
//            uriB.queryParam("year-max", searchForm.getYearEnd());
//        }

        // Facets
//        if (searchForm.getFacetCollection() != null) {
//            uriB.queryParam("fq", String.format("collection_str:\"%s\"", searchForm.getFacetCollection()));
//        }
//
//        if (searchForm.getFacetSubjects() != null) {
//            uriB.queryParam("fq", String.format("subjects_str:\"%s\"", searchForm.getFacetSubjects()));
//        }

//        if (searchForm.getFacetLanguage() != null) {
//            uriB.queryParam(
//                String.format("f%d-language", ++facetCount),
//                searchForm.getFacetLanguage()
//            );
//        }
//
//        if (searchForm.getFacetPlace() != null) {
//            uriB.queryParam(
//                String.format("f%d-place", ++facetCount),
//                searchForm.getFacetPlace()
//            );
//        }
//
//        if (searchForm.getFacetLocation() != null) {
//            uriB.queryParam(
//                String.format("f%d-location", ++facetCount),
//                searchForm.getFacetLocation()
//            );
//        }

        if (searchForm.getFacets() != null) {
            for (Map.Entry<String,String> facet: searchForm.getFacets().entrySet()) {
                uriB.queryParam("fq", String.format("%s:\"%s\"",displayNameToFacetNameMap.get(facet.getKey()),facet.getValue()));
            }
        }

        System.out.println("****** URL: "+uriB.toUriString());
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
        for (String key: facetFields.keySet()) {
            JSONArray fields = facetFields.getJSONArray(key);
            final ArrayList<Facet> facets = new ArrayList<>();

            final int facetGroupTotalGroups = 0;// TODO
            final int facetGroupOccurrences = 0;// TODO

            // Find out if this is a supported facet, and if not discard:
            String displayName = facetNameToDisplayNameMap.get(key);
            if (displayName==null || displayName.isEmpty()) {
                continue;
            }

            for (int i = 0; i < fields.length(); i=i+2) {
                String band = fields.getString(i);
                int band_count = fields.getInt(i+1);
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
