package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

public class CollectionFactory {

	private static Hashtable<String, Collection> collections;
	private CollectionsDao collectionsDao;
	private Calendar lastInit;
	private int INIT_TIMEOUT = 60000; // in milliseconds

	@Autowired
	public void setCollectionsDao(CollectionsDao dao) {
		collectionsDao = dao;
	}

	public synchronized void init() {

		// do not run again if it has already run in the last 1 minute
		Calendar now = Calendar.getInstance();
		if (lastInit != null) {		
			if (lastInit.getTimeInMillis() + INIT_TIMEOUT > now
					.getTimeInMillis()) {
				return;
			}
		}
		lastInit = now;

		collections = new Hashtable<String, Collection>();

		List<String> collectionIds = collectionsDao.getCollectionIds();
		for (int i = 0; i < collectionIds.size(); i++) {
			String collectionId = collectionIds.get(i);
			collections.put(collectionId,
					collectionsDao.getCollection(collectionId));
		}

	}

	public Collection getCollectionFromId(String id) {

		if (collections == null || collections.isEmpty()) {
			init();
		}
		return collections.get(id);

	}

	/**
	 * Returns the first collection with the given title or null if no
	 * collections exist with that title.
	 * 
	 * @param title
	 * @return
	 */
	public Collection getCollectionFromTitle(String title) {

		if (collections == null || collections.isEmpty()) {
			init();
		}
		Iterator<Collection> c = getCollections().iterator();

		while (c.hasNext()) {
			Collection collection = c.next();
			if (collection.getTitle().equals(title)) {
				return collection;
			}
		}

		return null;
	}

	public List<Collection> getCollections() {

		if (collections == null || collections.isEmpty()) {
			init();
		}
		ArrayList<Collection> list = new ArrayList<Collection>(
				collections.values());
		Collections.sort(list);
		return list;

	}

}
