package ulcambridge.foundations.viewer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	protected int order;	
	protected String id;
	protected String type;
	protected String title;
	protected String fullTitle;
	protected List<Person> authors;
	protected String shelfLocator;
	protected String abstractText;
	protected String thumbnailURL;
	protected String thumbnailOrientation;
	protected String abstractShort;
	protected List<String> pageLabels;
	protected List<String> pageThumbnailURLs;
	protected JSONObject json; // used for document view
	protected JSONObject simplejson; // used for collection view

	public Item(String itemId, String itemType, String itemTitle, List<Person> authors,
			String itemShelfLocator, String itemAbstract,
			String itemThumbnailURL, String thumbnailOrientation, 
			List<String> pageLabels, List<String> pageThumbnailURLs,
			JSONObject itemJson) {

		this.id = itemId;
		this.type = itemType;
		this.json = itemJson;
		this.title = itemTitle;
		this.fullTitle = itemTitle;
		this.shelfLocator = itemShelfLocator;
		this.abstractText = itemAbstract;
		this.thumbnailURL = itemThumbnailURL;
		this.thumbnailOrientation = thumbnailOrientation;
		this.authors = authors;
		
		// default to placeholder image
		if (thumbnailURL == null ||thumbnailURL.equals("")) {
			this.thumbnailURL = "/img/no-thumbnail.jpg";
			this.thumbnailOrientation =	"landscape";
		}

		// Make short abstract
		this.abstractShort = makeShortAbstract(itemAbstract);
		
		// cut of next word after 120 characters in the title
		if (title.length() > 120) {
			this.title = title.substring(0, 120 + title
					.substring(120).indexOf(" "))+ "...";
		}
		
		this.pageLabels = pageLabels;
		this.pageThumbnailURLs = pageThumbnailURLs;
		
		// Make simple JSON
		try {
			simplejson = new JSONObject();
			simplejson.append("id", this.getId());
			simplejson.append("title", this.getTitle());
			simplejson.append("shelfLocator", this.getShelfLocator());
			simplejson.append("abstractText", this.getAbstract());
			simplejson.append("abstractShort", this.getAbstractShort());			
			simplejson.append("thumbnailURL", this.getThumbnailURL());
			simplejson.append("thumbnailOrientation", this.getThumbnailOrientation());
			JSONArray authorJSON = new JSONArray();
			authorJSON.addAll(this.getAuthors());
			simplejson.append("authors", authorJSON);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		orderCount++;
		this.order = orderCount;

	}
	
	public List<String> getPageThumbnailURLs() {
		return pageThumbnailURLs;
	}
	
	public List<String> getPageLabels() {
		return pageLabels;
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
	
	public String getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getTitleFullform() {
		return fullTitle;
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
	
	private String makeShortAbstract(String fullAbstract) {
		
		String abstractShort = fullAbstract;
		
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

		// cut of next word after 120 characters in the abstract
		if (abstractShort.length() > 120) {
			abstractShort = abstractShort.substring(0, 120 + abstractShort
					.substring(120).indexOf(" "));
		}
		
		return abstractShort;
	}
	
	
	/**
	 * Returns the truncated version of the input if any words contained
	 * in the input are more than specified length. Used in wrapping. 
	 *  
	 * @param input
	 * @param length
	 * @return
	 */
	private String truncateLongWords(String input, int length) {
		
		// if there's a word that is more than 25 characters long add hyphen to it.
		// for wrapping
		Pattern pattern = Pattern.compile("[\\S]{"+length+",}");
		Matcher matcher = pattern.matcher(input);
				
		if(matcher.find()){
			int index = matcher.start();
			return input.substring(0, index+length);
		}
		
		return input;
	}

}
