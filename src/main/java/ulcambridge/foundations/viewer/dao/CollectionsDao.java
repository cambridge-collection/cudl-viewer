package ulcambridge.foundations.viewer.dao;

import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;

public interface CollectionsDao {

    public List<String> getCollectionIds();

    public Collection getCollection(String collectionId);

    public int getTotalNumberOfCollections();

    public int getTotalNumberOfItems();

    public List<String> getCollectionId(String itemId);

}
