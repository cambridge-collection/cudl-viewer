package ulcambridge.foundations.viewer.model;

import java.util.List;

import org.json.JSONObject;

/**
 * This class contains a subset of the data in the JSON file for an item, which
 * we want to display at collection page level.
 * 
 * @author jennie
 * 
 */
public class EssayItem extends Item {

	protected String content;
	protected List<String> itemReferences;
	
	public EssayItem(String itemId, String itemType, String itemTitle, List<Person> authors,
			String itemShelfLocator, String itemAbstract,
			String itemThumbnailURL, String thumbnailOrientation,
			JSONObject itemJson, String content, List <String> itemReferences) {

		super (itemId, itemType, itemTitle, authors,
				itemShelfLocator,itemAbstract,
				itemThumbnailURL, thumbnailOrientation,
				itemJson);
		
		this.content = content;
		this.itemReferences = itemReferences;
		
	}

	public List<String> getItemReferences() {
		return itemReferences;
	}
	
	public String getContent() {
		return content;
	}

}
