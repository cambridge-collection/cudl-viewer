package ulcambridge.foundations.viewer.dao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.UI;
import ulcambridge.foundations.viewer.model.UIDataCollection;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class CollectionsJSONDao implements CollectionsDao {

    private final Hashtable<String,Collection> collections = new Hashtable<>();
    private final UI uiTheme;
    private final Hashtable<String, List<String>> subCollections = new Hashtable<>();

    public CollectionsJSONDao(@Qualifier("datasetFile") File datasetFile,  @Qualifier("uiThemeBean") UI uiTheme) throws IOException {

        this.uiTheme = uiTheme;

        // Go through all the collections listed and setup collection objects
        String dataset = FileUtils.readFileToString(datasetFile, StandardCharsets.UTF_8);
        JSONObject datasetJson = new JSONObject(dataset);
        JSONArray collectionArray = datasetJson.getJSONArray("collections");

        for (int i = 0; i < collectionArray.length(); i++) {
            String collectionFilePath = datasetFile.getParent()+File.separator+collectionArray.getJSONObject(i).getString("@id");

            // Assume collection filename is of form urlslug.collection.json
            String collectionId = FilenameUtils.getName(collectionFilePath).replace(".collection.json", "");

            // Read collection file into a collection object
            this.collections.put(collectionId,getCollectionFromFile(new File(collectionFilePath)));
        }

        // Set the subcollections and parent collections on collection objects (all currently empty string)
        // This requires the collections hashtable to be populated.
        setupParentAndSubCollections();
    }

    private Collection getCollectionFromFile(File file) throws IOException {
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
            this.subCollections.put(collectionId, subcollectionIds);
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

    private void setupParentAndSubCollections() {
        for (String collectionId : subCollections.keySet()) {
            List<String> subCollectionIds = subCollections.get(collectionId);
            List<Collection> subCollections = new ArrayList<>();
            for (String subCollectionId : subCollectionIds) {
                // set Parent Collections
                Collection subCollection = collections.get(subCollectionId);
                subCollection.setParentCollectionId(collectionId);
                subCollections.add(subCollection);
            }
            // set Sub Collections
            collections.get(collectionId).setSubCollections(subCollections);
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
