package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.dao.CollectionsDAO;
import ulcambridge.foundations.viewer.dao.ItemsDAO;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;

public class ItemFactory {

	// Stores a hashtable of all the items in a collection indexed by
	// CollectionId
	private static Hashtable<String, Hashtable<String, Item>> itemsInCollection;
	private CollectionsDAO collectionsDAO;
	private ItemsDAO itemsDAO;	
	private Calendar lastInit;
	private int INIT_TIMEOUT = 60000; // in milliseconds

	@Autowired
	public void setCollectionsDAO(CollectionsDAO dao) {
		collectionsDAO = dao;
	}
	
	@Autowired
	public void setItemsDAO(ItemsDAO dao) {
		itemsDAO = dao;
	}	

	public synchronized void init() {
		
		// do not run again if it has already run in the last 1 minute
		Calendar now = Calendar.getInstance();
		if (lastInit!=null) {			
			if (lastInit.getTimeInMillis()+INIT_TIMEOUT>now.getTimeInMillis()) {
				return;
			}
		}
		lastInit = now;
				
		itemsInCollection = new Hashtable<String, Hashtable<String, Item>>();	
		
		List<String> collectionIds = collectionsDAO.getCollectionIds();
		for (int i = 0; i < collectionIds.size(); i++) {
			String collectionId = collectionIds.get(i);
			initItems(collectionId);
		}
		
     }

	/**
	 * Initalise the collections hashtable from information in the collections
	 * database.
	 */
	private synchronized void initItems(String collectionId) {

		Hashtable<String, Item> items = new Hashtable<String, Item>();

		Collection collection = collectionsDAO.getCollection(collectionId);
		List<String> itemIds = collection.getItemIds();  
				
		for (int i = 0; i < itemIds.size(); i++) {

			String itemId = itemIds.get(i);

			if (itemId == null || itemId.equals("")) {
				throw new IllegalArgumentException("Invalid Item Id given.");
			}

			Item item = itemsDAO.getItem(itemId);

			if (item!=null) {
			  items.put(itemId, item);
			}

		}

		if (!items.isEmpty()) {
		  itemsInCollection.put(collectionId, items);
		}
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
	 * Returns the first matching item for that id in any collection.
	 * 
	 * @param id
	 * @return
	 */
	public Item getItemFromId(String id) {

		if (itemsInCollection==null || itemsInCollection.isEmpty()) {init();}
		
		Enumeration<String> collections = itemsInCollection
				.keys();
		while (collections.hasMoreElements()) {
			Hashtable<String, Item> items = itemsInCollection
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
	public List<Item> getItemsFromCollectionId(String collectionId) {
		
		if (itemsInCollection==null || itemsInCollection.isEmpty()) {init();}
		
		Hashtable<String, Item> items = itemsInCollection
				.get(collectionId);
		ArrayList<Item> list = new ArrayList<Item>(items.values());
		Collections.sort(list);

		return list;
	}


}
