package ulcambridge.foundations.viewer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Properties;
import ulcambridge.foundations.viewer.search.CollectionItemsPage;
import ulcambridge.foundations.viewer.search.Search;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller for viewing a collection.
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/collections")
public class CollectionViewController {
    private final CollectionFactory collectionFactory;
    private final Search search;
    private final String contentHtmlPath;
    private final boolean showUnreleasedContent;
    private final String unreleasedDataDirectory;

    private static final String PATH_COLLECTION_NO_PAGE = "/{collectionId}";
    private static final String PATH_COLLECTION_WITH_PAGE = "/{collectionId}/{page}";

    /**
     * How many virtual collection tiles are rendered server-side, and the batch size
     * the client appends in thereafter. 20 matches what the search API serves for a
     * single request, so a batch never costs more than one query.
     */
    private static final int VIRTUAL_COLLECTION_BATCH_SIZE = 20;

    @Autowired
    public CollectionViewController(CollectionFactory collectionFactory,
                                    Search search,
                                    @Value("${cudl-viewer-content.html.path}") String contentHtmlPath,
                                    @Value("${showUnreleasedContent:false}") boolean showUnreleasedContent,
                                    @Value("${unreleasedDataDirectory:}") String unreleasedDataDirectory) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(search, "search is required");
        Assert.notNull(contentHtmlPath, "cudl-viewer-content.html.path is required");

        this.collectionFactory = collectionFactory;
        this.search = search;
        this.contentHtmlPath = contentHtmlPath;
        this.showUnreleasedContent = showUnreleasedContent;
        this.unreleasedDataDirectory = unreleasedDataDirectory;
    }

    /**
     * Returns the base URL directory that contains the given collection HTML file
     * (e.g. {@code collections/newton/summary.html}). Checks the main pages/html
     * directory first; if the file is absent and unreleased content is enabled,
     * falls back to the unreleased pages/html directory. This lets collection
     * summary and sponsor pages be served from the unreleased data without
     * changing where general pages (footer, etc.) are looked up.
     */
    private String resolveHtmlBaseUrl(String relativePath) {
        Path mainFile = Paths.get(contentHtmlPath, relativePath);
        if (mainFile.toFile().exists()) {
            return Paths.get(contentHtmlPath).toUri().toString();
        }
        if (showUnreleasedContent && !unreleasedDataDirectory.isBlank()) {
            Path unreleasedBase = Path.of(unreleasedDataDirectory, "pages", "html");
            if (unreleasedBase.resolve(relativePath).toFile().exists()) {
                return unreleasedBase.toUri().toString();
            }
        }
        return Paths.get(contentHtmlPath).toUri().toString();
    }

    // on path /collections/
    @RequestMapping(value = "/")
    public ModelAndView handleViewRequest()
            throws Exception {

        List<Collection> collections = collectionFactory.getCollections();
        final ModelAndView modelAndView = new ModelAndView("jsp/collections");

        // order by alphabetical title for this page
        Collections.sort(collections, Collection.SORT_BY_TITLE);

        modelAndView.addObject("contentHTMLURL", Paths.get(contentHtmlPath).toUri().toString());
        modelAndView.addObject("collections", collections);

        return modelAndView;
    }

    // on path /collections/{collectionId}
    @RequestMapping(value = PATH_COLLECTION_NO_PAGE)
    public ModelAndView handleRequest(@PathVariable("collectionId") String collectionId) {
        return handleRequest(collectionId, 1);
    }

    // on path /collections/{collectionId}/{page}
    @RequestMapping(value = PATH_COLLECTION_WITH_PAGE)
    public ModelAndView handleRequest( @PathVariable("collectionId") String collectionId,
                                       @PathVariable("page") Integer pageNumber) {

        final Collection collection = collectionFactory
                .getCollectionFromId(collectionId);

        if (collection == null){
            throw new ResourceNotFoundException();
        }
        final ModelAndView modelAndView = new ModelAndView("jsp/collection-"
                + collection.getType());

        // Get imageServer
        final String iiifImageServer = Properties.getString("IIIFImageServer");

        String summaryPath = collection.getSummary();
        String collectionHtmlBase = (summaryPath != null)
            ? resolveHtmlBaseUrl(summaryPath)
            : Paths.get(contentHtmlPath).toUri().toString();

        modelAndView.addObject("collection", collection);
        if (collection.getMetaDescription() != null) {
            modelAndView.addObject("metaDescription", collection.getMetaDescription());
        }
        modelAndView.addObject("collectionFactory", collectionFactory);
        modelAndView.addObject("imageServer", iiifImageServer);
        // contentHTMLURL always points to the main pages/html directory so that
        // global includes (footer, etc.) resolve correctly for all collections.
        // collectionHTMLURL is the resolved base for this collection's own HTML
        // (summary, sponsors) and may point to the unreleased directory instead.
        modelAndView.addObject("contentHTMLURL", Paths.get(contentHtmlPath).toUri().toString());
        modelAndView.addObject("collectionHTMLURL", collectionHtmlBase);
        modelAndView.addObject("pageNumber", pageNumber <= 0 ? 1 : pageNumber);

        // Virtual collections render their first batch of tiles server-side rather than
        // waiting on AJAX, so that batch has to be in the model at render time.
        if ("virtual".equals(collection.getType())) {
            addVirtualCollectionItems(modelAndView, collectionId);
        }

        // append a list of this collections subcollections if this is a parent.
        if ("parent".equals(collection.getType())) {
            final List<Collection> subCollections = collection.getSubCollections();
            modelAndView.addObject("subCollections", subCollections);
        }
        return modelAndView;
    }

    /**
     * Adds a virtual collection's first batch of item tiles, sourced from Solr like
     * the organisation carousel's, so the two page types cannot disagree about an
     * item's title, abstract, thumbnail or release state.
     *
     * <p>Only the first batch is rendered server-side: it keeps the page crawlable and
     * useful without Javascript, while the client lazily appends the rest from the
     * same {@code itemJSON} endpoint the organisation carousel uses. The items are
     * exposed as maps because EL cannot read properties off a {@code JSONObject}.
     */
    private void addVirtualCollectionItems(ModelAndView modelAndView, String collectionId) {
        final CollectionItemsPage page =
            search.getCollectionItems(collectionId, 0, VIRTUAL_COLLECTION_BATCH_SIZE);

        final List<Map<String, Object>> items = new ArrayList<>();
        for (JSONObject item : page.getItems()) {
            items.add(item.toMap());
        }

        modelAndView.addObject("items", items);
        // The client appends batches until it has this many, so it has to come from
        // Solr rather than the collection file, which can list unindexed items.
        modelAndView.addObject("collectionTotal", page.getTotal());
        modelAndView.addObject("collectionBatchSize", VIRTUAL_COLLECTION_BATCH_SIZE);
        // The JSP has to tell a Solr outage apart from a collection with no indexed
        // items: the first is a temporary error worth reporting, the second is not.
        modelAndView.addObject("itemsUnavailable", !page.isAvailable());
    }

    // on path
    // /collections/{collectionId}/itemJSON?start=<startItemPosition>&end=<endItemPosition>
    // To get information for items 0 to 8 url would be
    // /collections/{collectionId}/itemJSON?start=0&end=8
    @RequestMapping(value = "/{collectionId}/itemJSON", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String handleItemsAjaxRequest(
            @PathVariable("collectionId") String collectionId,
            @RequestParam("start") int startIndex,
            @RequestParam("end") int endIndex)
            throws Exception {

        final Collection collection = collectionFactory
                .getCollectionFromId(collectionId);

        if (collection == null){
            throw new ResourceNotFoundException();
        }

        if (startIndex < 0) {
            startIndex = 0;
        }
        final int rows = Math.max(0, endIndex - startIndex);

        // Replaces the per-item filesystem item load and the whole-collection
        // unreleased scan; unreleased badging now rides on each item's Solr data.
        final CollectionItemsPage page =
            search.getCollectionItems(collectionId, startIndex, rows);

        // build the request object
        final JSONObject dataRequest = new JSONObject();
        dataRequest.put("start", startIndex);
        dataRequest.put("end", endIndex);
        dataRequest.put("collectionId", collectionId);

        // build the final returned JSON data
        final JSONObject data = new JSONObject();
        data.put("request", dataRequest);
        data.put("items", page.getItems());
        // The carousel paginates against this rather than the collection file's item
        // count, which can list items that were never indexed. It comes back on the
        // same Solr response as the items, so it costs no extra query.
        data.put("total", page.getTotal());

        return data.toString();
    }
}
