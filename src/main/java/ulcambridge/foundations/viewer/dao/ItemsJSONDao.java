package ulcambridge.foundations.viewer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import ulcambridge.foundations.viewer.JSONReader;
import ulcambridge.foundations.viewer.model.EssayItem;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;
import ulcambridge.foundations.viewer.model.Properties;

public class ItemsJSONDao implements ItemsDao {

	private JSONReader reader;

	@Autowired
	public void setJSONReader(JSONReader jsonreader) {
		reader = jsonreader;
	}

	/**
	 * Get item information from the JSON file.
	 */
	public Item getItem(String itemId) {
		
		JSONObject itemJson = null;
		String itemType = "bookormanuscript"; // default
		
		if (itemId == null || itemId.equals("")) {
			throw new IllegalArgumentException("Invalid Item Id given.");
		}
		try {

			itemJson = reader.readJsonFromUrl(Properties.getString("jsonURL")
					+ itemId + ".json");
			
			// Might have type, might not.
			if (itemJson.has("itemType")) {
				itemType = itemJson.getString("itemType");
			}
			
			if (itemType.equals("essay")) {
								
				Item bookItem = getBookItem(itemId, itemJson);				
				return getEssayItem(itemId, itemJson, bookItem);
				
			} else {
				return getBookItem(itemId, itemJson);
			}
			
		} catch (IOException e) {
			System.err.println("Failed to load item: "+itemId+" error:"+e.getMessage());			
			return null;
		} catch (JSONException e) {
			System.err.println("Failed to load item: "+itemId+" error:"+e.getMessage());
			return null;
		}			
	}
	
	/**
	 * Parse the JSON into a Item object (default root object). 
	 * 
	 * @param itemId
	 * @param itemJson
	 * @return
	 */
	private Item getBookItem(String itemId, JSONObject itemJson) {

		String itemType = "bookormanuscript";
		String itemTitle = "";
		List<Person> itemAuthors = new ArrayList<Person>();
		String itemShelfLocator = "";
		String itemAbstract = "";
		String itemThumbnailURL = "";
		String thumbnailOrientation = "";
			
		try {
			
			// Pull out the information we want in our Item object
			JSONObject descriptiveMetadata = itemJson.getJSONArray(
					"descriptiveMetadata").getJSONObject(0);
			
			// Should always have title
			itemTitle = descriptiveMetadata.getJSONObject("title").getString(
					"displayForm");

			// Might have authors, might not.
			if (descriptiveMetadata.has("authors")) {
				itemAuthors = getPeopleFromJSON(descriptiveMetadata
						.getJSONObject("authors").getJSONArray("value"),
						"author");
			}

			// Might have shelf locator, might not.
			if (descriptiveMetadata.has("shelfLocator")) {
				itemShelfLocator = descriptiveMetadata.getJSONObject(
						"shelfLocator").getString("displayForm");
			}

			// Might have abstract, might not.
			if (descriptiveMetadata.has("abstract")) {
				itemAbstract = descriptiveMetadata.getJSONObject("abstract")
						.getString("displayForm");
			}
			
			// if not see if we have content (used in essay objects)
			// - NOTE reads content from page 0 only. 
			JSONObject page0 = itemJson.getJSONArray(
					"pages").getJSONObject(0);
			
			if (itemAbstract.equals("") && page0.has("content")) {
				itemAbstract = page0.getString("content");
			}

			// Might have Thumbnail image
			if (descriptiveMetadata.has("thumbnailUrl")
					&& descriptiveMetadata.has("thumbnailOrientation")) {
				itemThumbnailURL = descriptiveMetadata
						.getString("thumbnailUrl");
				if (Properties.getString("useProxy").equals("true")) {
					itemThumbnailURL = Properties.getString("proxyURL")
							+ itemThumbnailURL;
				}

				thumbnailOrientation = descriptiveMetadata
						.getString("thumbnailOrientation");
			}

		} catch (JSONException e) {
			System.err.println("Failed to load item: "+itemId+" error:"+e.getMessage());
			return null;
		}	
		
		Item item = new Item(itemId, itemType, itemTitle, itemAuthors, itemShelfLocator,
				itemAbstract, itemThumbnailURL, thumbnailOrientation, itemJson);

		return item;

	}
	
	private EssayItem getEssayItem(String itemId, JSONObject itemJson, Item parent) {
		
		String content = "";
		List<String> relatedItems = null;
		
		try {
			
			JSONObject page0 = itemJson.getJSONArray(
					"pages").getJSONObject(0);
			
			// Get essay content
			content = page0.getString("content");
			
			JSONObject descriptiveMetadata = itemJson.getJSONArray(
					"descriptiveMetadata").getJSONObject(0);
			
			// Get list of related items
			relatedItems = new ArrayList<String>();
			JSONArray itemReferences = descriptiveMetadata.getJSONArray("itemReferences");
			for (int i=0; i<itemReferences.length(); i++) {
				JSONObject itemRef = itemReferences.getJSONObject(i);	
				relatedItems.add(itemRef.getString("ID"));
			}
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new EssayItem(itemId, "essay", parent.getTitle(), parent.getAuthors(), parent.getShelfLocator(),
			 parent.getAbstract(), parent.getThumbnailURL(), parent.getThumbnailOrientation(), parent.getJSON(), 
			 content, relatedItems);
	}

	/**
	 * This method takes a JSONArray and creates Person objects for each JSON
	 * object in it.
	 * 
	 * @param names
	 * @return
	 */
	private List<Person> getPeopleFromJSON(JSONArray json, String role) {

		ArrayList<Person> people = new ArrayList<Person>();

		if (json == null) {
			return people;
		}

		String authority = null;
		String authorityURI = null;
		String valueURI = null;
		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject personJSON = json.getJSONObject(i);
				String fullForm = personJSON.getString("fullForm");
				String shortForm = personJSON.getString("shortForm");
				try {
					authority = personJSON.getString("authority");
					authorityURI = personJSON.getString("authorityURI");
					valueURI = personJSON.getString("valueURI");
				} catch (JSONException e) {
					/* ignore if not present */
				}
				String type = personJSON.getString("type");
				Person person = new Person(fullForm, shortForm, authority,
						authorityURI, valueURI, type, role);
				people.add(person);
			}

		} catch (JSONException e) {

			System.err.println("Error processing: " + json);
			e.printStackTrace();
		}

		return people;
	}

}