package ulcambridge.foundations.viewer;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Item;

public class ItemFactory {

	private static final int MAXCACHE = 500; // max number of items in the
												// cache.
	private static Map<String, Item> itemCache = Collections
			.synchronizedMap(new ItemCache(MAXCACHE)); // cache of most recent
														// items
	private ItemsDao itemsDao;

	@Autowired
	public void setItemsDao(ItemsDao dao) {
		itemsDao = dao;
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
	 * Returns the first matching item for that id in any collection. NOTE: Gets
	 * item from cache if available else gets item from JSON.
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
			if (item != null) {
				itemCache.put(id, item);
			}
		}
		return item;

	}

	public void clearItemCache() {
		itemCache.clear();		
	}

}
