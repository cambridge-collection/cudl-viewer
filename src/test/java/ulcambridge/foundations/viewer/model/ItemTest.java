package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;

/**
 * Unit test for testing individual items
 */
public class ItemTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public ItemTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ItemTest.class);
	}

	/**
	 * Tests the Item object
	 */
	public void testItem() {
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

		Item item = new Item("Test-ID", "Test Title", people,
				"test shelfLocator", "test abstract", "test thumbnail URL",
				"test thumbnail orientation");

		Item item2 = new Item(
				"Test-ID2",
				"Test Title",
				people,
				"test shelfLocator",
				"test abstract this is a longer abstract that will need to be shortened for display. <div class='videoCaption' > this is a video caption </div> this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display. this is a longer abstract that will need to be shortened for display.",
				"test thumbnail URL", "test thumbnail orientation");

		assertEquals(item.getId(), "Test-ID");
		assertEquals(item.getAbstract(), "test abstract");
		assertEquals(item.getAbstractShort(), "test abstract");
		assertEquals(item2.getAbstractShort().length()<item2.getAbstract().length(), true);
		assertEquals(item2.getAbstract().indexOf("this is a video caption")!=-1, true);
		assertEquals(item2.getAbstractShort().indexOf("this is a video caption")!=-1, false);
		
		assertEquals(item.getAuthors().size(), 1); // one author (role = "aut")
		assertEquals(item.getPeople().size(), 3);
		assertEquals(item.getShelfLocator(), "test shelfLocator");
		assertEquals(item.getThumbnailOrientation(),
				"test thumbnail orientation");
		assertEquals(item.getThumbnailURL(), "test thumbnail URL");
		assertEquals(item.getTitle(), "Test Title");

		assertEquals(item.compareTo(item2) < 0, true);
		assertEquals(item2.compareTo(item) > 0, true);
		assertEquals(item.compareTo(item), 0);
		assertEquals(item.equals(item2), false);
		assertEquals(item.equals(item), true);
		assertEquals(item.hashCode() == item2.hashCode(), false);

		Person a = item.getAuthors().get(0); // should be only one
		assertEquals(a.getAuthority(), "test authority");
		assertEquals(a.getAuthorityURI(), "test authority ID");
		assertEquals(a.getDisplayForm(), "Test Person");
		assertEquals(a.getFullForm(), "Test Person, 2012");
		assertEquals(a.getRole(), "aut");
		assertEquals(a.getType(), "test type");
		assertEquals(a.getValueURI(), "test value uri");

	}
}
