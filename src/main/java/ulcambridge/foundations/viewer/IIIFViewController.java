package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.view.RedirectView;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for viewing iiif metadata and collections
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/iiif")
public class IIIFViewController {

    private final ItemFactory itemFactory;
    private final CollectionFactory collectionFactory;

    @Autowired
    public IIIFViewController(ItemFactory itemFactory,
                              CollectionFactory collectionFactory,
                              @Value("${rootURL}") URI rootUrl) {

        Assert.notNull(itemFactory);
        Assert.notNull(rootUrl);

        this.itemFactory = itemFactory;
        this.collectionFactory = collectionFactory;

    }

    // on path /iiif/{docId}.json
    @RequestMapping(value = "/{docId}.json")
    public RedirectView handleIIIFJSONRequest(@PathVariable("docId") String docId, HttpServletRequest request, HttpServletResponse response) throws JSONException {
        return new RedirectView(docId);
    }

    // on path /iiif/{docId}
    @RequestMapping(value = "/{docId}")
    public ModelAndView handleIIIFRequest(@PathVariable("docId") String docId, HttpServletRequest request, HttpServletResponse response) throws JSONException {

        docId = docId.toUpperCase();

        //Get services
        String servicesURL = Properties.getString("services");
        if (servicesURL.startsWith("//")) {
            servicesURL = request.getScheme() + ":"+servicesURL;
        }

        Item item = itemFactory.getItemFromId(docId);
        if (item != null && item.getIIIFEnabled()) {

            String baseURL = request.getScheme() + "://" + request.getServerName();
            if (!(request.getServerPort()==443)&&!(request.getServerPort()==80)) {
                baseURL+= ":" + request.getServerPort();
            }
            IIIFPresentation pres = new IIIFPresentation(item, baseURL, servicesURL);
            JSONObject presJSON = pres.outputJSON();

            writeJSONOut(presJSON, response);

            return null;

        } else {
            throw new ResourceNotFoundException();
        }

    }

    // on path /iiif/collection/{collectionId}
    @RequestMapping(value = "/collection/{collectionId}")
    public ModelAndView handleIIIFCollectionRequest(@PathVariable("collectionId")
                                                    String collectionId, HttpServletRequest request,
                                                    HttpServletResponse response) throws JSONException {

        collectionId = collectionId.toLowerCase();

        Collection collection = collectionFactory.getCollectionFromId(collectionId);
        if (collection != null || collectionId.equals("all")) {

            String baseURL = request.getScheme() + "://" + request.getServerName();
            if (!(request.getServerPort()==443)&&!(request.getServerPort()==80)) {
                baseURL+= ":" + request.getServerPort();
            }
            IIIFCollection coll;
            if (collectionId.equals("all")) {
                coll = new IIIFCollection("all", "Cambridge University IIIF Collections", "All the available IIIF items available from Cambridge University Library.", null, collectionFactory.getCollections(), null, baseURL);
            } else  {
                coll = new IIIFCollection(collection, baseURL);
            }
            JSONObject collJSON = coll.outputJSON();

            writeJSONOut(collJSON, response);

            return null;

        } else {
            throw new ResourceNotFoundException();
        }

    }

    private void writeJSONOut(JSONObject json, HttpServletResponse response)
            throws JSONException {

        // Write out JSON file.
        response.setContentType("application/json");
        PrintStream out = null;
        try {
            out = new PrintStream(new BufferedOutputStream(
                    response.getOutputStream()), true, "UTF-8");
            out.print(json.toString(1));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

}
