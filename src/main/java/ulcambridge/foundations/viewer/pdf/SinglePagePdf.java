package ulcambridge.foundations.viewer.pdf;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.*;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import ulcambridge.foundations.viewer.model.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SinglePagePdf {

    private final String IIIF_ROOT = "https://images.lib.cam.ac.uk/iiif/";
    private final String HEADER_TEXT = "Manchester University";

    // Fonts
    private final String JUNICODE = "/usr/local/tomcat/webapps/ROOT/fonts/junicode/Junicode.ttf";
    private final String CHARISSIL = "/usr/local/tomcat/webapps/ROOT/fonts/CharisSIL/CharisSIL-R.ttf";
    private final String NOTOSANS = "/usr/local/tomcat/webapps/ROOT/fonts/noto-sans/NotoSans-Regular.ttf";
    private final String NOTOSANSARABIC = "/usr/local/tomcat/webapps/ROOT/fonts/noto-sans-arabic/NotoSansArabic-Regular.ttf";
    private final String NOTOSANSHEBREW = "/usr/local/tomcat/webapps/ROOT/fonts/noto-sans-hebrew/NotoSansHebrew-Regular.ttf";
    private final String NOTOSANSCHINESE = "/usr/local/tomcat/webapps/ROOT/fonts/noto-sans-chinese/NotoSansCJKtc-Regular.otf";
    private final String NOTOSANSJAPANESE = "/usr/local/tomcat/webapps/ROOT/fonts/noto-sans-japanese/NotoSansCJKjp-Regular.otf";


    // A4 595 pixels x 842 pixels
    public void writePdf(Item item, String docId, String page, HttpServletResponse response) {

        try {
            JSONObject pageMetadata = item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(0);

            response.setContentType("application/pdf");
            PdfDocument pdf = new PdfDocument(new PdfWriter(response.getOutputStream()));
            Document document = new Document(pdf, PageSize.A4, false);
            document.setMargins(10f, 10f, 100f, 10f);

            // Set font
            FontProgram fontProgram1 = FontProgramFactory.createFont(NOTOSANS);
            FontProgram fontProgram2 = FontProgramFactory.createFont(NOTOSANSARABIC);
            FontProgram fontProgram3 = FontProgramFactory.createFont(NOTOSANSHEBREW);
            FontProgram fontProgram4 = FontProgramFactory.createFont(NOTOSANSCHINESE);
            FontProgram fontProgram5 = FontProgramFactory.createFont(NOTOSANSJAPANESE);
            //PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);
            //font.setSubset(false);

            FontProvider fontProvider = new FontProvider();
            fontProvider.addFont(fontProgram1, PdfEncodings.IDENTITY_H);
            fontProvider.addFont(fontProgram2, PdfEncodings.IDENTITY_H);
            fontProvider.addFont(fontProgram3, PdfEncodings.IDENTITY_H);
            fontProvider.addFont(fontProgram4, PdfEncodings.IDENTITY_H);
            fontProvider.addFont(fontProgram5, PdfEncodings.IDENTITY_H);

/*            ConverterProperties properties = new ConverterProperties();
            properties.setFontProvider(fontProvider);
            List<IElement> iElements = HtmlConverter.convertToElements(item.getAbstract());
            for (IElement element : iElements) {
                document.add((IBlockElement)element);
            }*/

            document.setFontProvider(fontProvider);
            document.setFont(NOTOSANS); // needed to initalise fontProvider

            // Formatting
            document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1.02f));

            // Body
            JSONObject pageJSON = item.getJSON().getJSONArray("pages").getJSONObject(Integer.parseInt(page) - 1);
            String iiifImageURL = pageJSON.getString("IIIFImageURL");

            String imageURL = IIIF_ROOT + iiifImageURL + "/full/400,/0/default.jpg";
            Image image = new Image(ImageDataFactory.create(imageURL));

            document.add(new Paragraph(item.getTitle() +" ("+item.getId()+")").setFontSize(14).setBold());
            document.add(new Paragraph(Jsoup.parse(item.getAbstract()).text()));

            document.add(image.setMargins(10f, 10f, 20f, 10f));

            // Metadata Table
            // Table table = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();
            float[] columnWidths = {5, 10};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));

            // display, label, displayForm
            for (String key : pageMetadata.keySet()) {

                Object o = pageMetadata.get(key);
                if (o instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) o;

                    if (jsonObj.has("display") &&
                        jsonObj.get("display").equals(true) &&
                        jsonObj.has("label")) {

                        // label
                        Cell labelCell = new Cell();
                        labelCell.add(new Paragraph(jsonObj.getString("label")).setFontSize(10)
                            .setFontColor(ColorConstants.WHITE));
                        labelCell.setBackgroundColor(new DeviceRgb(30, 118, 128));
                        labelCell.setBorder(Border.NO_BORDER);
                        table.addCell(labelCell);

                        // value(s)
                        if (jsonObj.has("displayForm")) {
                            table.addCell(Jsoup.parse(jsonObj.getString("displayForm")).text()).setFontSize(10);
                        }

                        if (jsonObj.has("value")) {
                            StringBuilder valueString = new StringBuilder();
                            for (Object v : jsonObj.getJSONArray("value")) {
                                JSONObject value = (JSONObject) v;
                                if (value.has("display") && value.get("display").equals(true)) {
                                    valueString.append(value.getString("displayForm")).append(" ; ");
                                }
                            }
                            table.addCell(Jsoup.parse(valueString.toString()).text()).setFontSize(10);
                        }
                    }

                }
            }

            document.add(new Paragraph("Item Metadata").setBold());
            document.add(table);

            // Set Header
            Paragraph header = getHeader(HEADER_TEXT);

            for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
                Rectangle pageSize = document.getPdfDocument().getPage(i).getPageSize();
                float x = pageSize.getWidth() - 20;
                float y = pageSize.getTop() - 20;
                document.showTextAligned(header, x, y, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
            }

            // Set Footer
            StringBuilder footerString = new StringBuilder();
            if (pageMetadata.has("displayImageRights")) {
                footerString.append(pageMetadata.getString("displayImageRights"));
            }
            if (pageMetadata.has("metadataImageRights")) {
                footerString.append(pageMetadata.getString("metadataRights"));
            }

            Paragraph footer = getFooter(footerString.toString());
            for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
                Rectangle pageSize = document.getPdfDocument().getPage(i).getPageSize();
                float x = pageSize.getWidth() - 20;
                float y = pageSize.getBottom() + 20;
                document.showTextAligned(footer, x, y, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
            }

            document.flush();
            document.close();
            response.flushBuffer();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private Paragraph getHeader(String text) {

        Paragraph header = null;
        try {
            header = new Paragraph(text)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(10)
                .setFontColor(ColorConstants.DARK_GRAY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return header;

    }

    private Paragraph getFooter(String text) {

        Paragraph footer = null;
        try {
            footer = new Paragraph(text)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(10)
                .setFontColor(ColorConstants.DARK_GRAY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return footer;

    }

}
