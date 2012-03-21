package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

public class CollectionFactory {

	private static Hashtable<String, Collection> collections;

	// load in collections from properties files.
	private static boolean collectionsInitalised = initCollections();

	/**
	 * Initalise the collections hashtable from information in the collections
	 * properties file.
	 * 
	 * @return hashtable of collectionID to collection object.
	 */
	private synchronized static boolean initCollections() {

		collections = new Hashtable<String, Collection>();

		// Get collection url and title
		String[] collectionIds = Properties.getString("collections").split(
				"\\s*,\\s*");
		for (int i = 0; i < collectionIds.length; i++) {

			String collectionId = collectionIds[i];
			List<String> collectionItemIds = Arrays.asList(Properties
					.getString(collectionId + ".items").split("\\s*,\\s*"));
			List<Item> collectionItems = ItemFactory.getItems(collectionId);
			String collectionTitle = Properties.getString(collectionId
					+ ".title");
			String collectionSummary = Properties.getString(collectionId
					+ ".summary");
			String collectionSponsors = Properties.getString(collectionId
					+ ".sponsors");
			String collectionType = Properties
					.getString(collectionId + ".type");

			Collection collection = new Collection(collectionId,
					collectionTitle, collectionItemIds, collectionItems,
					collectionSummary, collectionSponsors, collectionType);
			collections.put(collectionId, collection);
		}

		return true;

	}

	public static Collection getCollectionFromId(String id) {

		if (collectionsInitalised) {
			return collections.get(id);
		}
		return null;
	}
	
	/**
	 * Returns the first collection with the given title or null
	 * if no collections exist with that title. 
	 * 
	 * @param title
	 * @return
	 */
	public static Collection getCollectionFromTitle(String title) {

		if (collectionsInitalised) {
			Iterator<Collection> c = CollectionFactory.getCollections().iterator();
			
			while (c.hasNext()) {
				Collection collection = c.next();
				if (collection.getTitle().equals(title)) {
					return collection;
				}
			}
		}
		return null;
	}

	public static List<Collection> getCollections() {

		if (collectionsInitalised) {
			ArrayList<Collection> list = new ArrayList<Collection>(
					collections.values());
			Collections.sort(list);
			return list;
		}
		return null;
	}

}
