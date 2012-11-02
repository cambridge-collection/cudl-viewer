package ulcambridge.foundations.viewer.dao;

import java.util.List;

import ulcambridge.foundations.viewer.exceptions.TooManyBookmarksException;
import ulcambridge.foundations.viewer.model.Bookmark;

public interface BookmarkDao {

	public List<Bookmark> getByUsername(String username);
	public void add(Bookmark bookmark) throws TooManyBookmarksException;
	public void delete(String username, String itemId, int page);
}
