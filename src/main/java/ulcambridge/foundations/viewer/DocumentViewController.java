package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.EssayItem;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for viewing a specific document or a specific page within a
 * document. Documents and pages are specified in the url requested.
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/view")
public class DocumentViewController {
    private static final String PATH_PREFIX = "/view";
    private static final String PATH_DOC_NO_PAGE = "/{docId}";
    private static final String PATH_DOC_WITH_PAGE = "/{docId}/{page}";

    private final CollectionFactory collectionFactory;
    private final ItemsDao itemDAO;

    private final URI rootURL;

    @Autowired
    public DocumentViewController(
        CollectionFactory collectionFactory,
        ItemsDao itemDAO, @Value("${rootURL}") URI rootUrl) {

        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(rootUrl, "rootUrl is required");

        this.collectionFactory = collectionFactory;
        this.itemDAO = itemDAO;
        this.rootURL = rootUrl;
    }

    // on path /view/{docId}
    @RequestMapping(value = PATH_DOC_NO_PAGE)
    public ModelAndView handleRequest(@PathVariable("docId") String docId,
            HttpServletRequest request) {
        // '0' is special page value indicating the item-level view
        return handleRequest(docId, 0, request);
    }

    // on path /view/{docId}/{page}
    @RequestMapping(value = "/{docId}/{page}")
    public ModelAndView handleRequest(@PathVariable("docId") String docId,
            @PathVariable("page") int page, HttpServletRequest request) {

        // force docID to uppercase
        docId = docId.toUpperCase();

        // Show a different view based on the itemType for this item.
        Item item = itemDAO.getItem(docId);
        if (item==null) {
            throw new ResourceNotFoundException();
        }
        String itemType = item.getType();

        //
        // XXX check if tagging feature is enabled
        //
        request.getSession().setAttribute("taggable", item.getTaggingStatus());

        if (itemType.equals("essay")) {
            return setupEssayView((EssayItem) item);
        } else {
            // default to document view.
            return setupDocumentView(item, page, request);
        }

    }

    // on path /view/thumbnails/{docId}.json?page=?&start=?&limit=?
    @RequestMapping(value = "/thumbnails/{docId}.json")
    public ModelAndView handleThumbnailJSONRequest(
            @PathVariable("docId") String docId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            HttpServletResponse response) throws JSONException {

        // force docID to uppercase
        docId = docId.toUpperCase();

        Item item = itemDAO.getItem(docId);
        JSONObject json = new JSONObject(item.getJSON(),
                JSONObject.getNames(item.getJSON()));
        JSONArray pages = json.getJSONArray("pages");

        if (limit != null && limit > 0 && limit < pages.length()
                && start != null) {

            // build smaller pages array.
            JSONArray pagesSubset = new JSONArray();

            for (int i = start; i < (start + limit) && i < pages.length(); i++) {
                pagesSubset.put(pages.get(i));
            }

            json.put("pages", pagesSubset);
        }

        writeJSONOut(json, response);

        return null;
    }

    // on path /view/{docId}.json
    @RequestMapping(value = "/{docId}.json")
    public ModelAndView handleJSONRequest(@PathVariable("docId") String docId,
            HttpServletResponse response) throws JSONException {

        // force docID to uppercase
        docId = docId.toUpperCase();

        Item item = itemDAO.getItem(docId);
        if (item!=null) {
          JSONObject json = item.getJSON();

          writeJSONOut(json, response);

          return null;

        } else {
            throw new ResourceNotFoundException();
        }

    }

    private void writeJSONOut(JSONObject json, HttpServletResponse response)
            throws JSONException {

        // Write out JSON file.
        response.setContentType("application/json");
        try (PrintStream out = new PrintStream(new BufferedOutputStream(
            response.getOutputStream()), true, "UTF-8")) {
            out.print(json.toString(1));
            out.flush();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * View for displaying documents or manuscripts.
     */
    private ModelAndView setupDocumentView(Item item, int page,
            HttpServletRequest request) {

        // check doc exists, if not return 404 page.
        if (item == null || page > item.getPageLabels().size() || page < 0) {
            throw new ResourceNotFoundException();
        }

        String requestURL = request.getRequestURL().toString();
        String docURL = requestURL;

        if (docURL.lastIndexOf("/") == docURL.length() - 1) {
            // cut off last character.
            docURL = docURL.substring(0, docURL.length() - 1);
        }

        // JSON file needs to be a relative URL for Ajax.
        String jsonURL = "/view/" + item.getId() + ".json";
        String jsonThumbnailsURL = "/view/thumbnails/" + item.getId() + ".json";

        List<Collection> docCollections = getCollection(item.getId());
        Collection organisationalCollection = getBreadcrumbCollection(docCollections);

        ModelAndView modelAndView = new ModelAndView("jsp/document");

        // URLs
        modelAndView.addObject("rootURL", rootURL);
        modelAndView.addObject("docURL", docURL);
        modelAndView.addObject("jsonURL", jsonURL);
        modelAndView.addObject("jsonThumbnailsURL", jsonThumbnailsURL);
        modelAndView.addObject("requestURL", requestURL);
        modelAndView.addObject(
                "canonicalURL", this.getCanonicalItemUrl(item.getId(), page));
        modelAndView.addObject("imageServer", Properties.getString("imageServer"));
        modelAndView.addObject("services", Properties.getString("services"));

        // Collection information
        modelAndView.addObject("organisationalCollection",
                organisationalCollection);
        modelAndView.addObject("collections", docCollections);

        // Get parent collection if there is one.
        Collection parent = null;
        if (organisationalCollection.getParentCollectionId() != null) {
            parent = collectionFactory
                    .getCollectionFromId(organisationalCollection
                            .getParentCollectionId());
        }
        modelAndView.addObject("parentCollection", parent);

        // Item Information
        modelAndView.addObject("item", item);
        modelAndView.addObject("docId", item.getId());
        modelAndView.addObject("itemTitle", item.getTitle());
        modelAndView.addObject("itemAuthors",
                new JSONArray(item.getAuthorNames()));
        modelAndView.addObject("itemAuthorsFullform",
                new JSONArray(item.getAuthorNamesFullForm()));
        modelAndView.addObject("itemAbstract", item.getAbstract());

        modelAndView.addObject("itemDAO", itemDAO);

        // Item Text Direction
        JSONObject json = item.getJSON();
        if (json.has("textDirection")) {
            String td = json.getString("textDirection");
            if (td != null && td.equals("R")) {
                modelAndView.addObject("textDirectionRightToLeft", true);
            }
        }

        // Page Information
        modelAndView.addObject("page", page);

        // get the page label, if not at the item-page
        if (page > 0) {
            List<String> pageLabels = item.getPageLabels();
            String label = "";
            if (pageLabels.size() > page - 1) {
                label = pageLabels.get(page - 1);
            }
            modelAndView.addObject("pageLabel", label);

            List<String> thumbnailURLs = item.getPageThumbnailURLs();
            String thumbnailURL = "";
            if (thumbnailURLs.size() > page - 1) {
                thumbnailURL = thumbnailURLs.get(page - 1);
            }
            modelAndView.addObject("thumbnailURL", thumbnailURL);
        } else {
            modelAndView.addObject("thumbnailURL", item.getThumbnailURL());
        }
        modelAndView.addObject("imageReproPageURL", item.getImageReproPageURL());


        // UI Configuration
        modelAndView.addObject("zoomResetButton", Properties.getString("ui.options.buttons.zoomResetButton"));
        modelAndView.addObject("zoomFactor", Properties.getString("ui.options.buttons.zoomFactor"));

        return modelAndView;
    }

    /**
     * Get the canonical URL for an item at a specific page.
     *
     * @param itemId The item ID to get the URL for.
     * @param page The page of the item to link to
     * @return The URL
     */
    private String getCanonicalItemUrl(String itemId, int page) {
        return UriComponentsBuilder.fromUri(this.rootURL)
                .path(PATH_PREFIX)
                .path(page < 2 ? PATH_DOC_NO_PAGE : PATH_DOC_WITH_PAGE)
                .build()
                .expand(itemId, page)
                .encode()
                .toUriString();
    }

    /**
     * View for displaying essay data.
     */
    private ModelAndView setupEssayView(EssayItem item) {

        if (item == null) {
            throw new ResourceNotFoundException();
        }

        List<Collection> docCollections = getCollection(item.getId());
        Collection organisationalCollection = getBreadcrumbCollection(docCollections);

        // Get parent collection if there is one.
        Collection parent = null;
        if (organisationalCollection.getParentCollectionId() != null) {
            parent = collectionFactory
                    .getCollectionFromId(organisationalCollection
                            .getParentCollectionId());
        }

        ModelAndView modelAndView = new ModelAndView("jsp/essay");

        modelAndView.addObject("docId", item.getId());
        modelAndView.addObject("organisationalCollection",
                organisationalCollection);
        modelAndView.addObject("collections", docCollections);

        modelAndView.addObject("itemTitle", item.getTitle());
        modelAndView.addObject("itemAuthors",
                new JSONArray(item.getAuthorNames()));
        modelAndView.addObject("itemAuthorsFullform",
                new JSONArray(item.getAuthorNamesFullForm()));
        modelAndView.addObject("itemAbstract", item.getAbstract());
        modelAndView.addObject("itemThumbnailURL", item.getThumbnailURL());
        modelAndView.addObject("itemThumbnailOrientation",
                item.getThumbnailOrientation());

        modelAndView.addObject("itemDAO", itemDAO);
        modelAndView.addObject("content", item.getContent());
        modelAndView.addObject("relatedItems", item.getItemReferences());
        modelAndView.addObject("parentCollection", parent);

        modelAndView.addObject("essayItem", item);
        return modelAndView;
    }

    /**
     * Get a list of collections that this item is in.
     */
    private List<Collection> getCollection(String docId) {
        return collectionFactory.getCollections().stream()
            .filter(thisCollection -> thisCollection.getItemIds().contains(docId))
            .collect(Collectors.toList());
    }

    /**
     * Get the collection from this list we want to appear in the breadcrumb
     * trail.
     */
    private Collection getBreadcrumbCollection(List<Collection> collections) {

        Collection collection = null;

        for (Collection thisCollection : collections) {
            // Stop if this is an organisational collection, else keep
            // looking
            if (thisCollection.getType().startsWith("organisation")) {
                collection = thisCollection;
            }

        }

        // If the item is not in any organisational collection set
        // the first item in the collections list as it's organisational
        // collection.
        if (collection == null && collections.size() > 0) {
            collection = collections.get(0);
        }

        return collection;
    }

}
