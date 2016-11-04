package ulcambridge.foundations.viewer.model;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for testing individual collections
 */
public class CollectionTest{
    /**
     * Tests the Collection object
     */
    @Test
    public void testCollection() {

        Person p1 = new Person("Test Person 1, 2012", "Test Person 1",
                "test authority ID", "test authority", "test value uri",
                "test type", "test role");
        Person p2 = new Person("Test Person 2, 2012", "Test Person 2",
                "test authority ID", "test authority", "test value uri",
                "test type", "test role");
        Person aut = new Person("Test Person, 2012", "Test Person",
                "test authority ID", "test authority", "test value uri",
                "test type", "aut");

        ArrayList<Person> people = new ArrayList<>();
        people.add(p1);
        people.add(p2);
        people.add(aut);

        List<String> pageLabels = new ArrayList<>();
        List<String> pageThumbnailURLs = new ArrayList<>();

        Item item = new Item("Test-ID", "bookormanuscript", "Test Title", people,
                "test shelfLocator", "test abstract", "test thumbnail URL",
                "test thumbnail orientation",
                pageLabels, pageThumbnailURLs, new JSONObject());

        ArrayList<String> collectionIds = new ArrayList<>();
        collectionIds.add("Test-ID");
        ArrayList<Item> collectionItems = new ArrayList<>();
        collectionItems.add(item);

        Collection c = new Collection("collectionID", "collectionTitle",
                collectionIds, "collectionSummary",
                "collectionSponsors", "collectionType", "");

        Collection c2 = new Collection(
                "collectionID2",
                "collectionTitle2",
                collectionIds,
                "collectionSummary",
                "collectionSponsors2", "collectionType2", "");

        assertEquals("collectionID", c.getId());
        assertEquals("collectionSponsors", c.getSponsors());
        assertEquals("collectionSummary", c.getSummary());
        assertEquals("collectionTitle", c.getTitle());
        assertEquals("collectionType", c.getType());
        assertEquals("/collections/collectionID", c.getURL());
        assertEquals(collectionIds, c.getItemIds());
        assertTrue(c.compareTo(c2) < 0);
        assertTrue(c2.compareTo(c) > 0);
        assertEquals(c.compareTo(c), 0);
        assertFalse(c.equals(c2));
        assertTrue(c.equals(c));
        assertFalse(c.hashCode() == c2.hashCode());
    }
}
