package ulcambridge.foundations.viewer.dao;

import ulcambridge.foundations.viewer.model.Collection;

import java.util.List;

public interface CollectionsDao {

    public List<String> getCollectionIds();

    public Collection getCollection(String collectionId);

}
