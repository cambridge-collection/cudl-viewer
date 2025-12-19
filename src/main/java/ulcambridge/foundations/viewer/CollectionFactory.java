package ulcambridge.foundations.viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.CollectionsJSONDao;
import ulcambridge.foundations.viewer.model.Collection;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Component
public class CollectionFactory {

    private Map<String, Collection> collections = Collections.emptyMap();
    private List<Collection> rootCollections = Collections.emptyList();
    private CollectionsDao collectionsDao;
    private Set<String> allItemIds = Collections.emptySet();
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
    public void init(boolean resetJSONFiles) {

        // Build new instances so other threads can keep using the existing
        // collections while a refresh is in progress.
        Map<String, Collection> newCollections = new HashMap<>();
        List<Collection> newRootCollections = new ArrayList<>();
        Set<String> newAllItemIds = new HashSet<>();

        Set<String> newJsonFiles = jsonFiles;
        if (newJsonFiles == null || resetJSONFiles) {
            newJsonFiles = getJSONFiles();
        }
        final Set<String> jsonFilesSnapshot = newJsonFiles;

        List<String> collectionIds = collectionsDao.getCollectionIds();
        for (String collectionId : collectionIds) {
            Collection collection = collectionsDao.getCollection(collectionId);
            // Check for missing JSON on disk and remove
            collection.getItemIds().removeIf(itemid -> !jsonFilesSnapshot.contains(itemid + ".json"));

            newCollections.put(collectionId, collection);
            newAllItemIds.addAll(collection.getItemIds());
        }

        // Setup the list of root collections used on the homescreen.
        for (Collection c : newCollections.values()) {
            String parentId = c.getParentCollectionId();

            if (parentId == null || parentId.length() == 0) {
                newRootCollections.add(c);
            }

            // set subcollections for each collection
            c.setSubCollections(getSubCollections(c, newCollections));
        }
        Collections.sort(newRootCollections);

        // Atomically publish the new snapshots.
        jsonFiles = newJsonFiles;
        collections = newCollections;
        rootCollections = newRootCollections;
        allItemIds = Collections.unmodifiableSet(newAllItemIds);

    }

    public synchronized void refreshCollections() {
        if (collectionsDao instanceof CollectionsJSONDao) {
            CollectionsJSONDao jsonDao = (CollectionsJSONDao) collectionsDao;
            jsonDao.refreshData(true);
        }
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

    private List<Collection> getSubCollections(Collection collection, Map<String, Collection> sourceCollections) {

        List<Collection> output = new ArrayList<>();
        for (Collection c : sourceCollections.values()) {
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
