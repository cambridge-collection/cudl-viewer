package ulcambridge.foundations.viewer.dao;

import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;


public interface CollectionsDAO {
	
	public List<String> getCollectionIds();
	public Collection getCollection(String collectionId);

}