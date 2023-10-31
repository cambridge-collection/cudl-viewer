package ulcambridge.foundations.viewer.dao;

import java.util.ArrayList;
import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;

public class MockCollectionsDao implements CollectionsDao {

    public List<String> getCollectionIds() {

        List<String> ids = new ArrayList<String>();
        ids.add("treasures");
        ids.add("newton");
        ids.add("longitude");

        return ids;
    }

    public Collection getCollection(String collectionId) {

        List<String> collectionItemIds = new ArrayList<>();
        collectionItemIds.add("MS-ADD-04004");

        return new Collection("treasures",
            "Newton Papers",
            collectionItemIds,
            "collections/treasures/summary.html",
            "collections/treasures/sponsors.html",
            "virtual",
            "", collectionItemIds);

    }

    @Override
    public int getTotalNumberOfCollections() {
        return 0;
    }

    @Override
    public int getTotalNumberOfItems() {
        return 0;
    }

    @Override
    public List<String> getCollectionId(String itemId) { return null; }

}
