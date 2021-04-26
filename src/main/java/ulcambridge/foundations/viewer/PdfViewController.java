package ulcambridge.foundations.viewer;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.pdf.FullDocumentPdf;
import ulcambridge.foundations.viewer.pdf.SinglePagePdf;

import javax.servlet.http.HttpServletResponse;

/**
 * Creates PDFs
 *
 */
@Controller
@RequestMapping("/pdf")
public class PdfViewController {

    private final ItemsDao itemDAO;
    private final SinglePagePdf singlePagePdf;
    private final FullDocumentPdf fullDocumentPdf;

    @Autowired
    public PdfViewController(
        ItemsDao itemDAO,
        SinglePagePdf singlePagePdf,
        FullDocumentPdf fullDocumentPdf) {

        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(singlePagePdf, "singlePagePdf is required");

        this.itemDAO = itemDAO;
        this.singlePagePdf = singlePagePdf;
        this.fullDocumentPdf = fullDocumentPdf;
    }

    // on path /pdf/{docId}/{page}
    @RequestMapping(value = "/{docId}/{page}")
    public void handleSinglePagePDF(@PathVariable("docId") String docId,
                                          @PathVariable("page") String page,
                                          HttpServletResponse response) throws JSONException {
        docId = docId.toUpperCase();

        Item item = itemDAO.getItem(docId);

        // Check metadata for permission to download pdf
        if (item != null && hasPermissionForPDFDownload(item)) {

            singlePagePdf.writePdf(item, page, response);

        } else {
            throw new ResourceNotFoundException();
        }
    }

    // on path /pdf/{docId}/
    @RequestMapping(value = "/{docId}")
    public void handleFullDocumentPDF(@PathVariable("docId") String docId,
                                  HttpServletResponse response) throws JSONException {
        docId = docId.toUpperCase();

        Item item = itemDAO.getItem(docId);

        // Check metadata for permission to download pdf
        if (item != null && hasPermissionForPDFDownload(item)) {

           fullDocumentPdf.writePdf(item, response);

        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Like the download image functionality, the download pdf functionality is limited to items which have a
     * downloadImageRights property.
     *
     * @param item
     * @return
     */
    private boolean hasPermissionForPDFDownload(Item item) {
        if (item!=null) {
            JSONObject descriptiveMetadata = item.getJSON().getJSONArray("descriptiveMetadata")
                .getJSONObject(0);

            // has downloadImageRights property that is not empty.
            return descriptiveMetadata.has("downloadImageRights") &&
                !descriptiveMetadata.getString("downloadImageRights").trim().equals("");
        }
        return false;
    }

}
