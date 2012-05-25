package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

/**
 * This class contains a subset of the data in the JSON file for an item, which
 * we want to display at collection page level.
 * 
 * @author jennie
 * 
 */
public class Item implements Comparable<Item> {

	private static int orderCount = 1;
	private int order;
	private String id;
	private String title;
	private List<Person> authors;
	private String shelfLocator;
	private String abstractText;
	private String thumbnailURL;
	private String thumbnailOrientation;
	private String abstractShort;
	private JSONObject json; // used for document view
	private JSONObject simplejson; // used for collection view

	public Item(String itemId, String itemTitle, List<Person> authors,
			String itemShelfLocator, String itemAbstract,
			String itemThumbnailURL, String thumbnailOrientation,
			JSONObject itemJson) {

		this.id = itemId;
		this.json = itemJson;
		this.title = itemTitle;
		this.shelfLocator = itemShelfLocator;
		this.abstractText = itemAbstract;
		this.thumbnailURL = itemThumbnailURL;
		this.thumbnailOrientation = thumbnailOrientation;
		this.authors = authors;

		// Make short abstract
		String abstractShort = abstractText;

		// remove video captions
		String[] captionParts = abstractShort
				.split("<div[^>]*class=['\"]videoCaption['\"][^>]*>");
		if (captionParts.length > 1) {

			abstractShort = captionParts[0];
			for (int i = 1; i < captionParts.length; i++) {
				int captionEnd = captionParts[i].indexOf("</div>");
				if (captionEnd > -1) {
					abstractShort += captionParts[i].substring(captionEnd);
				} else {
					abstractShort += captionParts[i];
				}
			}
		}

		// remove all tags
		abstractShort = abstractShort.replaceAll("\\<.*?>", "");

		// cut of next word after 120 characters
		if (abstractShort.length() > 120) {
			abstractShort = abstractShort.substring(0, 120 + abstractShort
					.substring(120).indexOf(" "));
		}
		this.abstractShort = abstractShort;

		// Make simple JSON
		try {
			simplejson = new JSONObject();
			simplejson.append("id", itemId);
			simplejson.append("title", itemTitle);
			simplejson.append("shelfLocator", itemShelfLocator);
			simplejson.append("abstractText", itemAbstract);
			simplejson.append("abstractShort", abstractShort);			
			simplejson.append("thumbnailURL", itemThumbnailURL);
			simplejson.append("thumbnailOrientation", thumbnailOrientation);
			JSONArray authorJSON = new JSONArray();
			authorJSON.addAll(authors);
			simplejson.append("authors", authorJSON);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		orderCount++;
		this.order = orderCount;

	}

	public List<String> getAuthorNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < authors.size(); i++) {
			names.add(authors.get(i).getDisplayForm());
		}
		return names;
	}

	public List<String> getAuthorNamesFullForm() {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < authors.size(); i++) {
			names.add(authors.get(i).getFullForm());
		}
		return names;
	}

	public int getOrder() {
		return order;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<Person> getAuthors() {
		return authors;
	}

	public String getAbstract() {
		return abstractText;
	}

	/*
	 * Returns a shortened version of the abstract for display on the collection
	 * page. Max 120 characters (ish) and no html tags.
	 */
	public String getAbstractShort() {
		return abstractShort;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public String getThumbnailOrientation() {
		return thumbnailOrientation;
	}

	public String getShelfLocator() {
		return shelfLocator;
	}

	public JSONObject getJSON() {
		return json;
	}

	public JSONObject getSimplifiedJSON() {
		return simplejson;
	}

	@Override
	public int compareTo(Item o) {
		if (this.getOrder() > o.getOrder()) {
			return 1;
		} else if (this.getOrder() == o.getOrder()) {
			return 0;
		}
		return -1;
		// return getId().compareTo(o.getId());
	}

	/**
	 * This method takes in a list of people and iterates through them to
	 * produce a List of just the authors (role=aut) from that list.
	 * 
	 * @param people
	 * @return
	 */
	private List<Person> getPeopleByRole(List<Person> people, String role) {

		ArrayList<Person> peopleWithRole = new ArrayList<Person>();
		Iterator<Person> peopleIt = people.iterator();
		while (peopleIt.hasNext()) {
			Person person = peopleIt.next();
			if (role.equals(person.getRole())) {
				peopleWithRole.add(person);
			}
		}
		return peopleWithRole;

	}

}
