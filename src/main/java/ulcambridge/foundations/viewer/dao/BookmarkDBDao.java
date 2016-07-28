package ulcambridge.foundations.viewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ulcambridge.foundations.viewer.exceptions.TooManyBookmarksException;
import ulcambridge.foundations.viewer.model.Bookmark;

public class BookmarkDBDao implements BookmarkDao {

    private JdbcTemplate jdbcTemplate;
    private int bookmarkLimit = 500; // could make this read from a properties
                                        // file.

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Bookmark> getByUsername(String username) {

        String query = "SELECT username, itemid, page, thumbnailURL, dateadded FROM bookmarks where username = ? ORDER BY dateadded DESC ";

        try {
            return (List<Bookmark>) jdbcTemplate.query(query,
                    new Object[] { username }, new RowMapper<Bookmark>() {
                        public Bookmark mapRow(ResultSet resultSet, int rowNum)
                                throws SQLException {
                            return new Bookmark(
                                    resultSet.getString("username"), resultSet
                                            .getString("itemid"), resultSet
                                            .getInt("page"), resultSet
                                            .getString("thumbnailURL"),
                                    resultSet.getDate("dateadded"));
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean exists(Bookmark bookmark) {

        String query = "SELECT username, itemid, page, thumbnailURL, dateadded FROM bookmarks where username = ? AND itemid = ? AND page = ? ";
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
                                            .getString("thumbnailURL"),
                                    resultSet.getDate("dateadded"));
                        }
                    });

            // found the bookmark.
            if (dbBookmark != null) {
                return true;
            }

        } catch (Exception e) {
            // hasn't found the bookmark.
            e.printStackTrace();
        }
        return false;
    }

    private int getNumberOfBookmarks(String username) {

        String query = "SELECT count(*) as count FROM bookmarks where username = ?";

        Integer count = (Integer) jdbcTemplate.queryForObject(query,
                new Object[] { username }, new RowMapper<Integer>() {
                    public Integer mapRow(ResultSet resultSet, int rowNum)
                            throws SQLException {
                        return new Integer(resultSet.getInt("count"));
                    }
                });

        return count.intValue();

    }

    /**
     * Adds a bookmark to the database if it doesn't exist already.
     */
    public void add(Bookmark bookmark) throws TooManyBookmarksException {

        // Check if the user has reached the limit for bookmarks he is allowed to add.
        if (getNumberOfBookmarks(bookmark.getUsername())>=this.bookmarkLimit) {
            throw new TooManyBookmarksException("You have reached the limit on the amount of bookmarks allowed.");
        }

        // Check to see if bookmark exists
        if (!exists(bookmark)) {

            String sql = "INSERT INTO bookmarks (username, itemid, page, thumbnailURL, dateadded) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, new Object[] { bookmark.getUsername(),
                    bookmark.getItemId(), bookmark.getPage(), bookmark.getThumbnailURL(), bookmark.getDateadded() });

        }

    }

    @Override
    public void delete(String username, String itemId, int page) {

        String sql = "DELETE FROM bookmarks where username = ? AND itemid = ? AND page = ?";
        jdbcTemplate.update(sql, new Object[] { username, itemId, page });

    }

}
