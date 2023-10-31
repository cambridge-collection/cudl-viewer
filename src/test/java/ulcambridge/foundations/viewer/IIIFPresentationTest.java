package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;
import ulcambridge.foundations.viewer.model.UI;
import ulcambridge.foundations.viewer.model.UIThemeData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class IIIFPresentationTest {

    private static final URI IIIF_URL = URI.create("https://image02.cudl.lib.cam.ac.uk/iiif/");

    private static final Person PERSON = new Person(
        "Mr Joe Bloggs",
        "J Bloggs",
        "http://authority.test",
        "Test Authority",
        "http://authority.test/jbloggs",
        "person",
        "aut"
    );

    private static final UI themeUI = new UI("","", null,null);

    private Object parseResource(String path) throws IOException {
        try (InputStreamReader in = new InputStreamReader(new ClassPathResource(path).getInputStream())) {
            return new JSONObject(new JSONTokener(in));
        }
    }

    @Test
    public void testIIIFAnnotationListFromTranscription() throws Exception {
        Item item = new Item(
            "MS-ADD-03958",
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
            (JSONObject) parseResource("cudl-data/MS-ADD-03958.json")
        );

        assertEquals(item.getJSON().get("useTranscriptions"),true);
        assertEquals(item.getJSON().get("useNormalisedTranscriptions"),true);
        assertEquals(item.getJSON().get("useDiplomaticTranscriptions"),true);
        assertEquals(item.getJSON().getJSONArray("pages").getJSONObject(110).get("transcriptionDiplomaticURL"),
            "/v1/transcription/newton/diplomatic/external/NATP00093/NATP00100-p058r/NATP00100-p058r");

        IIIFPresentation presentation = new IIIFPresentation(
            item, "http://base.test", "http://service.test", IIIF_URL,
            themeUI);
        JSONObject output = presentation.outputJSON();
        JSONObject otherContentItem =
            output.getJSONArray("sequences").getJSONObject(0).getJSONArray("canvases").getJSONObject(110)
            .getJSONArray("otherContent").getJSONObject(0);

        assertEquals("http://iiif.io/api/presentation/2/context.json", output.getString("@context"));
        assertEquals("http://service.test/v1/iiif/annotation/MS-ADD-03958/111",
            otherContentItem.get("@id"));
        assertEquals("sc:AnnotationList", otherContentItem.get("@type"));
    }

    @Test
    public void fullItem() throws Exception {
        Item item = new Item(
            "MS-ADD-04004",
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
            (JSONObject) parseResource("cudl-data/MS-ADD-04004.json")
        );
        IIIFPresentation presentation = new IIIFPresentation(
            item, "http://base.test", "http://service.test", IIIF_URL,
            themeUI);

        JSONObject output = presentation.outputJSON();
        assertEquals("http://iiif.io/api/presentation/2/context.json", output.getString("@context"));
        assertEquals("sc:Manifest", output.getString("@type"));
        assertEquals("http://base.test/iiif/MS-ADD-04004", output.getString("@id"));
        assertEquals("Item Title (LOC)", output.getString("label"));
        assertEquals("Item Abstract", output.getString("description"));
        assertThat(output.getString("attribution"), startsWith("Provided by Cambridge University Library."));
        assertEquals("http://base.test/img/cu_logo.png", output.getString("logo"));
        assertEquals("left-to-right", output.getString("viewingDirection"));
        assertEquals("http://service.test/v1/metadata/tei/MS-ADD-04004/", output.getString("seeAlso"));
        assertEquals(14, output.getJSONArray("metadata").length());
        assertEquals(1, output.getJSONArray("structures").length());

        JSONArray sequences = output.getJSONArray("sequences");
        assertEquals(1, sequences.length());
        JSONArray canvases = sequences.getJSONObject(0).getJSONArray("canvases");
        assertEquals(935, canvases.length());
        JSONObject canvas = canvases.getJSONObject(0);
        assertEquals("http://base.test/iiif/MS-ADD-04004/canvas/1", canvas.getString("@id"));
        assertEquals("sc:Canvas", canvas.getString("@type"));
        assertEquals("cover", canvas.getString("label"));
        assertEquals(7921, canvas.getInt("height"));
        assertEquals(5553, canvas.getInt("width"));

        JSONArray images = canvas.getJSONArray("images");
        assertEquals(1, images.length());
        JSONObject image = images.getJSONObject(0);
        assertEquals("oa:Annotation", image.getString("@type"));
        assertEquals("sc:painting", image.getString("motivation"));
        assertEquals("http://base.test/iiif/MS-ADD-04004/canvas/1", image.getString("on"));

        String imageURL = "https://image02.cudl.lib.cam.ac.uk/iiif/MS-ADD-04004-001-00001.jp2";
        JSONObject resource = image.getJSONObject("resource");
        assertEquals(imageURL, resource.getString("@id")
        );
        assertEquals("dctypes:Image", resource.getString("@type"));
        assertEquals("image/jpg", resource.getString("format"));
        assertEquals(7921, resource.getInt("height"));
        assertEquals(5553, resource.getInt("width"));

        JSONObject service = resource.getJSONObject("service");
        assertEquals("http://iiif.io/api/image/2/context.json", service.getString("@context"));
        assertEquals(imageURL, service.getString("@id"));
        assertEquals("http://iiif.io/api/image/2/level1.json", service.getString("profile"));
    }

    /** This item has two images missing */
    @Test
    public void missingImages() throws Exception {
        Item item = new Item(
            "MS-ADD-01809",
            "bookormanuscript",
            "Item Title",
            Collections.singletonList(PERSON),
            "LOC",
            "Item Abstract",
            "thumb.jpg",
            "portrait",
            "rights.html",
            Collections.nCopies(142, "Page Label"),
            Collections.nCopies(142, "pagethumb.jpg"),
            true,
            false,
            (JSONObject) parseResource("cudl-data/MS-ADD-01809.json")
        );
        IIIFPresentation presentation = new IIIFPresentation(
            item, "http://base.test", "http://service.test", IIIF_URL,
            themeUI);

        // TODO: use Logback backend for easy log assertions
        JSONObject output = presentation.outputJSON();
        JSONArray sequences = output.getJSONArray("sequences");
        assertEquals(1, sequences.length());
        JSONArray canvases = sequences.getJSONObject(0).getJSONArray("canvases");
        assertEquals(140, canvases.length());
    }

}
