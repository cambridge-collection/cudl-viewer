package ulcambridge.foundations.viewer;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Item;

public class ItemFactory {

	private static final int MAXCACHE = 500; // max number of items in the cache.
	private static ItemCache itemCache = new ItemCache(MAXCACHE); // cache of most recent items
	private ItemsDao itemsDao;	
	private Calendar lastInit;
	private final int INIT_TIMEOUT = 60000; // in milliseconds
	
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
		
		// Bring item back from cache if possible. 
		Item item = null;
		if (itemCache.containsKey(id)) {
		  item = (Item) itemCache.get(id);		  
		} else {
		  item = itemsDao.getItem(id);
		  if (item!=null) { itemCache.put(id, item); }
		}
		return item;
		
	}


}
