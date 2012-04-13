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

	private static final CollectionFactory collectionFactory = new CollectionFactory();

	private Hashtable<String, Collection> collections;

	/**
	 * Protected constructor.
	 */
	protected CollectionFactory() {
			collections = new Hashtable<String, Collection>();

			// Get collection url and title
			String[] collectionIds = Properties.getString("collections").split(
					"\\s*,\\s*");
			for (int i = 0; i < collectionIds.length; i++) {

				String collectionId = collectionIds[i];
				List<String> collectionItemIds = Arrays.asList(Properties
						.getString(collectionId + ".items").split("\\s*,\\s*"));

				List<Item> collectionItems = ItemFactory.getItemsFromCollectionId(collectionId);
				String collectionTitle = Properties.getString(collectionId
						+ ".title");
				String collectionSummary = Properties.getString(collectionId
						+ ".summary");
				String collectionSponsors = Properties.getString(collectionId
						+ ".sponsors");
				String collectionType = Properties.getString(collectionId
						+ ".type");

				Collection collection = new Collection(collectionId,
						collectionTitle, collectionItemIds, collectionItems,
						collectionSummary, collectionSponsors, collectionType);
				collections.put(collectionId, collection);
			}
	}

	public static Collection getCollectionFromId(String id) {

		return collectionFactory.collections.get(id);

	}

	/**
	 * Returns the first collection with the given title or null if no
	 * collections exist with that title.
	 * 
	 * @param title
	 * @return
	 */
	public static Collection getCollectionFromTitle(String title) {

		Iterator<Collection> c = getCollections().iterator();

		while (c.hasNext()) {
			Collection collection = c.next();
			if (collection.getTitle().equals(title)) {
				return collection;
			}
		}

		return null;
	}

	public static List<Collection> getCollections() {

		ArrayList<Collection> list = new ArrayList<Collection>(
				collectionFactory.collections.values());
		Collections.sort(list);
		return list;

	}

}
