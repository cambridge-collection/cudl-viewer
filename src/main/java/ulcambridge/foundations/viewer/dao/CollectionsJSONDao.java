package ulcambridge.foundations.viewer.dao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.UI;
import ulcambridge.foundations.viewer.model.UIDataCollection;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class CollectionsJSONDao implements CollectionsDao {

    private static final Logger log = LoggerFactory.getLogger(CollectionsJSONDao.class);

    private Hashtable<String,Collection> collections;
    private final File datasetFile;
    private final File uiFile;
    private UI uiTheme;
    private final String cachingEnabled;
    private final Path unreleasedCollectionsDir;

    public CollectionsJSONDao(@Qualifier("datasetFile") File datasetFile,
                              @Value("${dataUIFile}") String uiFilepath,
                              String cachingEnabled,
                              Path unreleasedCollectionsDir) throws IOException {

        this.cachingEnabled = cachingEnabled;
        this.unreleasedCollectionsDir = unreleasedCollectionsDir;
        this.datasetFile = datasetFile;
        this.uiFile = new File(uiFilepath);
        UIDao uiDao = new UIDao();
        this.uiTheme = uiDao.getUITheme(Paths.get(uiFilepath));

        this.collections = readCollectionsFromFiles(datasetFile);
    }

    private Hashtable<String,Collection> readCollectionsFromFiles(File datasetFile)
        throws IOException {

        Hashtable<String,Collection> collections = new Hashtable<>();
        Hashtable<String, List<String>> subCollections = new Hashtable<>();

        String dataset = FileUtils.readFileToString(datasetFile, StandardCharsets.UTF_8);
        JSONObject datasetJson = new JSONObject(dataset);
        JSONArray collectionArray = datasetJson.getJSONArray("collections");

        // Load collections listed in the dataset file. Missing files are skipped
        // with a warning rather than aborting startup — this allows the dataset
        // to reference collections that only exist in the unreleased directory.
        for (int i = 0; i < collectionArray.length(); i++) {
            String collectionFilePath = datasetFile.getParent() + File.separator + collectionArray.getJSONObject(i).getString("@id");
            File collectionFile = new File(collectionFilePath);
            String collectionId = FilenameUtils.getName(collectionFilePath).replace(".collection.json", "");
            try {
                Collection c = getCollectionFromFile(collectionFile, subCollections);
                if (c != null) {
                    collections.put(collectionId, c);
                }
            } catch (IOException e) {
                log.warn("Skipping collection file that could not be read: {} — {}", collectionFile.getPath(), e.getMessage());
            }
        }

        // Scan the unreleased collections directory and merge any collections not
        // already loaded above. This is not gated on showUnreleasedContent: the flag
        // governs whether the badge and notice render, not whether the collection
        // loads, so an unreleased collection page stays reachable either way.
        if (unreleasedCollectionsDir != null) {
            log.debug("scanning unreleased collections dir: {}", unreleasedCollectionsDir);
            File[] files = unreleasedCollectionsDir.toFile()
                .listFiles((dir, name) -> name.endsWith(".collection.json"));

            log.debug("found {} unreleased collection files", files == null ? 0 : files.length);
            if (files != null) {
                Set<String> loadedIds = collections.keySet();

                for (File file : files) {
                    try {
                        Collection c = getCollectionFromFile(file, subCollections);
                        // Skip if this collection id was already loaded from the main dataset.
                        if (c != null && !loadedIds.contains(c.getId())) {
                            collections.put(c.getId(), c);
                            log.debug("added unreleased collection: id={} type={}", c.getId(), c.getType());
                        } else if (c != null) {
                            log.debug("skipping unreleased collection (already loaded): {}", c.getId());
                        }
                    } catch (IOException e) {
                        log.warn("Skipping unreadable unreleased collection file: {} — {}", file.getName(), e.getMessage());
                    }
                }
            }
        }

        return setupParentAndSubCollections(collections, subCollections);
    }

    private Collection getCollectionFromFile(File file, Hashtable<String, List<String>> subCollections)
        throws IOException {

        String collection = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        JSONObject collectionJson = new JSONObject(collection);

        JSONObject name = collectionJson.getJSONObject("name");
        JSONObject description = collectionJson.getJSONObject("description");
        JSONObject credit = collectionJson.getJSONObject("credit");

        String collectionId = name.getString("url-slug");
        String collectionTitle = name.getString("full");
        List<String> collectionItemIds = getItemIds(collectionJson);
        String collectionSummary = description.getJSONObject("full").getString("@id");
        String collectionSponsors = credit.getJSONObject("prose").getString("@id");
        String collectionType = getCollectionType(collectionId);
        String parentCollectionId = null;

        String metaDescription = description.getString("medium");

        // Release state comes from the file's own properties, not from which directory
        // it was found in. Absent isReleased means unreleased and absent status means
        // draft, matching the item rule; most collection files carry neither yet.
        boolean released = collectionJson.optBoolean("isReleased", false);
        String status = collectionJson.optString("status", Collection.DEFAULT_STATUS);

        if (collectionJson.has("collections")) {
            List<String> subcollectionIds = new ArrayList<>();
            for (int i = 0; i < collectionJson.getJSONArray("collections").length(); i++) {
                String subCollectionPath = collectionJson.getJSONArray("collections").getJSONObject(i).getString("@id");
                String subCollectionId = FilenameUtils.getName(subCollectionPath).replace(".collection.json", "");
                subcollectionIds.add(subCollectionId);
            }
            subCollections.put(collectionId, subcollectionIds);
        }

        Collection collectionObject = new Collection(collectionId, collectionTitle, collectionItemIds,
            collectionSummary, collectionSponsors, collectionType, parentCollectionId, metaDescription);
        collectionObject.setReleaseState(released, status);
        return collectionObject;
    }

    private List<String> getItemIds(JSONObject collectionJson) {
        List<String> itemIds = new ArrayList<>();
        JSONArray itemsArray = collectionJson.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            String itemPath = (itemsArray.getJSONObject(i).getString("@id"));
            itemIds.add(FilenameUtils.getName(itemPath.replace(".json", "")));
        }
        return itemIds;
    }

    private String getCollectionType(String collectionId) {

        List<UIDataCollection> uiCollections = uiTheme.getThemeData().getCollections();
        for (UIDataCollection uiCollection : uiCollections) {
            if (uiCollection.getCollection().getId().endsWith(File.separator + collectionId + ".collection.json")) {
                return uiCollection.getLayout();
            }
        }
        return null;
    }

    private Hashtable<String,Collection> setupParentAndSubCollections(Hashtable<String,Collection> collections,
                                                                      Hashtable<String, List<String>> subCollections) {
        for (String collectionId : subCollections.keySet()) {
            List<String> subCollectionIds = subCollections.get(collectionId);
            List<Collection> subCollectionsList = new ArrayList<>();
            for (String subCollectionId : subCollectionIds) {
                Collection subCollection = collections.get(subCollectionId);
                subCollection.setParentCollectionId(collectionId);
                subCollectionsList.add(subCollection);
            }
            collections.get(collectionId).setSubCollections(subCollectionsList);
        }
        return collections;
    }

    // Reload every 2 mins if caching disabled.
    @Scheduled(fixedDelay = 1000 * 60 * 2)
    private void refreshData() {
        refreshData(false);
    }

    public void refreshData(boolean force) {
        if (!"true".equalsIgnoreCase(cachingEnabled) || force) {
            try {
                this.collections = readCollectionsFromFiles(datasetFile);
            } catch (IOException e) {
                System.err.println("Error in reading collections from dataset file: ");
                e.printStackTrace(System.err);
            }

            UIDao uiDao = new UIDao();
            this.uiTheme = uiDao.getUITheme(Paths.get(uiFile.getPath()));
        }
    }

    @Override
    public List<String> getCollectionIds() {
        return new ArrayList<>(collections.keySet());
    }

    @Override
    public Collection getCollection(final String collectionId) {
        return collections.get(collectionId);
    }
}
