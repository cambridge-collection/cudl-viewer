package ulcambridge.foundations.viewer.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for testing individual items
 */
public class ItemTest {
    /**
     * Tests the Item object
     * @throws JSONException
     */
    @Test
    public void testItem() throws JSONException {
        final Person aut = new Person("Test Person, 2012", "Test Person",
                "test authority ID", "test authority", "test value uri",
                "test type", "aut");

        final List<Person> authors = Arrays.asList(aut);

        final List<String> pageLabels = new ArrayList<>();
        final List<String> pageThumbnailURLs = new ArrayList<>();

        JSONObject json = new JSONObject();

        final Item item = new Item("Test-ID", "bookormanuscript", "Test Title", authors,
                "test shelfLocator", "test abstract", "test thumbnail URL",
                "test thumbnail orientation", "http://example.com",
                 pageLabels, pageThumbnailURLs,
                true, false, json);

        final Item item2 = new Item(
                "Test-ID2",
                "bookormanuscript",
                "Test Title",
                authors,
                "test shelfLocator",
                "test abstract this is a longer abstract that will need to be shortened for display. <div class='videoCaption' > this is a video caption </div> this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display.",
                "test thumbnail URL", "test thumbnail orientation",
                "http://example.com",
                pageLabels, pageThumbnailURLs, false, true, json);

        assertEquals("Test-ID", item.getId());
        assertEquals("test abstract", item.getAbstract());
        assertEquals("test abstract", item.getAbstractShort());
        assertTrue(item2.getAbstractShort().length() < item2.getAbstract().length());
        assertFalse(item2.getAbstract().indexOf("this is a video caption") == -1);
        assertTrue(item2.getAbstractShort().indexOf("this is a video caption") == -1);
        assertEquals(1, item.getAuthors().size());
        assertEquals("test shelfLocator", item.getShelfLocator());
        assertEquals("test thumbnail orientation", item.getThumbnailOrientation());
        assertEquals("http://example.com", item.getImageReproPageURL());
        assertEquals("test thumbnail URL", item.getThumbnailURL());
        assertEquals("Test Title", item.getTitle());
        assertEquals(true, item.getIIIFEnabled());
        assertEquals(false, item2.getIIIFEnabled());
        assertEquals(false, item.getTaggingStatus());
        assertEquals(true, item2.getTaggingStatus());

        assertTrue(item.compareTo(item2) < 0);
        assertTrue(item2.compareTo(item) > 0);
        assertEquals(item.compareTo(item), 0);
        assertFalse(item.equals(item2));
        assertTrue(item.equals(item));
        assertFalse(item.hashCode() == item2.hashCode());

        final Person a = item.getAuthors().get(0); // should be only one
        assertEquals("test authority", a.getAuthority());
        assertEquals("test authority ID", a.getAuthorityURI());
        assertEquals("Test Person", a.getDisplayForm());
        assertEquals("Test Person, 2012", a.getFullForm());
        assertEquals("aut", a.getRole());
        assertEquals("test type", a.getType());
        assertEquals("test value uri", a.getValueURI());
    }
}
