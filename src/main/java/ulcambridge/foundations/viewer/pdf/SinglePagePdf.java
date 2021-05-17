package ulcambridge.foundations.viewer.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import org.json.JSONObject;
import ulcambridge.foundations.viewer.model.Item;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

public class SinglePagePdf {

    private final String IIIFImageServer;
    private final BasicTemplatePdf basicTemplatePdf;

    /**
     * This is used to generate a PDF with a large image of a single page and document metadata.
     *
     * These classes could be moved out to a separate service.
     */
    public SinglePagePdf(String IIIFImageServer, String baseURL,
                         String headerText, int[] pdfColour,
                         String[] urlsForFontZips, String defaultFont, String cachePath) throws MalformedURLException {
        this.IIIFImageServer = IIIFImageServer;
        this.basicTemplatePdf = new BasicTemplatePdf(baseURL, headerText, pdfColour,
            urlsForFontZips, defaultFont, cachePath);
    }

    public void writePdf(Item item, String pageInput, HttpServletResponse response) {

        try {
            int page = Integer.parseInt(pageInput);
            int numPages = item.getJSON().getJSONArray("pages").length();
            if (page < 1) { page = 1; }
            if (page > numPages) { page = numPages; }

            JSONObject pageJSON = item.getJSON().getJSONArray("pages").getJSONObject(page - 1);
            String iiifImageURL = pageJSON.getString("IIIFImageURL");

            String imageURL = IIIFImageServer + iiifImageURL + "/full/,1000/0/default.jpg";
            if (pageJSON.getInt("imageWidth") > pageJSON.getInt("imageHeight")) {
                imageURL = IIIFImageServer + iiifImageURL + "/full/1000,/0/default.jpg";
            }
            Image image = new Image(ImageDataFactory.create(imageURL));

            Div div = new Div();
            div.add(image.setMargins(10f, 0f, 30f, 0f)
                .scaleToFit(PageSize.A4.getWidth() - 60f, PageSize.A4.getHeight() - 220f));

            basicTemplatePdf.writePdf(item, div, response, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
