package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
public class CollectionViewController {

    protected final Log logger = LogFactory.getLog(getClass());
    private CollectionFactory collectionFactory;
    private ItemFactory itemFactory;

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }

    // on path /collections/
    @RequestMapping(value = "/")
    public ModelAndView handleViewRequest(HttpServletResponse response)
            throws Exception {

        ModelAndView modelAndView = new ModelAndView("jsp/collections");
        ArrayList<Item> featuredItems = new ArrayList<Item>();
        String[] itemIds = Properties.getString("collection.featuredItems")
                .split("\\s*,\\s*");
        for (int i = 0; i < itemIds.length; i++) {
            String itemId = itemIds[i];
            featuredItems.add(itemFactory.getItemFromId(itemId));
        }
        modelAndView.addObject("featuredItems", featuredItems);
        return modelAndView;
    }

    // on path /collections/{collectionId}
    @RequestMapping(value = "/{collectionId}")
    public ModelAndView handleRequest(HttpServletResponse response,
            @PathVariable("collectionId") String collectionId,
            HttpServletRequest request) {
    	
        Collection collection = collectionFactory
                .getCollectionFromId(collectionId);

        ModelAndView modelAndView = new ModelAndView("jsp/collection-"
                + collection.getType());

        // Get imageServer
        String imageServer = Properties.getString("imageServer");

        // Get content url
        String contentHTMLURL = Properties.getString("cudl-viewer-content.html.url");

        modelAndView.addObject("collection", collection);
        modelAndView.addObject("itemFactory", itemFactory);
        modelAndView.addObject("collectionFactory", collectionFactory);
        modelAndView.addObject("imageServer", imageServer);
        modelAndView.addObject("contentHTMLURL", contentHTMLURL);

        // append a list of this collections subcollections if this is a parent. 
        if (collection.getType().equals("parent")) {

            List<Collection> subCollections = this.collectionFactory
                    .getSubCollections(collection);
            modelAndView.addObject("subCollections", subCollections);

        }
        
        return modelAndView;

    }

	// on path
    // /collections/{collectionId}/itemJSON?start=<startItemPosition>&end=<endItemPosition>
    // To get information for items 0 to 8 url would be
    // /collections/{collectionId}/itemJSON?start=0&end=8
    @RequestMapping(value = "/{collectionId}/itemJSON")
    public ModelAndView handleItemsAjaxRequest(HttpServletResponse response,
            @PathVariable("collectionId") String collectionId,
            @RequestParam("start") int startIndex,
            @RequestParam("end") int endIndex, HttpServletRequest request)
            throws Exception {

        Collection collection = collectionFactory
                .getCollectionFromId(collectionId);

        List<String> ids = collection.getItemIds();
        List<Item> items = new ArrayList<Item>();

        if (startIndex < 0) {
            startIndex = 0;
        } else if (endIndex >= ids.size()) {
            endIndex = ids.size(); // if end Index is too large cap at max
            // size
        }

        for (int i = startIndex; i < endIndex; i++) {
            items.add(itemFactory.getItemFromId(ids.get(i)));
            
        }

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            jsonArray.add(item.getSimplifiedJSON());
        }

        // build the request object
        JSONObject dataRequest = new JSONObject();
        dataRequest.put("start", startIndex);
        dataRequest.put("end", endIndex);
        dataRequest.put("collectionId", collectionId);

        // build the final returned JSON data
        JSONObject data = new JSONObject();
        data.put("request", dataRequest);
        data.put("items", jsonArray);

        // Write out JSON file.
        response.setContentType("application/json");
        PrintStream out = null;

        try {
            out = new PrintStream(new BufferedOutputStream(
                    response.getOutputStream()), true, "UTF-8");
            out.print(data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return null;

    }

}
