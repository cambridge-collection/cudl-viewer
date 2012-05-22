package ulcambridge.foundations.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;
import ulcambridge.foundations.viewer.model.Properties;

public class ItemFactory {

	// Only want one instance of itemFactory
	private static ItemFactory itemFactory;
	private static JSONReader reader = new JSONReader();

	// Stores a hashtable of all the items in a collection indexed by
	// CollectionId
	private Hashtable<String, Hashtable<String, Item>> itemsInCollection = new Hashtable<String, Hashtable<String, Item>>();

	/**
	 * Protected constructor.
	 */
	private ItemFactory() {

		String[] collections = Properties.getString("collections").trim()
				.split(",");
		for (int i = 0; i < collections.length; i++) {
			initItems(collections[i]);
		}

	}

	public static ItemFactory getItemFactory() {
		if (itemFactory == null) {
			itemFactory = new ItemFactory();
		}
		return itemFactory;
	}

	/**
	 * Initalise the collections hashtable from information in the collections
	 * properties file.
	 */
	private void initItems(String collectionId) {

		Hashtable<String, Item> items = new Hashtable<String, Item>();

		String[] itemIds = Properties.getString(collectionId + ".items").trim()
				.split("\\s*,\\s*");
		for (int i = 0; i < itemIds.length; i++) {

			String itemId = itemIds[i];
			String itemTitle = "";
			List<Person> itemAuthors = new ArrayList<Person>();
			String itemShelfLocator = "";
			String itemAbstract = "";
			String itemThumbnailURL = "";
			String thumbnailOrientation = "";
			JSONObject itemJson = null;

			if (itemId == null || itemId.equals("")) {
				throw new IllegalArgumentException("Invalid Item Id given.");
			}
			try {
				itemJson = reader.readJsonFromUrl(Properties
						.getString("jsonURL") + itemId + ".json");

				// Pull out the information we want in our Item object
				JSONObject descriptiveMetadata = itemJson.getJSONArray(
						"descriptiveMetadata").getJSONObject(0);

				// Should always have title
				itemTitle = descriptiveMetadata.getJSONObject("title").getString("displayForm");

				// Might have authors, might not.
				if (descriptiveMetadata.has("authors")) {
					itemAuthors = getPeopleFromJSON(descriptiveMetadata
							.getJSONObject("authors").getJSONArray("value"), "author");
				}

				// Might have shelf locator, might not.
				if (descriptiveMetadata.has("shelfLocator")) {
					itemShelfLocator = descriptiveMetadata
							.getJSONObject("shelfLocator").getString("displayForm");
				}

				// Might have abstract, might not.
				if (descriptiveMetadata.has("abstract")) {
					itemAbstract = descriptiveMetadata.getJSONObject("abstract").getString("displayForm");
				}

				// Might have Thumbnail image
				if (descriptiveMetadata.has("thumbnailUrl")
						&& descriptiveMetadata.has("thumbnailOrientation")) {
					itemThumbnailURL = descriptiveMetadata
							.getString("thumbnailUrl");
					if (Properties.getString("useProxy").equals("true")) {
						itemThumbnailURL = Properties.getString("proxyURL")
								+ itemThumbnailURL;
					}

					thumbnailOrientation = descriptiveMetadata
							.getString("thumbnailOrientation");
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Item item = new Item(itemId, itemTitle, itemAuthors,
					itemShelfLocator, itemAbstract, itemThumbnailURL,
					thumbnailOrientation, itemJson);

			items.put(itemId, item);

		}

		itemsInCollection.put(collectionId, items);

	}

	/*
	 * private Dimension getWidthHeightImage(URL url) {
	 * 
	 * ImageIcon icon = new ImageIcon(url); return new
	 * Dimension(icon.getIconWidth(), icon.getIconHeight());
	 * 
	 * }
	 */

	/**
	 * Get the item with the given id, which exists in the specified collection
	 * 
	 * @param id
	 * @param collectionId
	 * @return
	 */
	public static Item getItemFromId(String id, String collectionId) {
		Hashtable<String, Item> items = getItemFactory().itemsInCollection
				.get(collectionId);
		return items.get(id);
	}

	/**
	 * Returns the first matching item for that id in any collection.
	 * 
	 * @param id
	 * @return
	 */
	public static Item getItemFromId(String id) {

		Enumeration<String> collections = getItemFactory().itemsInCollection
				.keys();
		while (collections.hasMoreElements()) {
			Hashtable<String, Item> items = getItemFactory().itemsInCollection
					.get(collections.nextElement());
			Item item = items.get(id);
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns a list of all items in the specified collection
	 * 
	 * @param collectionId
	 * @return
	 */
	public static List<Item> getItemsFromCollectionId(String collectionId) {
		Hashtable<String, Item> items = getItemFactory().itemsInCollection
				.get(collectionId);
		ArrayList<Item> list = new ArrayList<Item>(items.values());
		Collections.sort(list);

		return list;
	}

	/**
	 * This method takes a JSONArray and creates Person objects for each JSON
	 * object in it.
	 * 
	 * @param names
	 * @return
	 */
	private List<Person> getPeopleFromJSON(JSONArray json, String role) {

		ArrayList<Person> people = new ArrayList<Person>();

		if (json == null) {
			return people;
		}

		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject personJSON = json.getJSONObject(i);
				String authForm = personJSON.getString("authForm");
				String shortForm = personJSON.getString("shortForm");
				String authority = personJSON.getString("authority");
				String authorityURI = personJSON.getString("authorityURI");
				String valueURI = personJSON.getString("valueURI");
				String type = personJSON.getString("type");
				Person person = new Person(authForm, shortForm, authority,
						authorityURI, valueURI, type, role);
				people.add(person);
			}

		} catch (JSONException e) {

			System.err.println("Error processing: "+json);			
			e.printStackTrace();
		}

		return people;
	}

}
