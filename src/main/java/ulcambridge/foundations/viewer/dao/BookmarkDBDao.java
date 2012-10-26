package ulcambridge.foundations.viewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ulcambridge.foundations.viewer.model.Bookmark;
import ulcambridge.foundations.viewer.model.Collection;

public class BookmarkDBDao implements BookmarkDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Bookmark> getByUsername(String username) {

		String query = "SELECT username, itemid, page, thumbnailURL FROM bookmarks where username = ? ";

		try {
			return (List<Bookmark>) jdbcTemplate.query(query,
					new Object[] { username }, new RowMapper<Bookmark>() {
						public Bookmark mapRow(ResultSet resultSet, int rowNum)
								throws SQLException {
							return new Bookmark(
									resultSet.getString("username"), resultSet
											.getString("itemid"), resultSet
											.getInt("page"), resultSet
											.getString("thumbnailURL"));
						}
					});

		} catch (Exception e) {
			return null;
		}

	}

	public boolean exists(Bookmark bookmark) {

		String query = "SELECT username, itemid, page, thumbnailURL FROM bookmarks where username = ? AND itemid = ? AND page = ? ";
		try {
			Bookmark dbBookmark = (Bookmark) jdbcTemplate.queryForObject(query,
					new Object[] { bookmark.getUsername(),
							bookmark.getItemId(), bookmark.getPage() },
					new RowMapper<Bookmark>() {
						public Bookmark mapRow(ResultSet resultSet, int rowNum)
								throws SQLException {
							return new Bookmark(
									resultSet.getString("username"), resultSet
											.getString("itemid"), resultSet
											.getInt("page"), resultSet
											.getString("thumbnailURL"));
						}
					});

			// found the bookmark.
			if (dbBookmark != null) {
				return true;
			}

		} catch (Exception e) {
			// hasn't found the bookmark.
		}
		return false;
	}

	/**
	 * Adds a bookmark to the database if it doesn't exist already.
	 */
	public void add(Bookmark bookmark) {

		// Check to see if bookmark exists
		if (!exists(bookmark)) {

			String sql = "INSERT INTO bookmarks (username, itemid, page, thumbnailURL) values (?, ?, ?, ?)";
			jdbcTemplate.update(sql, new Object[] { bookmark.getUsername(),
					bookmark.getItemId(), bookmark.getPage(), bookmark.getThumbnailURL() });

		}

	}

	@Override
	public void delete(Bookmark bookmark) {

		// Check to see if bookmark exists
		if (exists(bookmark)) {

			String sql = "DELETE FROM bookmarks where username = ? AND itemid = ? AND page = ?";
			jdbcTemplate.update(sql, new Object[] { bookmark.getUsername(),
					bookmark.getItemId(), bookmark.getPage() });
		}

	}

}
