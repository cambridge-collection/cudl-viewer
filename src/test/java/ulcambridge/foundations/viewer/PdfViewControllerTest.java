package ulcambridge.foundations.viewer;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;
import ulcambridge.foundations.viewer.pdf.FullDocumentPdf;
import ulcambridge.foundations.viewer.pdf.SinglePagePdf;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This tests the controller only, not PDF generation.
 */
@RunWith(MockitoJUnitRunner.class)
class PdfViewControllerTest {

    private static final String ITEM_ID = "MS-DD-00004-00017";
    private final ItemsDao itemsDao = PdfViewControllerTest::getItem;

    @Mock
    SinglePagePdf singlePagePdf;

    @Mock
    FullDocumentPdf fullDocumentPdf;

    @Mock
    HttpServletResponse response;

    private static final Person PERSON = new Person(
        "Mr Joe Bloggs",
        "J Bloggs",
        "http://authority.test",
        "Test Authority",
        "http://authority.test/jbloggs",
        "person",
        "aut"
    );

    private static Item getItem(String itemId) {
        Item item = null;
        try {
            item = new Item(
                itemId,
                "bookormanuscript",
                "Item Title",
                Collections.singletonList(PERSON),
                "LOC",
                "Item Abstract",
                "thumb.jpg",
                "portrait",
                "rights.html",
                Collections.nCopies(935, "Page Label"),
                Collections.nCopies(935, "pagethumb.jpg"),
                true,
                false,
                (JSONObject) parseResource("cudl-data/"+itemId+".json")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return item;
    }

    @BeforeEach
    void setup() {
        initMocks(this);
        doNothing().when(singlePagePdf).writePdf(any(), any(), any());
        doNothing().when(fullDocumentPdf).writePdf(any(), any());
    }

    @Test
    void handleSinglePagePDF() {
        PdfViewController pdfViewController = new PdfViewController(itemsDao, singlePagePdf, fullDocumentPdf);
        pdfViewController.handleSinglePagePDF(ITEM_ID, "page", response);
        Mockito.verify(singlePagePdf, Mockito.times(1)).writePdf(any(), any(), any());
    }

    @Test
    void handleFullDocumentPDF() {
        PdfViewController pdfViewController = new PdfViewController(itemsDao, singlePagePdf, fullDocumentPdf);
        pdfViewController.handleFullDocumentPDF(ITEM_ID, response);
        Mockito.verify(fullDocumentPdf, Mockito.times(1)).writePdf(any(), any());
    }

    @Test
    // This requires the PDFRights field in metadata
    void hasPermissionForPDFDownload() {
        boolean resultTrue = PdfViewController.hasPermissionForPDFDownload(itemsDao.getItem(ITEM_ID));
        assertTrue(resultTrue);
        boolean resultFalse = PdfViewController.hasPermissionForPDFDownload(itemsDao.getItem("MS-ADD-04004"));
        assertFalse(resultFalse);
    }

    private static Object parseResource(String path) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new ClassPathResource(path).getInputStream())) {
            return new JSONObject(new JSONTokener(in));
        }
    }
}
