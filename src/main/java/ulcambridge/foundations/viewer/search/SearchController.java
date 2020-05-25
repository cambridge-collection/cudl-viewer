package ulcambridge.foundations.viewer.search;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Controller
@RequestMapping("/search")
@Validated
public class SearchController {
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final Search search;
    private final ItemsDao itemDAO;
    private final CollectionFactory collectionFactory;
    private final URI imageServerURL;

    /**
     * @param search to use for queries. e.g. SearchXTF
     */
    @Autowired
    public SearchController(CollectionFactory collectionFactory,
                            ItemsDao itemDAO, Search search, @Qualifier("imageServerURL") URI imageServerURL) {

        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(search, "search is required");
        Assert.notNull(imageServerURL, "imageServerURL is required");

        this.collectionFactory = collectionFactory;
        this.itemDAO = itemDAO;
        this.search = search;
        this.imageServerURL = imageServerURL;
    }

    // on /search path
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView processSearch(@Valid SearchForm searchForm)
            throws MalformedURLException {

        // Perform XTF Search
        SearchResultSet results = this.search.makeSearch(searchForm, 1, 1);

        ModelAndView modelAndView = new ModelAndView("jsp/search-results");
        modelAndView.addObject("form", searchForm);
        modelAndView.addObject("results", results);
        modelAndView.addObject("queryString",
                SearchUtil.getURLParameters(searchForm));

        return modelAndView;
    }

    /**
     * Advanced Search Query Page
     *
     * /search/advanced/query
     */
    @RequestMapping(method = RequestMethod.GET, value = "/advanced/query")
    public ModelAndView advancedSearch(
            @Valid @ModelAttribute SearchForm searchForm,
            @RequestParam(value="tagging", required=false, defaultValue="false") boolean enableTagging,
            BindingResult bindingResult, HttpSession session)
            throws MalformedURLException {

        ModelAndView modelAndView = new ModelAndView("jsp/search-advanced");
        List<Collection> collectionList = collectionFactory.getCollections();
        Collections.sort(collectionList, collectionTitleComparator);
         // order alphabetically by title
        searchForm.setCollections(collectionList);
        modelAndView.addObject("form", searchForm);
        modelAndView.addObject("enableTagging", enableTagging);
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
            @RequestParam(value="tagging", required=false, defaultValue="false") boolean enableTagging,
            BindingResult bindingResult, HttpSession session)
            throws MalformedURLException {

        // Perform XTF Search
        SearchResultSet results = this.search.makeSearch(searchForm, 1, 1);

        return new ModelAndView("jsp/search-advancedresults")
                .addObject("form", searchForm)
                .addObject("results", results)
                .addObject("queryString",
                        SearchUtil.getURLParameters(searchForm))
                .addObject("enableTagging", enableTagging);
    }

    private JSONArray getResultsJSON(SearchResultSet results) {
        Assert.notNull(results);

        // Put chosen search results into an array.
        JSONArray jsonArray = new JSONArray();

        for(SearchResult searchResult : results.getResults()) {
            jsonArray.add(getResultItemJSON(searchResult));
        }

        return jsonArray;
    }

    private JSONObject getResultItemJSON(SearchResult searchResult) {
        Item item = itemDAO.getItem(searchResult.getFileId());

        // Make a JSON object that contains information about the
        // matching item and information about the result snippets
        // and pages that match.
        JSONObject itemJSON = new JSONObject();
        itemJSON.put("item", item.getSimplifiedJSON());
        itemJSON.put("startPage", searchResult.getStartPage());
        itemJSON.put("startPageLabel", searchResult.getStartPageLabel());
        itemJSON.put("itemType", searchResult.getType());

        // Make an array for the snippets.
        JSONArray resultsArray = new JSONArray();

        for (String snippet : searchResult.getSnippets()) {
            resultsArray.add(snippet.trim());
        }

        itemJSON.put("snippets", resultsArray);

        itemJSON.put("pageThumbnailURL", getResultThumbnailURL(item, searchResult)
            .map(url -> this.imageServerURL.resolve(url).toString()).orElse("/img/no-thumbnail.jpg"));

        return itemJSON;
    }

    private static Optional<URI> getResultThumbnailURL(Item item, SearchResult searchResult) {
        int pageIndex = searchResult.getStartPage() - 1;
        String url = null;
        if(pageIndex >= 0 && pageIndex < item.getPageThumbnailURLs().size()) {
            url = item.getPageThumbnailURLs().get(pageIndex);
        }
        // The list holds empty string for pages without thumbnails
        if(url != null && !url.isEmpty()) {
            try {
                return Optional.of(new URI(url));
            }
            catch (URISyntaxException e) {
                LOG.error("Invalid thumbnail URL in item '{}', page {}: '{}'", item.getId(), pageIndex, url);
            }
        }
        return Optional.empty();
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
            .body(getResultsJSON(results).toString());
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
