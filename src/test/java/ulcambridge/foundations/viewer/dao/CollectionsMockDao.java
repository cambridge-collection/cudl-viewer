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
				"collections/treasures/summary.jsp", "collections/treasures/sponsors.jsp", "virtual", "", true);

	}

	@Override
	public boolean isItemTaggable(String itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCollectionTaggable(String collectionId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCollectionId(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}

}