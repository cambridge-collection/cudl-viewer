package ulcambridge.foundations.viewer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Collection;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CollectionsDBDao implements CollectionsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CollectionsDBDao(DataSource dataSource) {
        Assert.notNull(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<String> getCollectionIds() {

        String query = "SELECT collectionid FROM collections ORDER BY collectionorder";

        return (List<String>) jdbcTemplate.query(query,
                new RowMapper<String>() {
                    public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return resultSet.getString("collectionid");
                    }
                });
    }

    public Collection getCollection(String collectionId) {

        String query = "SELECT collectionid, title, summaryurl, sponsorsurl, type, collectionorder, parentcollectionid, metadescription " +
            "FROM collections " +
            "WHERE collectionid = ? " +
            "ORDER BY collectionorder";

        return (Collection) jdbcTemplate.queryForObject(query, new Object[]{collectionId},
                new RowMapper<Collection>() {
                    public Collection mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return new Collection(resultSet.getString("collectionid"),
                                resultSet.getString("title"), getItemIds(resultSet.getString("collectionid")),
                                resultSet.getString("summaryurl"), resultSet.getString("sponsorsurl"),
                                resultSet.getString("type"), resultSet.getString("parentcollectionid"),
                                resultSet.getString("metadescription"));
                    }
                });
    }

    private List<String> getItemIds(String collectionId) {

        String query = "SELECT itemid FROM itemsincollection WHERE collectionid = ? AND visible=true ORDER BY itemorder";

        return (List<String>) jdbcTemplate.query(query, new Object[]{collectionId},
                new RowMapper<String>() {
                    public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return resultSet.getString("itemid");
                    }
                });
    }

    public int getCollectionsRowCount() {
        String query = "SELECT count(*) FROM collections";
        Integer rowcount = jdbcTemplate.queryForObject(query, Integer.class);
        return rowcount;
    }

    public int getItemsInCollectionsRowCount() {
        String query = "SELECT count(*) FROM itemsincollection";
        Integer rowcount = jdbcTemplate.queryForObject(query, Integer.class);
        return rowcount;
    }

    public int getItemsRowCount() {
        String query = "SELECT count(*) FROM items";
        Integer rowcount = jdbcTemplate.queryForObject(query, Integer.class);
        return rowcount;
    }

    //
    // XXX tagging switch
    //

    @Override
    public boolean isItemTaggable(String itemId) {

        String query = "SELECT taggingstatus FROM items WHERE itemid = ?";

        return jdbcTemplate.query(query, new Object[] { itemId },
                new ResultSetExtractor<Boolean>() {
                    @Override
                    public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while (rs.next()) {
                            return rs.getBoolean("taggingstatus");
                        }
                        return false;
                    }
                });
    }

    @Override
    public List<String> getCollectionId(String itemId) {

        String query = "SELECT collectionid FROM itemsincollection WHERE itemid = ?";

        return jdbcTemplate.query(query, new Object[] { itemId },
                new ResultSetExtractor<List<String>>() {
                    @Override
                    public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<String> colIds = new ArrayList<String> ();
                        while (rs.next()) {
                            String colId = rs.getString("collectionid");
                            colIds.add( colId );
                        }
                        return colIds;
                    }
        });
    }

}
