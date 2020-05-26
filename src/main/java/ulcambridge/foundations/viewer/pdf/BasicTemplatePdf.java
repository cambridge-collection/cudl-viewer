package ulcambridge.foundations.viewer.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.*;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ulcambridge.foundations.viewer.model.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

class BasicTemplatePdf {

    private final String baseURL;
    private final String headerText;
    private final int[] pdfColour;
    private final String defaultFont;
    private final String[] fontDirectories;
    private final String cachePath;

    BasicTemplatePdf(String baseURL,
                     String headerText, int[] pdfColour,
                     String[] urlsForFontZips, String defaultFont,
                     String cachePath) throws MalformedURLException {
        this.baseURL = baseURL;
        this.headerText = headerText;
        this.pdfColour = pdfColour;
        this.defaultFont = defaultFont;
        this.cachePath = cachePath;
        this.fontDirectories = new String[urlsForFontZips.length];
        for (int i=0; i<urlsForFontZips.length; i++) {
            fontDirectories[i] = ExtractZipToTempDirectory(new URL(urlsForFontZips[i]));
        }
        // create cache dir if not exists
        File dir = new File(this.cachePath);
        if (!this.cachePath.trim().equals("") && !dir.exists()){
            dir.mkdirs();
        }
    }

    void writePdf(Item item, Div images, HttpServletResponse response, boolean cache) {

        try {
            response.setContentType("application/pdf");

            OutputStream os;
            // Return version from cache if possible
            if (cache) {
                if (existsInCache(item.getId()+".pdf")) {
                    streamPdfFromCache(item.getId()+".pdf", response.getOutputStream());
                    return;
                }

                File f = getCacheFile(item.getId()+".pdf");
                os = new FileOutputStream(f); // write to file instead of stream
            } else {
                // default to stream out pdf
                os = response.getOutputStream();
            }

            JSONObject pageMetadata = item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(0);
            PdfDocument pdf = new PdfDocument(new PdfWriter(os));
            Document document = new Document(pdf, PageSize.A4, false);
            document.setMargins(30f, 30f, 100f, 30f);

            // Set font
            // Setup font provider, note: FIRST listed font is always default.
            FontProvider fontProvider = new FontProvider();
            for (String fontDir: fontDirectories) {
                fontProvider.addDirectory(fontDir);
            }
            document.setFontProvider(fontProvider);
            document.setFont(defaultFont); // needed to initalise fontProvider

            // Formatting
            document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1.02f));

            // Title
            document.add(new Paragraph(item.getTitle() + " (" + item.getId() + ")")
                .setFontColor(new DeviceRgb(pdfColour[0], pdfColour[1], pdfColour[2])).setFontSize(14).setBold());

            document.add(images);

            // Abstract
            List<IBlockElement> elements = parseHTML(item.getAbstract(), fontProvider);
            for (IBlockElement e : elements) {
                document.add(e);
            }

            // Metadata Table
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
                        labelCell.setBackgroundColor(new DeviceRgb(pdfColour[0], pdfColour[1], pdfColour[2]));
                        labelCell.setBorder(Border.NO_BORDER);
                        table.addCell(labelCell);

                        // value(s)
                        if (jsonObj.has("displayForm")) {
                            List<IBlockElement> displayElements = parseHTML(jsonObj.getString("displayForm"), fontProvider);
                            Div div = new Div();
                            for (IBlockElement e : displayElements) {
                                div.add(e);
                            }
                            table.addCell(div).setFontSize(10);
                        }

                        if (jsonObj.has("value")) {
                            StringBuilder valueString = new StringBuilder();
                            for (Object v : jsonObj.getJSONArray("value")) {
                                JSONObject value = (JSONObject) v;
                                if (value.has("display") && value.get("display").equals(true)) {
                                    valueString.append(value.getString("displayForm")).append(" ; ");
                                }
                            }
                            valueString.replace(valueString.lastIndexOf(" ;"), valueString.lastIndexOf(" ;") + 2, "");
                            List<IBlockElement> valueElements = parseHTML(valueString.toString(), fontProvider);
                            Div div = new Div();
                            for (IBlockElement e : valueElements) {
                                div.add(e);
                            }
                            table.addCell(div).setFontSize(10);
                        }
                    }
                }
            }

            document.add(new Paragraph("Item Metadata").setBold());
            document.add(table);

            // Set Header
            Paragraph header = getHeader(headerText);

            for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
                Rectangle pageSize = document.getPdfDocument().getPage(i).getPageSize();
                float x = pageSize.getWidth() - 20;
                float y = pageSize.getTop() - 20;
                document.showTextAligned(header, x, y, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
            }

            // Set Footer
            for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
                Rectangle pageSize = document.getPdfDocument().getPage(i).getPageSize();
                float x = pageSize.getWidth() - 20;
                float y = pageSize.getBottom() + 20;
                if (pageMetadata.has("displayImageRights")) {
                    Paragraph p = getFooter(pageMetadata.getString("displayImageRights"));
                    document.showTextAligned(p, x, y, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
                }
                if (pageMetadata.has("metadataRights")) {
                    Paragraph p = getFooter(pageMetadata.getString("metadataRights"));
                    document.showTextAligned(p, x, y - 14, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
                }
            }

            document.flush();
            document.close();

            // Cache
            if (cache) {
                streamPdfFromCache(item.getId()+".pdf", response.getOutputStream());
            }

            response.flushBuffer();

        } catch (Exception e) {
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

    private List<IBlockElement> parseHTML(String input, FontProvider provider) {

        // Update internal links
        org.jsoup.nodes.Document html = Jsoup.parse(input);
        Elements links = html.select("a");
        for (Element link : links) {
            if (!link.attr("href").trim().equals("") &&
                !link.attr("href").startsWith("http") &&
                !link.attr("href").startsWith("//")) {
                // add external url to relative links
                link.attr("href", baseURL + link.attr("href"));
            }
        }

        ConverterProperties properties = new ConverterProperties();
        properties.setFontProvider(provider);
        properties.setImmediateFlush(false);

        List<IBlockElement> elements = new ArrayList<>();
        List<IElement> iElements = HtmlConverter.convertToElements(html.body().toString(), properties);
        for (IElement element : iElements) {
            elements.add((IBlockElement) element);
        }
        return elements;
    }

    private String ExtractZipToTempDirectory(URL zipURL) {
        try {
            // Return version from cache if possible
            String filename = URLEncoder.encode(zipURL.toString(), "UTF-8");
            if (existsInCache(filename)) {
                return getCacheFile(filename).getCanonicalPath();
            }

            // Get Zip File
            File f = getCacheFile(URLEncoder.encode(zipURL.toString(), "UTF-8") +".zip");
            File dir = getCacheFile(URLEncoder.encode(zipURL.toString(), "UTF-8"));

            FileUtils.copyURLToFile(zipURL,f);

            // Extract zip
            ZipFile zipFile = new ZipFile(f);
            zipFile.extractAll(dir.getCanonicalPath());

            return dir.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private boolean existsInCache(String filename) {
        File file = getCacheFile(filename);
        return file.exists() && file.canRead();
    }

    private File getCacheFile(String filename) {
        return new File(this.cachePath+File.separator+filename);
    }

    private void streamPdfFromCache(String filename, OutputStream outputStream) throws IOException {
        InputStream inputStream = FileUtils.openInputStream(getCacheFile(filename));
        IOUtils.copy(inputStream,outputStream);
        outputStream.flush();;
        outputStream.close();
    }
}

