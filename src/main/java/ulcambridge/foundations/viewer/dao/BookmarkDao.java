package ulcambridge.foundations.viewer.dao;

import java.util.List;

import ulcambridge.foundations.viewer.model.Bookmark;

public interface BookmarkDao {

	public List<Bookmark> getByUsername(String username);
	public void add(Bookmark bookmark);
	public void delete(Bookmark bookmark);
}
