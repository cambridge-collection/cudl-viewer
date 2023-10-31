package ulcambridge.foundations.viewer.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ulcambridge.foundations.viewer.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Component
@Profile("!test")
public class CollectionsJSONDao implements CollectionsDao {

    private final String datasetFile;
    private final Dataset dataset;
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String,Collection> collections;
    private Map<String,List<String>> itemsToCollectionsMap;
    private Map<String,String> collectionToParentMap;
    private final UI ui;

    public CollectionsJSONDao (@Value("${datasetFile}") String datasetFile,
                               @Value("${dataDirectory}") String dataDirectory,
                               @Qualifier("uiThemeBean") UI ui) {

        this.datasetFile = datasetFile;
        this.ui = ui;
        this.dataset = setupDataset(Path.of(datasetFile));
        init(dataDirectory);


    }

    private Dataset setupDataset(Path jsonFilepath) {
        try {
            String datasetJSON = Files.readString(jsonFilepath);
            return mapper.readValue(datasetJSON, Dataset.class);

        } catch (IOException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void init(String dataDirectory) {

        Map<String,Collection> collections = new Hashtable<>();
        Map<String,List<String>> itemToCollectionsMap = new Hashtable<>();
        Map<String,String> collectionToParentMap = new Hashtable<>();
        try {

            for (Id id: dataset.getCollections()) {

                // read in collections JSON
                String collectionJSONFile = Files.readString(Path.of(dataDirectory + "/" + id.getId()));
                CollectionJSON collectionJSON = mapper.readValue(collectionJSONFile, CollectionJSON.class);
                String collectionId = collectionJSON.getName().getUrlSlug();

                // Add each item to item-to-collection mapping
                for (String itemId : collectionJSON.getCudlItemIds()) {
                    List<String> collectionList = itemToCollectionsMap.get(itemId);
                    if (collectionList != null) {
                        collectionList.add(collectionId);
                    } else {
                        collectionList = new ArrayList<>();
                        collectionList.add(collectionId);
                    }
                    itemToCollectionsMap.put(itemId, collectionList);
                }

                // Build mapping of collection-to-parent
                if (collectionJSON.getSubCollectionIds() != null && !collectionJSON.getSubCollectionIds().isEmpty()) {
                    for (Id subId : collectionJSON.getSubCollectionIds()) {
                        String subCUDLId = FilenameUtils.getBaseName(subId.getId());
                        collectionToParentMap.put(subCUDLId, collectionId);
                    }
                }
            }
            this.itemsToCollectionsMap = itemToCollectionsMap;
            this.collectionToParentMap = collectionToParentMap;

            for (Id id: dataset.getCollections()) {
                // read in collections JSON
                String collectionJSONFile = Files.readString(Path.of(dataDirectory + "/" + id.getId()));
                CollectionJSON collectionJSON = mapper.readValue(collectionJSONFile, CollectionJSON.class);
                String collectionId = collectionJSON.getName().getUrlSlug();

                String summary = collectionJSON.getDescription().getFull().getId();
                String sponsors = collectionJSON.getCredit().getProse().getId();

                // TODO make this less hardcoded.
                summary = summary.substring(summary.indexOf("collections/"));
                sponsors = sponsors.substring(sponsors.indexOf("collections/"));

                // Set collections map
                Collection collection = new Collection (collectionId, collectionJSON.getName().getFull(),
                    collectionJSON.getCudlItemIds(),summary,
                    sponsors, getCollectionType(collectionId),
                    collectionToParentMap.get(collectionId),
                    collectionJSON.getCudlItemIds(), collectionJSON.getDescription().getShortDescription());

                collections.put(collectionId,collection);
            }
            this.collections = collections;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getCollectionType(String collectionId) {
        UICollection uiCollection = ui.getThemeData().getCollectionByCollectionId(collectionId);
        return uiCollection.getLayout();
    }

    @Override
    public List<String> getCollectionIds() {
        return new ArrayList<>(collections.keySet());
    }

    @Override
    public Collection getCollection(String collectionId) {
        return collections.get(collectionId);
    }

    @Override
    public int getTotalNumberOfCollections() {
        return collections.size();
    }

    @Override
    public int getTotalNumberOfItems() {
        return itemsToCollectionsMap.keySet().size();
    }

    @Override
    public List<String> getCollectionId(String itemId) {
        return itemsToCollectionsMap.get(itemId);
    }
}
