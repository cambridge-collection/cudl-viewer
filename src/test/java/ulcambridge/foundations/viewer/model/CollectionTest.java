package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.json.JSONObject;

/**
 * Unit test for testing individual collections
 */
public class CollectionTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public CollectionTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CollectionTest.class);
	}

	/**
	 * Tests the Collection object
	 */
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

		ArrayList<Person> people = new ArrayList<Person>();
		people.add(p1);
		people.add(p2);
		people.add(aut);
		
		List<String> pageLabels = new ArrayList<String>();

		Item item = new Item("Test-ID", "bookormanuscript", "Test Title", people,
				"test shelfLocator", "test abstract", "test thumbnail URL",
				"test thumbnail orientation", pageLabels, new JSONObject());

		ArrayList<String> collectionIds = new ArrayList<String>();
		collectionIds.add("Test-ID");
		ArrayList<Item> collectionItems = new ArrayList<Item>();
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

		assertEquals(c.getId(), "collectionID");
		assertEquals(c.getSponsors(), "collectionSponsors");
		assertEquals(c.getSummary(), "collectionSummary");
		assertEquals(c.getTitle(), "collectionTitle");
		assertEquals(c.getType(), "collectionType");
		assertEquals(c.getURL(), "/collections/collectionID");
		assertEquals(c.getItemIds(), collectionIds);
		assertEquals(c.compareTo(c2) < 0, true);
		assertEquals(c2.compareTo(c) > 0, true);
		assertEquals(c.compareTo(c), 0);
		assertEquals(c.equals(c2), false);
		assertEquals(c.equals(c), true);
		assertEquals(c.hashCode() == c2.hashCode(), false);

	}
}
