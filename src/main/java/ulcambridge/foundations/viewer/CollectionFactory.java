package ulcambridge.foundations.viewer;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionFactory {

	private final static ResourceBundle config = ResourceBundle
			.getBundle("collections");

	private static Hashtable<String, Collection> collections;

	private static boolean collectionsInitalised = false;
	
	/**
	 * Initalise the collections hashtable from information in the collections
	 * properties file.
	 * 
	 * @return hashtable of collectionID to collection object. 
	 */
	private synchronized static void initCollections() {

		collections = new Hashtable<String, Collection>();

		// Get collection url and title
		String[] collectionIds = config.getString("collections").split(
				"\\s*,\\s*");
		for (int i = 0; i < collectionIds.length; i++) {
			
			String collectionId = collectionIds[i];
			List<String> collectionItems = Arrays.asList(config
					.getString(collectionId + ".items").split("\\s*,\\s*"));
			String collectionURL = config.getString(collectionId + ".url");
			String collectionTitle = config.getString(collectionId + ".title");
			String collectionHTML = config.getString(collectionId + ".html");

			System.out.println("adding collection docID: "+collectionId);
			
			Collection collection = new Collection(collectionId,
					collectionTitle, collectionItems, collectionURL,
					collectionHTML);
			collections.put(collectionId, collection);
		}
		
		collectionsInitalised = true;

	}
	
	public static Collection getCollectionFromId(String id) {
		return collections.get(id);
	}
	
	public static Iterator<Collection> getCollections() {
		if (!collectionsInitalised) {
			initCollections();
		}
		return collections.values().iterator();
	}
}
