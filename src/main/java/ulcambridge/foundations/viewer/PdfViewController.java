package ulcambridge.foundations.viewer;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.exceptions.ResourceNotFoundException;
import ulcambridge.foundations.viewer.model.Item;
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

    @Autowired
    public PdfViewController(
        ItemsDao itemDAO,
        SinglePagePdf singlePagePdf) {

        Assert.notNull(itemDAO, "itemDAO is required");
        Assert.notNull(singlePagePdf, "singlePagePdf is required");

        this.itemDAO = itemDAO;
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

            singlePagePdf.writePdf(item, page, response);

        } else {
            throw new ResourceNotFoundException();
        }
    }

}
