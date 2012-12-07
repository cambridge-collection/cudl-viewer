package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Item;

public class ItemFactory {

	// Stores a hashtable of all the items in a collection indexed by
	// CollectionId
	private static HashMap<String, Item> itemCache;
	private final int MAXCACHE = 200; // max number of items in the cache. 
	private CollectionsDao collectionsDao;
	private ItemsDao itemsDao;	
	private Calendar lastInit;
	private final int INIT_TIMEOUT = 60000; // in milliseconds

	@Autowired
	public void setCollectionsDao(CollectionsDao dao) {
		collectionsDao = dao;
	}
	
	@Autowired
	public void setItemsDao(ItemsDao dao) {
		itemsDao = dao;
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
	 * NOTE: Gets item from cache if available else gets item from JSON. 
	 * 
	 * @param id
	 * @return
	 */
	public Item getItemFromId(String id) {
		
		// TODO caching!
		return itemsDao.getItem(id);
		
	}

	/**
	 * Returns a list of all items in the specified collection
	 * 
	 * @param collectionId
	 * @return
	 */
	public List<Item> getItemsFromCollectionId(String collectionId) {
		
		List<Item> items = new ArrayList<Item>();
		List<String> ids = collectionsDao.getCollectionIds();
		for (int i=0; i<ids.size(); i++) {
		  String id = ids.get(i);
		  items.add(getItemFromId(id));
		}
		
		return items;
	}


}
