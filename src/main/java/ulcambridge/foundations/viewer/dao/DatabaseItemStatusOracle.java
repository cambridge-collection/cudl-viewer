package ulcambridge.foundations.viewer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DatabaseItemStatusOracle implements ItemStatusOracle {
    private final JdbcTemplate db;

    @Autowired
    public DatabaseItemStatusOracle(JdbcTemplate db) {
        Assert.notNull(db, "db is required");
        this.db = db;
    }

    public boolean isIIIFEnabled(String itemId) {
        final String query = "SELECT iiifenabled FROM items WHERE itemid = ?";

        return db.queryForObject(query, new Object[]{itemId},
            Boolean.class);
    }

    public boolean isTaggingEnabled(String itemId) {
        final String query = "SELECT taggingstatus FROM items WHERE itemid = ?";

        return db.queryForObject(query, new Object[]{itemId},
            Boolean.class);
    }
}
