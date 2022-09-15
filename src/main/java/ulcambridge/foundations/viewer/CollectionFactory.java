package ulcambridge.foundations.viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    private final Path jsonDirPath;

    @Autowired
    public CollectionFactory(CollectionsDao dao, @Value("${caching.enabled:true}")
        String cachingEnabled, @Qualifier("itemJSON") Path jsonDirPath) {
        Assert.notNull(dao, "CollectionsDao cannot be null");
        Assert.notNull(jsonDirPath, "itemJSONDirectory should not be null");
        setCollectionsDao(dao);
        this.cachingEnabled = cachingEnabled;
        this.jsonDirPath = jsonDirPath;
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
            Collection collection = collectionsDao.getCollection(id);
            if (collection!=null && "parent".equals(collection.getType())) {
                collection.setSubCollections(getSubCollections(collection));
            }
            return collection;
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


    /*
This method removes any items from collections if they do not exist as JSON on the file system.
This takes a while to run so runs async and infrequently.
 */
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public synchronized void removeItemsWithMissingJSON() {
        System.out.println("Scheduled removeItemsWithMissingJSON from all collections...");
        List<Collection> collections = getCollections();
        for (Collection c: collections) {
            c.getItemIds().removeIf(itemid -> !existsJSON(itemid + ".json"));
        }
        System.out.println("Scheduled removeItemsWithMissingJSON complete.");
    }


    private boolean existsJSON(String id) {
        if (id == null) { return false; }
        return getJSONFiles().contains(id);
    }

    private Set<String> getJSONFiles() {

        return Stream.of(Objects.requireNonNull(jsonDirPath.toFile().listFiles()))
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .collect(Collectors.toSet());
    }
}
