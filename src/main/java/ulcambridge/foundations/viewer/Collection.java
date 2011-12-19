package ulcambridge.foundations.viewer;

import java.util.List;

public class Collection {

	String id;
	String title;
	List<String> items;
	String url;
	String html;

	public Collection(String collectionID, String collectionTitle,
			List<String> collectionItems, String collectionURL,
			String collectionHTML) {

		this.id = collectionID;
		this.title = collectionTitle;
		this.items = collectionItems;
		this.url = collectionURL;
		this.html = collectionHTML;

	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getItems() {
		return items;
	}

	public String getURL() {
		return url;
	}

	public String getHTML() {
		return html;
	}
}
