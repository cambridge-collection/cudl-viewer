package ulcambridge.foundations.viewer.model;

import java.util.List;

public class Collection implements Comparable<Collection> {

	private static int orderCount = 1;
	private int order;
	private String id;
	private String title;
	private List<String> itemIds;
	private List<Item> items;
	private String url;
	private String summary;
	private String sponsors;
	private String type;

	public Collection(String collectionID, String collectionTitle,
			List<String> collectionItemIds, List<Item> collectionItems,
			String collectionSummary, String collectionSponsors,
			String collectionType) {

		this.id = collectionID;
		this.title = collectionTitle;
		this.itemIds = collectionItemIds;
		this.items = collectionItems;
		this.url = "/collections/" + collectionID;
		this.summary = collectionSummary;
		this.sponsors = collectionSponsors;
		this.type = collectionType;

		orderCount++;
		this.order = orderCount;
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

	public List<String> getItemIds() {
		return itemIds;
	}

	public List<Item> getItems() {
		return items;
	}

	public String getURL() {
		return url;
	}

	public String getSummary() {
		return summary;
	}

	public String getSponsors() {
		return sponsors;
	}

	public String getType() {
		return type;
	}

	@Override
	public int compareTo(Collection o) {
		if (this.getOrder() > o.getOrder()) {
			return 1;
		} else if (this.getOrder() == o.getOrder()) {
			return 0;
		}
		return -1;

		// return getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Collection) {
			return this.id.equals(((Collection) o).id);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}	
}
