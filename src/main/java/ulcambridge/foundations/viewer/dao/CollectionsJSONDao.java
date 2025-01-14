package ulcambridge.foundations.viewer.dao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.UI;
import ulcambridge.foundations.viewer.model.UIDataCollection;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CollectionsJSONDao implements CollectionsDao {

    private Hashtable<String,Collection> collections;
    private final File datasetFile;
    private final File uiFile;
    private UI uiTheme;
    private final String cachingEnabled;

    public CollectionsJSONDao(@Qualifier("datasetFile") File datasetFile,
                              @Value("${dataUIFile}") String uiFilepath, String cachingEnabled) throws IOException {

        this.cachingEnabled = cachingEnabled;
        this.datasetFile = datasetFile;
        this.uiFile = new File(uiFilepath);
        UIDao uiDao = new UIDao();
        this.uiTheme =  uiDao.getUITheme(Paths.get(uiFilepath));

        this.collections = readCollectionsFromFiles(datasetFile);

    }

    private Hashtable<String,Collection> readCollectionsFromFiles(File datasetFile)
        throws IOException {

        Hashtable<String,Collection> collections = new Hashtable<>();
        Hashtable<String, List<String>> subCollections = new Hashtable<>();

        // Go through all the collections listed and setup collection objects
        String dataset = FileUtils.readFileToString(datasetFile, StandardCharsets.UTF_8);
        JSONObject datasetJson = new JSONObject(dataset);
        JSONArray collectionArray = datasetJson.getJSONArray("collections");

        for (int i = 0; i < collectionArray.length(); i++) {
            String collectionFilePath = datasetFile.getParent()+File.separator+collectionArray.getJSONObject(i).getString("@id");

            // Assume collection filename is of form urlslug.collection.json
            String collectionId = FilenameUtils.getName(collectionFilePath).replace(".collection.json", "");

            // Read collection file into a collection object
            collections.put(collectionId,getCollectionFromFile(new File(collectionFilePath), subCollections));
        }

        // Set the subcollections and parent collections on collection objects (all currently empty string)
        // This requires the collections hashtable to be populated.
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
        String collectionType =  getCollectionType(collectionId);
        String parentCollectionId = null;

        String metaDescription = description.getString("medium");

        if (collectionJson.has("collections")) {
            List<String> subcollectionIds = new ArrayList<>();
            for (int i = 0; i < collectionJson.getJSONArray("collections").length(); i++) {
                String subCollectionPath = collectionJson.getJSONArray("collections").getJSONObject(i).getString("@id");
                String subCollectionId = FilenameUtils.getName(subCollectionPath).replace(".collection.json", "");
                subcollectionIds.add(subCollectionId);
            }
            subCollections.put(collectionId, subcollectionIds);
        }

        return new Collection(collectionId, collectionTitle, collectionItemIds, collectionSummary,
            collectionSponsors, collectionType, parentCollectionId, metaDescription);

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
            if (uiCollection.getCollection().getId().endsWith(File.separator+collectionId+".collection.json")) {
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
                // set Parent Collections
                Collection subCollection = collections.get(subCollectionId);
                subCollection.setParentCollectionId(collectionId);
                subCollectionsList.add(subCollection);
            }
            // set Sub Collections
            collections.get(collectionId).setSubCollections(subCollectionsList);
        }
        return collections;
    }

    // Reload every 5 mins if caching disabled.
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    private void refreshData() {
        refreshData(false);
    }

    public void refreshData(boolean force) {
        if (!"true".equalsIgnoreCase(cachingEnabled) || force) {
            // Reload dataset
            try {
                this.collections = readCollectionsFromFiles(datasetFile);
            } catch (IOException e) {
                System.err.println("Error in reading collections from dataset file: ");
                e.printStackTrace(System.err);
            }

            // Reload UI
            UIDao uiDao = new UIDao();
            this.uiTheme =  uiDao.getUITheme(Paths.get(uiFile.getPath()));

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
