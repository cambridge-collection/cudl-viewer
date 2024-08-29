package ulcambridge.foundations.viewer.model;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for testing individual collections
 */
public class CollectionTest {
    /**
     * Tests the Collection object
     */
    @Test
    public void testCollection() {

        final Person p1 = new Person("Test Person 1, 2012", "Test Person 1",
                "test authority ID", "test authority", "test value uri",
                "test type", "test role");
        final Person p2 = new Person("Test Person 2, 2012", "Test Person 2",
                "test authority ID", "test authority", "test value uri",
                "test type", "test role");
        final Person aut = new Person("Test Person, 2012", "Test Person",
                "test authority ID", "test authority", "test value uri",
                "test type", "aut");

        final List<Person> people = Arrays.asList(p1, p2, aut);

        final List<String> pageLabels = new ArrayList<>();
        final List<String> pageThumbnailURLs = new ArrayList<>();

        // Is this testing the constructor?  If not, can remove.
        final Item item = new Item("Test-ID", "bookormanuscript", "Test Title", people,
                "test shelfLocator", "test abstract", "test thumbnail URL",
                "test thumbnail orientation", "http://example.com",
                pageLabels, pageThumbnailURLs, true, false, new JSONObject());

        final List<String> collectionIds = Arrays.asList("Test-ID");

        final Collection c = new Collection("collectionID", "collectionTitle",
                collectionIds, "collectionSummary",
                "collectionSponsors", "collectionType", "", "");

        final Collection c2 = new Collection(
                "collectionID2",
                "collectionTitle2",
                collectionIds,
                "collectionSummary",
                "collectionSponsors2", "collectionType2", "", "");

        assertEquals("collectionID", c.getId());
        assertEquals("collectionSponsors", c.getSponsors());
        assertEquals("collectionSummary", c.getSummary());
        assertEquals("collectionTitle", c.getTitle());
        assertEquals("collectionType", c.getType());
        assertEquals("/collections/collectionID", c.getURL());
        assertEquals(collectionIds, c.getItemIds());
        assertEquals(collectionIds, c.getItemIds());
        assertTrue(c.compareTo(c2) < 0);
        assertTrue(c2.compareTo(c) > 0);
        assertEquals(c.compareTo(c), 0);
        assertFalse(c.equals(c2));
        assertTrue(c.equals(c));
        assertFalse(c.hashCode() == c2.hashCode());
    }
}
