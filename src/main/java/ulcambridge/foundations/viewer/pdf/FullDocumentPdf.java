package ulcambridge.foundations.viewer.pdf;


import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import org.json.JSONObject;
import ulcambridge.foundations.viewer.model.Item;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * This is used to generate a PDF with thumbnails for all pages in the document and links to the viewer for
 * more detail on these pages.  Also includes document metadata.
 *
 * These classes could be moved out to a separate service.
 */
public class FullDocumentPdf {

    private final String IIIFImageServer;
    private final String baseURL;
    private final BasicTemplatePdf basicTemplatePdf;
    private final int MAX_THUMBNAIL_WIDTH_HEIGHT = 200;

    public FullDocumentPdf(String IIIFImageServer, String baseURL,
                           String headerText, int[] pdfColour,
                           String[] urlsForFontZips, String defaultFont, String cachePath) throws MalformedURLException {
        this.IIIFImageServer = IIIFImageServer;
        this.baseURL = baseURL;
        this.basicTemplatePdf = new BasicTemplatePdf(baseURL, headerText, pdfColour, urlsForFontZips, defaultFont,
            cachePath);
    }

    public void writePdf(Item item, HttpServletResponse response) {

        try {
            // stream from cache if available
            if (basicTemplatePdf.existsInCache(item)) {
                basicTemplatePdf.streamFromCache(item, response);
                return;
            }

            // Images
            float[] columnWidths = {3, 3, 3, 3, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            for (Object pageJSONObj : item.getJSON().getJSONArray("pages")) {

                JSONObject pageJSON = (JSONObject) pageJSONObj;
                String iiifImageURL = pageJSON.getString("IIIFImageURL");

                String imageURL = IIIFImageServer + iiifImageURL + "/full/,"+MAX_THUMBNAIL_WIDTH_HEIGHT+"/0/default.jpg";
                if (pageJSON.getInt("imageWidth") > pageJSON.getInt("imageHeight")) {
                    imageURL = IIIFImageServer + iiifImageURL + "/full/"+MAX_THUMBNAIL_WIDTH_HEIGHT+",/0/default.jpg";
                }

                Image image;
                try {
                    image = new Image(ImageDataFactory.create(imageURL));
                } catch (IOException e) {
                    // we are getting occasional 500 errors from the image server so try again...
                    image = new Image(ImageDataFactory.create(imageURL));
                }

                Cell cell = new Cell();
                cell.add(image.setMargins(0f, 0f, 0f, 0f)
                    .setMaxHeight(MAX_THUMBNAIL_WIDTH_HEIGHT)
                    .setAutoScaleWidth(true)
                    .setAction(PdfAction.createURI(baseURL+"/view/"+item.getId()+"/"+pageJSON.getInt("sequence"))));
                cell.add(new Paragraph(pageJSON.getString("label")).setOpacity(0.5f));
                cell.setBorder(Border.NO_BORDER);
                table.addCell(cell);
            }
            Div div = new Div();
            div.add(table);

            basicTemplatePdf.writePdf(item, div, response, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
