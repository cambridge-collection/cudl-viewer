package ulcambridge.foundations.viewer.dao;

import java.util.ArrayList;
import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;

/**
 * Used for testing
 * 
 * @author jennie
 */
public class CollectionsMockDao implements CollectionsDao {

	public List<String> getCollectionIds() {

		List<String> ids = new ArrayList<String>();
		ids.add("MS-ADD-03959");
		ids.add("MS-ADD-04000");
		ids.add("MS-ADD-04004");
		
		return ids;
	}

	public Collection getCollection(String collectionId) {

		return new Collection("treasures", "Newton Papers", getCollectionIds(),
				"collections/treasures/summary.html", "collections/treasures/sponsors.html", "virtual", "");

	}

    @Override
    public int getCollectionsRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getItemsInCollectionsRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getItemsRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}