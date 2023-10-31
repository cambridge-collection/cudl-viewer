package ulcambridge.foundations.viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Component
public class CollectionFactory {

    private static Map<String, Collection> collections;
    private static List<Collection> rootCollections;
    private CollectionsDao collectionsDao;
    private static int collectionsRowCount;
    private static int itemsRowCount;
   // private static int itemsinCollectionRowCount;
    private static Set<String> allItemIds;
    private final String cachingEnabled;
    private final Path jsonDirPath;
    private Set<String> jsonFiles;

    @Autowired
    public CollectionFactory(CollectionsDao dao, @Value("${caching.enabled:true}")
        String cachingEnabled, @Qualifier("itemJSON") Path jsonDirPath) {
        Assert.notNull(dao, "CollectionsDao cannot be null");
        Assert.notNull(jsonDirPath, "itemJSONDirectory should not be null");
        this.cachingEnabled = cachingEnabled;
        this.jsonDirPath = jsonDirPath;
        setCollectionsDao(dao);
    }

    private void setCollectionsDao(CollectionsDao dao) {
        collectionsDao = dao;
        init(false);
    }

    /**
     * Resetting JSON files reads from disk which makes this very slow for large collections
     * so only use if manually called using /refresh url or on startup.
     * @param resetJSONFiles
     */
    public synchronized void init(boolean resetJSONFiles) {

        // Create a new instance of cached items.
        collections = new Hashtable<>();
        rootCollections = new Vector<>();
        allItemIds =  Collections.synchronizedSet(new HashSet<>());
        if (jsonFiles==null || resetJSONFiles) {
            jsonFiles = getJSONFiles();
        }

        List<String> collectionIds = collectionsDao.getCollectionIds();
        for (String collectionId : collectionIds) {
            Collection collection = collectionsDao.getCollection(collectionId);

            // Check for missing JSON on disk and remove
            collection.getItemIds().removeIf(itemid -> !existsJSON(itemid + ".json"));

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
        collectionsRowCount = collectionsDao.getTotalNumberOfCollections();
        itemsRowCount = collectionsDao.getTotalNumberOfItems();

    }

    public Collection getCollectionFromId(String id) {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            Collection collection = collectionsDao.getCollection(id);
            assert collection != null;
            if ("parent".equals(collection.getType())) {
                collection.setSubCollections(getSubCollections(collection));
            }
            collection.getItemIds().removeIf(itemid -> !existsJSON(itemid + ".json"));
            return collection;
        }
        return collections.get(id);

    }

    public List<Collection> getCollections() {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            init(false);
        }

        ArrayList<Collection> list = new ArrayList<>(
                collections.values());
        Collections.sort(list);
        return list;

    }

    public List<Collection> getRootCollections() {

        if (!"true".equalsIgnoreCase(cachingEnabled)) {
            init(false);
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

    private boolean existsJSON(String id) {
        if (id == null) { return false; }
        return jsonFiles.contains(id);
    }

    /**
     * Careful with this, it can be expensive to build a list of all items on disk.
     * @return
     */
    private Set<String> getJSONFiles() {
        final Path dir = Paths.get(String.valueOf(jsonDirPath));
        final Set<String> names = new HashSet<>();

        try (
            final DirectoryStream<Path> dirstream
                = Files.newDirectoryStream(dir);
        ) {
            for (final Path entry: dirstream) {
                names.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
}
