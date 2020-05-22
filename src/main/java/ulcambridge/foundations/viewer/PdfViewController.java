package ulcambridge.foundations.viewer;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.pdf.SinglePagePdf;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Controller for viewing a specific document or a specific page within a
 * document. Documents and pages are specified in the url requested.
 *
 * @author jennie
 *
 */
@Controller
@RequestMapping("/pdf")
public class PdfViewController {
    private static final String PATH_PREFIX = "/pdf";
    private static final String PATH_DOC_NO_PAGE = "/{docId}";
    private static final String PATH_DOC_WITH_PAGE = "/{docId}/{page}";

    private final CollectionFactory collectionFactory;
    private final ItemsDao itemDAO;

    private final URI rootURL;
    private final URI iiifImageServer;
    private final SinglePagePdf singlePagePdf;

    @Autowired
    public PdfViewController(
        CollectionFactory collectionFactory,
        ItemsDao itemDAO,
        URI rootUrl,
        URI iiifImageServer,
        @Qualifier("singlePagePdf") SinglePagePdf singlePagePdf) {

        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(rootUrl, "rootUrl is required");
        Assert.notNull(iiifImageServer, "iiifImageServer is required");
        Assert.notNull(singlePagePdf, "singlePagePdf is required");

        this.collectionFactory = collectionFactory;
        this.itemDAO = itemDAO;
        this.rootURL = rootUrl;
        this.iiifImageServer = iiifImageServer;
        this.singlePagePdf = singlePagePdf;

    }

    // on path /pdf/{docId}/{page}
    @RequestMapping(value = "/{docId}/{page}")
    public void handleJSONRequest(@PathVariable("docId") String docId,
                                          @PathVariable("page") String page,
                                          HttpServletResponse response) throws JSONException {
        docId = docId.toUpperCase();

        Item item = itemDAO.getItem(docId);
        if (item != null) {

            singlePagePdf.writePdf(item, docId, page, response);

        } else {
            throw new ResourceNotFoundException();
        }
    }

}
