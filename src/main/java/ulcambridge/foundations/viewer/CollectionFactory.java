package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;


@Component
public class CollectionFactory {

    private static Map<String, Collection> collections;
    private static List<Collection> rootCollections;
    private CollectionsDao collectionsDao;
    private static int collectionsRowCount;
    private static int itemsRowCount;
    private static int itemsinCollectionRowCount;
    private static Set<String> allItemIds;
    private final String cachingEnabled;

    @Autowired
    public CollectionFactory(CollectionsDao dao, @Value("${caching.enabled:true}") String cachingEnabled) {
        Assert.notNull(dao);
        setCollectionsDao(dao);
        this.cachingEnabled = cachingEnabled;
    }

    private void setCollectionsDao(CollectionsDao dao) {
        collectionsDao = dao;
        init();
    }

    public synchronized void init() {

        // Create a new instance of cached items.
        collections = new Hashtable<>();
        rootCollections = new Vector<>();
        allItemIds =  Collections.synchronizedSet(new HashSet<>());

        List<String> collectionIds = collectionsDao.getCollectionIds();
        for (String collectionId : collectionIds) {
            Collection collection = collectionsDao.getCollection(collectionId);
            collections.put(collectionId, collection);
            allItemIds.addAll(collection.getItemIds());
        }

        // Setup the list of root collections used on the homescreen.
        for (Collection c : collections.values()) {
            String parentId = c.getParentCollectionId();

            if (parentId == null || parentId.length() == 0) {
                rootCollections.add(c);
            }

            // set subcollections for each collection
            c.setSubCollections(getSubCollections(c));
        }
        Collections.sort(rootCollections);
        collectionsRowCount = collectionsDao.getCollectionsRowCount();
        itemsRowCount = collectionsDao.getItemsRowCount();
        itemsinCollectionRowCount = collectionsDao
                .getItemsInCollectionsRowCount();

    }

    public Collection getCollectionFromId(String id) {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            return collectionsDao.getCollection(id);
        }
        return collections.get(id);

    }

    public List<Collection> getCollections() {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            init();
        }

        ArrayList<Collection> list = new ArrayList<>(
                collections.values());
        Collections.sort(list);
        return list;

    }

    public List<Collection> getRootCollections() {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            init();
        }

        return rootCollections;

    }

    public List<Collection> getSubCollections(Collection collection) {

        List<Collection> output = new ArrayList<>();
        for (Collection c : collections.values()) {
            String parentId = c.getParentCollectionId();

            if (parentId != null && parentId.equals(collection.getId())) {
                output.add(c);
            }
        }
        Collections.sort(output);
        return output;
    }

    public Set<String> getAllItemIds() {
        return allItemIds;
    }

    public int getCollectionsRowCount() {
        return collectionsRowCount;
    }

    public int getItemsRowCount() {
        return itemsRowCount;
    }

    public int getItemsInCollectionsRowCount() {
        return itemsinCollectionRowCount;
    }

}
