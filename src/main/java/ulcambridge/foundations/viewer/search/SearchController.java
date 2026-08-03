package ulcambridge.foundations.viewer.search;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Collection;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/search")
@Validated
public class SearchController {
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final Search search;
    private final CollectionFactory collectionFactory;
    private final String contentHtmlUrl;

    /**
     * @param search to use for queries. e.g. SolrSearch
     */
    @Autowired
    public SearchController(CollectionFactory collectionFactory,
                            Search search,
                            @Value("${cudl-viewer-content.html.path}") String contentHtmlPath) {

        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(search, "search is required");
        Assert.notNull(contentHtmlPath, "cudl-viewer-content.html.path is required");

        this.collectionFactory = collectionFactory;
        this.search = search;
        this.contentHtmlUrl = Paths.get(contentHtmlPath).toUri().toString();
    }

    // on /search path
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView processSearch(@Valid SearchForm searchForm,
        @RequestParam(required=false) Map<String,String> qparams) {

        // if empty URL, forward to search query form
        if (qparams.isEmpty()) {
            return new ModelAndView("forward:/search/query");
        }

        // No search here: the client's single /search/JSONAdvanced call renders the page.
        ModelAndView modelAndView = new ModelAndView("jsp/search-results");
        modelAndView.addObject("form", searchForm);
        modelAndView.addObject("queryString",
            SearchUtil.getURLParameters(searchForm));
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);

        return modelAndView;
    }

    /**
     * Advanced Search Query Page
     *
     * /search/advanced/query
     */
    @RequestMapping(method = RequestMethod.GET, value = "/query")
    public ModelAndView advancedSearch(
            @Valid @ModelAttribute SearchForm searchForm,
            @RequestParam(value="tagging", required=false, defaultValue="false") boolean enableTagging) {

        ModelAndView modelAndView = new ModelAndView("jsp/search-advanced");
        List<Collection> collectionList = collectionFactory.getRootCollections();
        Collections.sort(collectionList, collectionTitleComparator);
         // order alphabetically by title
        searchForm.setCollections(collectionList);
        modelAndView.addObject("form", searchForm);
        modelAndView.addObject("enableTagging", enableTagging);
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    /**
     * Advanced Search Results Page Performs search and displays results.
     *
     * on /search/advanced/results path
     */
    @RequestMapping(method = RequestMethod.GET, value = "/advanced/results")
    public ModelAndView processAdvancedSearch(
            @ModelAttribute @Valid SearchForm searchForm,
            @RequestParam(value="tagging", required=false, defaultValue="false") boolean enableTagging) {

        // No search here: the client's single /search/JSONAdvanced call renders the page.
        return new ModelAndView("jsp/search-advancedresults")
                .addObject("form", searchForm)
                .addObject("queryString",
                        SearchUtil.getURLParameters(searchForm))
                .addObject("enableTagging", enableTagging)
                .addObject("contentHTMLURL", contentHtmlUrl);
    }

    private JSONArray getResultsJSON(SearchResultSet results) {
        Assert.notNull(results);

        // Put chosen search results into an array.
        JSONArray jsonArray = new JSONArray();

        for(SearchResult searchResult : results.getResults()) {
            // Isolate each result: a single bad result must not abort the whole
            // batch (this is the regression that used to 500 the /search/JSON list).
            try {
                jsonArray.add(getResultItemJSON(searchResult));
            } catch (Exception e) {
                LOG.warn("Skipping search result '{}': {}",
                    searchResult != null ? searchResult.getFileId() : null, e.getMessage());
            }
        }

        return jsonArray;
    }

    /**
     * Builds the per-result JSON entirely from the Solr-sourced {@link SearchResult}
     * (no filesystem item load). The nested {@code item} object keeps the shape the
     * client already renders, plus the {@code unreleased} flag for badging.
     */
    private JSONObject getResultItemJSON(SearchResult searchResult) {

        JSONObject item = new JSONObject();
        item.put("id", searchResult.getFileId());
        item.put("title", searchResult.getTitle());
        item.put("shelfLocator", searchResult.getShelfLocator());
        item.put("abstractShort", searchResult.getAbstractShort());
        item.put("mainDisplay", searchResult.getMainDisplay());
        item.put("unreleased", !searchResult.isReleased());
        item.put("itemStatus", searchResult.getItemStatus());

        JSONObject itemJSON = new JSONObject();
        itemJSON.put("item", item);
        itemJSON.put("startPage", searchResult.getStartPage());
        itemJSON.put("startPageLabel", searchResult.getStartPageLabel());
        itemJSON.put("itemType", searchResult.getType());

        // Make an array for the snippets.
        JSONArray resultsArray = new JSONArray();

        for (String snippet : searchResult.getSnippets()) {
            resultsArray.add(snippet.trim());
        }

        itemJSON.put("snippets", resultsArray);

        // The matched-page thumbnail was already resolved from Solr's IIIFImageURL.
        itemJSON.put("pageThumbnailURL", searchResult.getThumbnailURL());

        return itemJSON;
    }

    private JSONObject getFacetJson(Facet facet) {
        JSONObject o = new JSONObject();

        o.put("value", facet.getBand());
        o.put("occurrences", facet.getOccurrences());

        return o;
    }

    private JSONArray getGroupFacetsJSON(FacetGroup group) {
        JSONArray a = new JSONArray();

        for(Facet f : group.getFacets()) {
            a.add(getFacetJson(f));
        }

        return a;
    }

    private JSONObject getFacetGroupJSON(FacetGroup group) {
        JSONObject o = new JSONObject();

        o.put("label", group.getFieldLabel());
        o.put("field", group.getField());
        o.put("totalFacets", group.getTotalGroups());
        o.put("facets", getGroupFacetsJSON(group));

        return o;
    }

    private JSONArray getAvailableFacetsJSON(SearchResultSet results, SearchForm form) {
        JSONArray a = new JSONArray();

        for(FacetGroup facetGroup : results.getFacets()) {
            if(form.getFacets().containsKey(facetGroup.getField()))
                continue;

            // Solr returns some faceted fields as empty arrays; dropping the group
            // stops a consumer rendering a heading over an empty list.
            if(facetGroup.getFacets().isEmpty())
                continue;

            a.add(getFacetGroupJSON(facetGroup));
        }

        return a;
    }

    private JSONArray getSelectedFacetsJSON(SearchForm form) {
        JSONArray a = new JSONArray();

        for(Map.Entry<String, String> selectedFacet : form.getFacets().entrySet()) {
            JSONObject o = new JSONObject();
            o.put("field", selectedFacet.getKey());
            o.put("value", selectedFacet.getValue());
            a.add(o);
        }

        return a;
    }

    private JSONObject getFacetsJSON(SearchResultSet results, SearchForm form) {
        JSONObject o = new JSONObject();

        o.put("selected", getSelectedFacetsJSON(form));
        o.put("available", getAvailableFacetsJSON(results, form));

        return o;
    }

    private JSONObject getInfoJSON(SearchResultSet results) {
        JSONObject o = new JSONObject();

        o.put("hits", results.getNumberOfResults());
        o.put("queryTime", results.getQueryTime());

        // An unreachable Solr yields 0 hits and an error rather than a failed request,
        // so the client needs this to tell an outage from an empty result set.
        o.put("error", results.getError() == null ? "" : results.getError());

        return o;
    }

    /** The info block lets a page turn report its own query time and spot an outage. */
    private JSONObject getItemsJSON(SearchResultSet results) {
        JSONObject o = new JSONObject();

        o.put("items", getResultsJSON(results));
        o.put("info", getInfoJSON(results));

        return o;
    }

    private JSONObject getJSON(SearchResultSet results, SearchForm form) {
        JSONObject o = new JSONObject();

        o.put("items", getResultsJSON(results));
        o.put("facets", getFacetsJSON(results, form));
        o.put("info", getInfoJSON(results));

        return o;
    }

    public static class Range {
        @Min(0)
        public int start = 0;
        @Min(0)
        public int end = 8;

        public void setStart(int start) {
            this.start = start;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    // on path /search/JSON?start=<startIndex>&end=<endIndex>&search params
    @RequestMapping(method = RequestMethod.GET, value = "/JSON",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleItemsAjaxRequest(
            @Valid SearchForm searchForm,
            @Valid Range range) {

        SearchResultSet results = this.search.makeSearch(
            searchForm, range.start, range.end);

        // Write out JSON file.
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .header("Cache-Control", "public, max-age=60")
            .body(getItemsJSON(results).toString());
    }

    /**
     * Similar to the /JSON endpoint, except this includes facets and
     * statistics, allowing the entire page to be re-rendered.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/JSONAdvanced",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleItemsAdvancedAjaxRequest(
            @Valid SearchForm searchForm,
            @RequestParam("start") int startIndex,
            @RequestParam("end") int endIndex) {

        SearchResultSet results = this.search.makeSearch(
                searchForm, startIndex, endIndex);

        // Write out JSON file.
        return ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=60")
                .body(getJSON(results, searchForm).toString());
    }


    public static Comparator<Collection> collectionTitleComparator = new Comparator<Collection>() {

        public int compare(Collection col1, Collection col2) {

            // ascending order
            return col1.getTitle().compareTo(col2.getTitle());
        }

    };
}
