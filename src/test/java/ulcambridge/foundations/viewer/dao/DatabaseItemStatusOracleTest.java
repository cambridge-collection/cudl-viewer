//package ulcambridge.foundations.viewer.dao;
//
//import com.opentable.db.postgres.embedded.FlywayPreparer;
//import com.opentable.db.postgres.junit5.EmbeddedPostgresExtension;
//import com.opentable.db.postgres.junit5.PreparedDbExtension;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//import java.util.Arrays;
//
//import static com.google.common.truth.Truth.assertThat;
//
//public class DatabaseItemStatusOracleTest {
//
//    @RegisterExtension
//    public static PreparedDbExtension db = EmbeddedPostgresExtension.preparedDatabase(FlywayPreparer.forClasspathLocation("db/migration"));
//
//    private JdbcTemplate jdbcTemplate;
//    private DatabaseItemStatusOracle statusOracle;
//
//    @BeforeEach
//    public void setupDb() {
//        DataSource dataSource = db.getTestDatabase();
//        jdbcTemplate = new JdbcTemplate(dataSource);
//
//        jdbcTemplate.execute("TRUNCATE items CASCADE");
//        jdbcTemplate.batchUpdate("INSERT INTO items (itemid, taggingstatus, iiifenabled) VALUES (?, ?, ?)", Arrays.asList(
//            new Object[]{"MS-FOO-001", true, true},
//            new Object[]{"MS-FOO-002", false, true},
//            new Object[]{"MS-FOO-003", true, false},
//            new Object[]{"MS-FOO-004", false, false}
//        ));
//
//        statusOracle = new DatabaseItemStatusOracle(jdbcTemplate, false, false);
//    }
//
//    @Test
//    public void isIIIFEnabled() {
//        assertThat(statusOracle.isIIIFEnabled("MS-FOO-001")).isTrue();
//        assertThat(statusOracle.isIIIFEnabled("MS-FOO-002")).isTrue();
//        assertThat(statusOracle.isIIIFEnabled("MS-FOO-003")).isFalse();
//        assertThat(statusOracle.isIIIFEnabled("MS-FOO-004")).isFalse();
//    }
//
//    @Test
//    public void isTaggingEnabled() {
//        assertThat(statusOracle.isTaggingEnabled("MS-FOO-001")).isTrue();
//        assertThat(statusOracle.isTaggingEnabled("MS-FOO-002")).isFalse();
//        assertThat(statusOracle.isTaggingEnabled("MS-FOO-003")).isTrue();
//        assertThat(statusOracle.isTaggingEnabled("MS-FOO-004")).isFalse();
//    }
//
//    @ParameterizedTest
//    @ValueSource(booleans = {true, false})
//    public void iiifDefaultIsUsedIfItemNotInDb(boolean isEnabled) {
//        statusOracle = new DatabaseItemStatusOracle(jdbcTemplate, isEnabled, false);
//
//        assertThat(statusOracle.isIIIFEnabled("UNKNOWN-ITEM")).isEqualTo(isEnabled);
//    }
//
//    @ParameterizedTest
//    @ValueSource(booleans = {true, false})
//    public void taggingDefaultIsUsedIfItemNotInDb(boolean isEnabled) {
//        statusOracle = new DatabaseItemStatusOracle(jdbcTemplate, false, isEnabled);
//
//        assertThat(statusOracle.isTaggingEnabled("UNKNOWN-ITEM")).isEqualTo(isEnabled);
//    }
//}
