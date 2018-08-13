package ulcambridge.foundations.viewer.search;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for viewing a collection.
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    protected final Log logger = LogFactory.getLog(getClass());
    private final Search search;
    private final ItemFactory itemFactory;
    private final CollectionFactory collectionFactory;

    /**
     * Constructor, set in search-servlet.xml.
     *
     * @param search
     *            to use for queries. e.g. SearchXTF.
     */
    @Autowired
    public SearchController(CollectionFactory collectionFactory,
                            ItemFactory itemFactory, Search search) {

        Assert.notNull(collectionFactory);
        Assert.notNull(itemFactory);
        Assert.notNull(search);

        this.collectionFactory = collectionFactory;
        this.itemFactory = itemFactory;
        this.search = search;
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
     *
     * @param request
     * @param response
     * @return
     * @throws MalformedURLException
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
     *
     * @param request
     * @param response
     * @return
     * @throws MalformedURLException
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
        Item item = itemFactory.getItemFromId(searchResult.getFileId());

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

        // Page Thumbnails.  Use specified thumbnail if possible.
        if (searchResult.getThumbnailURL() != null) {
            String pageThumbnail = searchResult.getThumbnailURL();
            if (pageThumbnail.contains("thumbnail")) {
                itemJSON.put("pageThumbnailURL", pageThumbnail);
            } else {
                pageThumbnail = Properties.getString("imageServer") + pageThumbnail;
                itemJSON.put("pageThumbnailURL", pageThumbnail);
            }
        } else {
            String pageThumbnail = "";
            org.json.JSONObject page = null;
            try {
                page = (org.json.JSONObject) item.getJSON()
                        .getJSONArray("pages")
                        .get(searchResult.getStartPage() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (page != null && page.get("IIIFImageURL") != null) {

                    pageThumbnail = page.get("IIIFImageURL")
                            .toString();

                    pageThumbnail = Properties.getString("IIIFImageServer") + pageThumbnail;

                }
            } catch (JSONException e) {

                // displayImageURL not found, which throws an exception.
                pageThumbnail = "/img/no-thumbnail.jpg";

            }

            itemJSON.put("pageThumbnailURL", pageThumbnail);

        }// End Page Thumbnails

        return itemJSON;
    }

    private JSONObject getFacetJson(Facet facet) {
        JSONObject o = new JSONObject();

        o.put("value", facet.getBand());
        o.put("occurrences", facet.getOccurences());

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

    // on path /search/JSON?start=<startIndex>&end=<endIndex>&search params
    @RequestMapping(method = RequestMethod.GET, value = "/JSON",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleItemsAjaxRequest(
            @Valid SearchForm searchForm,
            @RequestParam("start") int startIndex,
            @RequestParam("end") int endIndex) {

        SearchResultSet results = this.search.makeSearch(
                searchForm, startIndex, endIndex);

        // Write out JSON file.
        return ResponseEntity.ok()
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
