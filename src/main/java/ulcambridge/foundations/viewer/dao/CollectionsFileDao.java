package ulcambridge.foundations.viewer.dao;

import java.util.Arrays;
import java.util.List;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Properties;

public abstract class CollectionsFileDao implements CollectionsDao {

	public List<String> getCollectionIds() {

		// Get collection url and title
		return Arrays.asList(Properties.getString("collections").split(
				"\\s*,\\s*"));
	}

	public Collection getCollection(String collectionId) {

		List<String> collectionItemIds = Arrays.asList(Properties.getString(
				collectionId + ".items").split("\\s*,\\s*"));

		String collectionTitle = Properties.getString(collectionId + ".title");
		String collectionSummary = Properties.getString(collectionId
				+ ".summary");
		String collectionSponsors = Properties.getString(collectionId
				+ ".sponsors");
		String collectionType = Properties.getString(collectionId + ".type");
		String collectionParentId = Properties.getString(collectionId + ".parentId");
		
		//
		// XXX genizah tagging
		//
		boolean taggingStatus = Boolean.parseBoolean( Properties.getString(collectionId + ".tagging") );
		
		return new Collection(collectionId, collectionTitle, collectionItemIds,
				collectionSummary, collectionSponsors, collectionType, collectionParentId, taggingStatus);

	}

}