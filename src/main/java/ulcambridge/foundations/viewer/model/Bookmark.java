package ulcambridge.foundations.viewer.model;

public class Bookmark {

	private String username;
	private String itemId;
	private int page;
	private String thumbnailURL;

	public Bookmark(String username, String itemId, int page, String thumbnailURL) {
       this.username = username;
       this.itemId = itemId;
       this.setPage(page);	  
       this.thumbnailURL = thumbnailURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

}
