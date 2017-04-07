package ulcambridge.foundations.viewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

import java.util.ArrayList;
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

    protected final Log logger = LogFactory.getLog(getClass());
    private final CollectionFactory collectionFactory;
    private final ItemFactory itemFactory;
    private static final Pattern SPLIT_RE = Pattern.compile("\\s*,\\s*");

    @Autowired
    public CollectionViewController(CollectionFactory collectionFactory,
                                    ItemFactory itemFactory) {
        Assert.notNull(collectionFactory);
        Assert.notNull(itemFactory);

        this.collectionFactory = collectionFactory;
        this.itemFactory = itemFactory;
    }


    // on path /collections/
    @RequestMapping(value = "/")
    public ModelAndView handleViewRequest()
            throws Exception {

        final ModelAndView modelAndView = new ModelAndView("jsp/collections");
        final ArrayList<Item> featuredItems = new ArrayList<Item>();
        final String itemIdString = Properties.getString("collection.featuredItems");
        final String[] itemIds = SPLIT_RE.split(itemIdString);
        for (final String itemId : itemIds) {
            featuredItems.add(itemFactory.getItemFromId(itemId));
        }
        modelAndView.addObject("featuredItems", featuredItems);
        return modelAndView;
    }

    // on path /collections/{collectionId}
    @RequestMapping(value = "/{collectionId}")
    public ModelAndView handleRequest( @PathVariable("collectionId") String collectionId ) {

        final Collection collection = collectionFactory
                .getCollectionFromId(collectionId);

        final ModelAndView modelAndView = new ModelAndView("jsp/collection-"
                + collection.getType());

        // Get imageServer
        final String imageServer = Properties.getString("imageServer");

        // Get content url
        final String contentHTMLURL = Properties.getString("cudl-viewer-content.html.url");

        modelAndView.addObject("collection", collection);
        if (collection.getMetaDescription() != null) {
            modelAndView.addObject("metaDescription", collection.getMetaDescription());
        }
        modelAndView.addObject("itemFactory", itemFactory);
        modelAndView.addObject("collectionFactory", collectionFactory);
        modelAndView.addObject("imageServer", imageServer);
        modelAndView.addObject("contentHTMLURL", contentHTMLURL);

        // append a list of this collections subcollections if this is a parent.
        if (collection.getType().equals("parent")) {
            final List<Collection> subCollections = this.collectionFactory
                    .getSubCollections(collection);
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

        final List<String> ids = collection.getItemIds();
        final List<Item> items = new ArrayList<>();

        if (startIndex < 0) {
            startIndex = 0;
        } else if (endIndex >= ids.size()) {
            endIndex = ids.size(); // if end Index is too large cap at max
            // size
        }

        for (int i = startIndex; i < endIndex; i++) {
            items.add(itemFactory.getItemFromId(ids.get(i)));
        }

        final JSONArray jsonArray = new JSONArray();

        for (final Item item : items) {
            jsonArray.add(item.getSimplifiedJSON());
        }

        // build the request object
        final JSONObject dataRequest = new JSONObject();
        dataRequest.put("start", startIndex);
        dataRequest.put("end", endIndex);
        dataRequest.put("collectionId", collectionId);

        // build the final returned JSON data
        final JSONObject data = new JSONObject();
        data.put("request", dataRequest);
        data.put("items", jsonArray);

        return data.toString();
    }
}
