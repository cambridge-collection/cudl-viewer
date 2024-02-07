package ulcambridge.foundations.viewer;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

/**
 * Controller for viewing iiif metadata and collections
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/iiif")
public class IIIFViewController {
    private final ItemsDao itemDAO;
    private final CollectionFactory collectionFactory;
    private final URI iiifImageServer;
    private final UI themeUI;

    @Autowired
    public IIIFViewController(ItemsDao itemDAO,
                              CollectionFactory collectionFactory,
                              @Qualifier("rootUrl") URI rootUrl,
                              @Qualifier("iiifImageServer")URI iiifImageServer,
                              @Qualifier("uiThemeBean") UI uiTheme) {

        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(rootUrl, "rootUrl is required");
        Assert.notNull(iiifImageServer, "iiifImageServer is required");
        Assert.notNull(uiTheme, "UI (theme) is required");

        this.itemDAO = itemDAO;
        this.collectionFactory = collectionFactory;
        this.iiifImageServer = iiifImageServer;
        this.themeUI = uiTheme;
    }

    // on path /iiif/{docId}.json
    @RequestMapping(value = "/{docId}.json")
    public RedirectView handleIIIFJSONRequest(@PathVariable("docId") String docId, HttpServletRequest request, HttpServletResponse response) throws JSONException {
        return new RedirectView(docId);
    }

    // on path /iiif/{docId}
    @RequestMapping(value = "/{docId}")
    public ModelAndView handleIIIFRequest(@PathVariable("docId") String docId, HttpServletRequest request, HttpServletResponse response) throws JSONException {

        IIIFPresentation pres = getIIIFPresentation(docId, request);
        JSONObject presJSON = pres.outputJSON();

        writeJSONOut(presJSON, response);

        return null;
    }

    // on path /iiif/{docId}/simple
    @RequestMapping(value = "/{docId}/simple")
    public ModelAndView handleSimpleIIIFRequest(@PathVariable("docId") String docId, HttpServletRequest request, HttpServletResponse response) throws JSONException {


        IIIFPresentation pres = getIIIFPresentation(docId, request);
        JSONObject presJSON = pres.outputSimpleJSON();

        writeJSONOut(presJSON, response);

        return null;

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
                coll = new IIIFCollection("all", themeUI.getThemeUI().getTitle()+" IIIF Collections", "All the available IIIF items available on this platform.", null, collectionFactory.getCollections(), null, baseURL);
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

    private IIIFPresentation getIIIFPresentation(String docId, HttpServletRequest request) {

        docId = docId.toUpperCase();

        //Get services
        String servicesURL = Properties.getString("services");
        if (servicesURL.startsWith("//")) {
            servicesURL = request.getScheme() + ":"+servicesURL;
        }

        Item item = itemDAO.getItem(docId);
        if (item != null && item.getIIIFEnabled()) {

            String baseURL = request.getScheme() + "://" + request.getServerName();
            if (!(request.getServerPort()==443)&&!(request.getServerPort()==80)) {
                baseURL+= ":" + request.getServerPort();
            }
            return new IIIFPresentation(item, baseURL, servicesURL, iiifImageServer, themeUI);

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
