package ulcambridge.foundations.viewer.model;

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
	private String shelfLocator;
	private String abstractText;
	private String thumbnailURL;
	private int thumbnailWidth;
	private int thumbnailHeight;	
	private String abstractShort;

	public Item(String itemId, String itemTitle, String shelfLocator,
			String abstractText, String thumbnailURL, int thumbnailWidth,
			int thumbnailHeight) {

		this.id = itemId;
		this.title = itemTitle;
		this.shelfLocator = shelfLocator;
		this.abstractText = abstractText;
		this.thumbnailURL = thumbnailURL;
		this.thumbnailWidth = thumbnailWidth;
		this.thumbnailHeight = thumbnailHeight;

		String abstractShort = abstractText;
		abstractShort = abstractShort.replaceAll("\\<.*?>", ""); // remove tags
		// cut of next word after 120 characters
		if (abstractShort.length() > 120) {
			abstractShort = abstractShort.substring(0, 120 + abstractShort
					.substring(120).indexOf(" "));
		}
		this.abstractShort = abstractShort;

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
	
	public int getThumbnailWidth() {
		return thumbnailWidth;
	}
	
	public int getThumbnailHeight() {
		return thumbnailHeight;
	}	

	public String getShelfLocator() {
		return shelfLocator;
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

}
