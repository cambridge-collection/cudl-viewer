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
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
    private final ItemsDao itemDAO;
    private final String contentHtmlUrl;
    private static final Pattern SPLIT_RE = Pattern.compile("\\s*,\\s*");

    private static final String PATH_COLLECTION_NO_PAGE = "/{collectionId}";
    private static final String PATH_COLLECTION_WITH_PAGE = "/{collectionId}/{page}";

    @Autowired
    public CollectionViewController(CollectionFactory collectionFactory,
                                    ItemsDao itemDAO,
                                    @Value("${cudl-viewer-content.html.path}") String contentHtmlPath) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(contentHtmlPath, "cudl-viewer-content.html.path is required");

        this.collectionFactory = collectionFactory;
        this.itemDAO = itemDAO;
        this.contentHtmlUrl = Paths.get(contentHtmlPath).toUri().toString();
    }

    // on path /collections/
    @RequestMapping(value = "/")
    public ModelAndView handleViewRequest()
            throws Exception {

        List<Collection> collections = collectionFactory.getCollections();
        final ModelAndView modelAndView = new ModelAndView("jsp/collections");

        // order by alphabetical title for this page
        Collections.sort(collections, Collection.SORT_BY_TITLE);

        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
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

        modelAndView.addObject("collection", collection);
        if (collection.getMetaDescription() != null) {
            modelAndView.addObject("metaDescription", collection.getMetaDescription());
        }
        modelAndView.addObject("itemDAO", itemDAO);
        modelAndView.addObject("collectionFactory", collectionFactory);
        modelAndView.addObject("imageServer", iiifImageServer);
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        modelAndView.addObject("pageNumber", pageNumber <= 0 ? 1 : pageNumber);

        // append a list of this collections subcollections if this is a parent.
        if (collection.getType().equals("parent")) {
            final List<Collection> subCollections = collection.getSubCollections();
            modelAndView.addObject("subCollections", subCollections);
        }
        return modelAndView;
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

        final List<String> ids = collection.getItemIds();
        final List<Item> items = new ArrayList<>();

        if (startIndex < 0) {
            startIndex = 0;
        } else if (endIndex >= ids.size()) {
            endIndex = ids.size(); // if end Index is too large cap at max
            // size
        }

        for (int i = startIndex; i < endIndex; i++) {
            items.add(itemDAO.getItem(ids.get(i)));
        }

        final List<JSONObject> itemsJSON = new ArrayList<>(items.size());

        for (final Item item : items) {
            itemsJSON.add(item.getSimplifiedJSON());
        }

        // build the request object
        final JSONObject dataRequest = new JSONObject();
        dataRequest.put("start", startIndex);
        dataRequest.put("end", endIndex);
        dataRequest.put("collectionId", collectionId);

        // build the final returned JSON data
        final JSONObject data = new JSONObject();
        data.put("request", dataRequest);
        data.put("items", itemsJSON);

        return data.toString();
    }
}
