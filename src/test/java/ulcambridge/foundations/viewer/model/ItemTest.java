package ulcambridge.foundations.viewer.model;

import com.google.common.collect.ImmutableList;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit test for testing individual items
 */
public class ItemTest {
    private Person getTestPerson() {
        return new Person("Test Person, 2012", "Test Person",
                "test authority ID", "test authority", "test value uri",
                "test type", "aut");
    }

    private Item getTestItem1() {
        final List<Person> authors = ImmutableList.of(getTestPerson());
        final List<String> pageLabels = Collections.emptyList();
        final List<String> pageThumbnailURLs = Collections.emptyList();

        JSONObject json = new JSONObject();

        return new Item("Test-ID", "bookormanuscript", "Test Title", authors,
                "test shelfLocator", "test abstract", "test thumbnail URL",
                "test thumbnail orientation", "http://example.com",
                pageLabels, pageThumbnailURLs,
                true, false, json);
    }

    private Item getTestItem2() {
        final List<Person> authors = ImmutableList.of(getTestPerson());
        final List<String> pageLabels = Collections.emptyList();
        final List<String> pageThumbnailURLs = Collections.emptyList();

        JSONObject json = new JSONObject();

        return new Item(
                "Test-ID2",
                "bookormanuscript",
                "Test Title",
                authors,
                "test shelfLocator",
                "test abstract this is a longer abstract that will need to be shortened for display. <div class='videoCaption' > this is a video caption </div> this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display.",
                "test thumbnail URL", "test thumbnail orientation",
                "http://example.com",
                pageLabels, pageThumbnailURLs, false, true, json);
    }

    /**
     * Tests the Item object
     * @throws JSONException
     */
    @Test
    public void testItem() throws JSONException {
        final Item item = getTestItem1();
        final Item item2 = getTestItem2();

        assertEquals("Test-ID", item.getId());
        assertEquals("test abstract", item.getAbstract());
        assertEquals("test abstract", item.getAbstractShort());
        assertTrue(item2.getAbstractShort().length() < item2.getAbstract().length());
        assertNotEquals(item2.getAbstract().indexOf("this is a video caption"), -1);
        assertEquals(item2.getAbstractShort().indexOf("this is a video caption"), -1);
        assertEquals(1, item.getAuthors().size());
        assertEquals("test shelfLocator", item.getShelfLocator());
        assertEquals("test thumbnail orientation", item.getThumbnailOrientation());
        assertEquals("http://example.com", item.getImageReproPageURL());
        assertEquals("test thumbnail URL", item.getThumbnailURL());
        assertEquals("Test Title", item.getTitle());
        assertTrue(item.getIIIFEnabled());
        assertFalse(item2.getIIIFEnabled());
        assertFalse(item.getTaggingStatus());
        assertTrue(item2.getTaggingStatus());

        assertTrue(item.compareTo(item2) < 0);
        assertTrue(item2.compareTo(item) > 0);
        assertEquals(item.compareTo(item), 0);
        assertNotEquals(item, item2);
        assertEquals(item, item);
        assertNotEquals(item.hashCode(), item2.hashCode());

        final Person a = item.getAuthors().get(0); // should be only one
        assertEquals("test authority", a.getAuthority());
        assertEquals("test authority ID", a.getAuthorityURI());
        assertEquals("Test Person", a.getDisplayForm());
        assertEquals("Test Person, 2012", a.getFullForm());
        assertEquals("aut", a.getRole());
        assertEquals("test type", a.getType());
        assertEquals("test value uri", a.getValueURI());
    }

    @Test
    public void simpleJSON() {
        JSONObject item = getTestItem1().getSimplifiedJSON();

        assertThat(item.length()).isEqualTo(9);
        assertThat(item.getJSONArray("authors").length()).isEqualTo(1);
        JSONObject author = item.getJSONArray("authors").getJSONObject(0);
        assertThat(author.length()).isEqualTo(7);
        assertThat(author.getString("authority")).isEqualTo("test authority");
        assertThat(author.getString("authorityURI")).isEqualTo("test authority ID");
        assertThat(author.getString("displayForm")).isEqualTo("Test Person");
        assertThat(author.getString("fullForm")).isEqualTo("Test Person, 2012");
        assertThat(author.getString("role")).isEqualTo("aut");
        assertThat(author.getString("type")).isEqualTo("test type");
        assertThat(author.getString("valueURI")).isEqualTo("test value uri");

        assertThat(item.getString("id")).isEqualTo("Test-ID");
        assertThat(item.getString("title")).isEqualTo("Test Title");
        assertThat(item.getString("shelfLocator")).isEqualTo("test shelfLocator");
        assertThat(item.getString("abstractText")).isEqualTo("test abstract");
        assertThat(item.getString("abstractShort")).isNotNull();
        assertThat(item.getString("abstractShort")).isEqualTo(getTestItem1().getAbstractShort());
        assertThat(item.getString("thumbnailURL")).isEqualTo("test thumbnail URL");
        assertThat(item.getString("thumbnailOrientation")).isEqualTo("test thumbnail orientation");
        assertThat(item.getString("imageReproPageURL")).isEqualTo("http://example.com");
    }
}
