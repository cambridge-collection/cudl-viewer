package ulcambridge.foundations.viewer;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.ImageIcon;

import org.json.JSONException;
import org.json.JSONObject;

import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

public class ItemFactory {

	// Stores a hashtable of all the items in a collection indexed by
	// CollectionId
	private static Hashtable<String, Hashtable<String, Item>> itemsInCollection = new Hashtable<String, Hashtable<String, Item>>();

	/**
	 * Initalise the collections hashtable from information in the collections
	 * properties file.
	 * 
	 * @return hashtable of collectionID to collection object.
	 */
	private synchronized static void initItems(String collectionId) {

		Hashtable<String, Item> items = new Hashtable<String, Item>();

		String[] itemIds = Properties.getString(collectionId + ".items").trim()
				.split("\\s*,\\s*");
		for (int i = 0; i < itemIds.length; i++) {

			String itemId = itemIds[i];
			String itemTitle = "";
			String itemShelfLocator = "";
			String itemAbstract = "";
			String itemThumbnailURL = "";
			String thumbnailOrientation = "";

			if (itemId != null && !itemId.equals("")) {
				try {
					JSONObject json = JSONReader.readJsonFromUrl(Properties
							.getString("jsonURL") + itemId + ".json");

					JSONObject descriptiveMetadata = json.getJSONArray(
							"descriptiveMetadata").getJSONObject(0);

					itemTitle = descriptiveMetadata.getString("title");
					itemShelfLocator = descriptiveMetadata
							.getString("shelfLocator");
					itemAbstract = descriptiveMetadata.getString("abstract");

					// Thumbnails
					itemThumbnailURL = descriptiveMetadata
							.getString("thumbnailUrl");
					if (Properties.getString("useProxy").equals("true")) {
						itemThumbnailURL = Properties.getString("proxyURL")
								+ descriptiveMetadata.getString("thumbnailUrl");
					}

					thumbnailOrientation = descriptiveMetadata
							.getString("thumbnailOrientation");

				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Item item = new Item(itemId, itemTitle, itemShelfLocator,
						itemAbstract, itemThumbnailURL, thumbnailOrientation);

				items.put(itemId, item);

			}
		}

		itemsInCollection.put(collectionId, items);

	}

	public static Item getItemFromId(String id, String collectionId) {
		if (!itemsInitalised(collectionId)) {
			initItems(collectionId);
		}
		Hashtable<String, Item> items = itemsInCollection.get(collectionId);
		return items.get(id);
	}

	public static List<Item> getItems(String collectionId) {
		if (!itemsInitalised(collectionId)) {
			initItems(collectionId);
		}
		Hashtable<String, Item> items = itemsInCollection.get(collectionId);
		ArrayList<Item> list = new ArrayList<Item>(items.values());
		Collections.sort(list);

		return list;
	}

	private static boolean itemsInitalised(String collectionId) {

		if (itemsInCollection.get(collectionId) != null
				&& itemsInCollection.get(collectionId).size() > 0) {
			return true;
		}
		return false;
	}

	private static Dimension getWidthHeightImage(URL url) {

		ImageIcon icon = new ImageIcon(url);
		return new Dimension(icon.getIconWidth(), icon.getIconHeight());

	}
}
