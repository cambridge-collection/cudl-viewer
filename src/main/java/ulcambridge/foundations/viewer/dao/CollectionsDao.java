package ulcambridge.foundations.viewer.dao;

import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;

public interface CollectionsDao {

    public List<String> getCollectionIds();

    public Collection getCollection(String collectionId);

    public int getCollectionsRowCount();

    public int getItemsInCollectionsRowCount();

    public int getItemsRowCount();
    
    //
	// XXX tagging switch
	//

	public boolean isItemTaggable(String itemId);
	
	public boolean isCollectionTaggable(String collectionId);
	
	public List<String> getCollectionId(String itemId);
    
}
