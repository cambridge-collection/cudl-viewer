//package ulcambridge.foundations.viewer.dao;
//
//import org.springframework.dao.IncorrectResultSizeDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.util.Assert;
//
//import java.util.List;
//
//public class DatabaseItemStatusOracle implements ItemStatusOracle {
//    private final JdbcTemplate db;
//    private final boolean isIIIFEnabledByDefault;
//    private final boolean isTaggingEnabledByDefault;
//
//    public DatabaseItemStatusOracle(JdbcTemplate db,
//                                    boolean isIIIFEnabledByDefault,
//                                    boolean isTaggingEnabledByDefault) {
//        Assert.notNull(db, "db is required");
//        this.db = db;
//        this.isIIIFEnabledByDefault = isIIIFEnabledByDefault;
//        this.isTaggingEnabledByDefault = isTaggingEnabledByDefault;
//    }
//
//    public boolean isIIIFEnabled(String itemId) {
//        final String query = "SELECT iiifenabled FROM items WHERE itemid = ?";
//
//        return this.getDefault(db.queryForList(query, new Object[]{itemId},
//            Boolean.class), isIIIFEnabledByDefault);
//    }
//
//    public boolean isTaggingEnabled(String itemId) {
//        final String query = "SELECT taggingstatus FROM items WHERE itemid = ?";
//
//        return this.getDefault(db.queryForList(query, new Object[]{itemId},
//            Boolean.class), isTaggingEnabledByDefault);
//    }
//
//    private boolean getDefault(List<Boolean> result, boolean default_) {
//        if(result.size() > 1) {
//            throw new IncorrectResultSizeDataAccessException("Expected 0 or 1 rows", 1, result.size());
//        }
//        return result.size() == 0 ? default_ : result.get(0);
//    }
//}
