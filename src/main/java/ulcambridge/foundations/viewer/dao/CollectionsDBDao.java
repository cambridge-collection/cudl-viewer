package ulcambridge.foundations.viewer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Collection;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
public class CollectionsDBDao implements CollectionsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CollectionsDBDao(final DataSource dataSource) {
        Assert.notNull(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String> getCollectionIds() {

        final String query = "SELECT collectionid FROM collections ORDER BY collectionorder";

        return jdbcTemplate.query(query,
            (resultSet, rowNum) -> resultSet.getString("collectionid"));
    }

    @Override
    public Collection getCollection(final String collectionId) {

        final String itemQuery = "SELECT items.itemid as itemid FROM items, itemsincollection WHERE " +
            "items.itemid = itemsincollection.itemid AND collectionid = ? AND iiifenabled = true ORDER BY itemorder";

        List<String> iiifEnabledItemIds = jdbcTemplate.query(itemQuery, new Object[]{collectionId},
            (resultSet, rowNum) -> resultSet.getString("itemid"));

        final String query = "SELECT collectionid, title, summaryurl, sponsorsurl, type, collectionorder, parentcollectionid, metadescription " +
            "FROM collections " +
            "WHERE collectionid = ? " +
            "ORDER BY collectionorder";

        return jdbcTemplate.queryForObject(query, new Object[]{collectionId},
            (resultSet, rowNum) -> new Collection(
                resultSet.getString("collectionid"),
                resultSet.getString("title"),
                getItemIds(resultSet.getString("collectionid")),
                resultSet.getString("summaryurl"),
                resultSet.getString("sponsorsurl"),
                resultSet.getString("type"),
                resultSet.getString("parentcollectionid"),
                iiifEnabledItemIds,
                resultSet.getString("metadescription")));
    }

    private List<String> getItemIds(final String collectionId) {

        final String query = "SELECT itemid FROM itemsincollection WHERE collectionid = ? AND visible=true ORDER BY itemorder";

         return jdbcTemplate.query(query, new Object[]{collectionId},
            (resultSet, rowNum) -> resultSet.getString("itemid"));

    }

    @Override
    public int getCollectionsRowCount() {
        final String query = "SELECT count(*) FROM collections";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    @Override
    public int getItemsInCollectionsRowCount() {
        final String query = "SELECT count(*) FROM itemsincollection";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    @Override
    public int getItemsRowCount() {
        final String query = "SELECT count(*) FROM items";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    @Override
    public List<String> getCollectionId(final String itemId) {

        final String query = "SELECT collectionid FROM itemsincollection WHERE itemid = ?";

        return jdbcTemplate.query(query, new Object[] { itemId },
            rs -> {
                List<String> colIds = new ArrayList<> ();
                while (rs.next()) {
                    String colId = rs.getString("collectionid");
                    colIds.add( colId );
                }
                return colIds;
            });
    }
}
