package ulcambridge.foundations.viewer.admin;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

/**
 * @author rekha
 */
@Component
public class RefreshCache {

    private final CollectionFactory collectionFactory;
    private final Cache<String, Item> itemCache;

    // FIXME: Inject this stuff
    private String jsonLocalPathMasters = Properties.getString("admin.git.json.localpath");
    private String jsonUrl = Properties.getString("admin.git.json.url");
    private GitHelper jsonGit = new GitHelper(jsonLocalPathMasters,jsonUrl);

    private String dbLocalPathMasters = Properties.getString("admin.git.db.localpath");
    private String dbUrl = Properties.getString("admin.git.db.url");
    private GitHelper dbGit = new GitHelper(dbLocalPathMasters,dbUrl);

    private String lastJSONRevision = jsonGit.getLastRevision();
    private String lastDBRevision = dbGit.getLastRevision();

    @Autowired
    public RefreshCache(CollectionFactory collectionFactory, @Qualifier("itemCache") Cache<String, Item> itemCache) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemCache, "itemCache is required");

        this.collectionFactory = collectionFactory;
        this.itemCache = itemCache;
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)  // Check every 5 mins.
    public void checkForUpdates() {

            String latestDBRevision = dbGit.getLastRevision();
            String latestJSONRevision = jsonGit.getLastRevision();

            if (latestDBRevision != null &&
                !lastDBRevision.equals(latestDBRevision)) {
                // Get fresh collections from the database
                refreshDB();
                lastDBRevision = latestDBRevision;
            }

            if (latestJSONRevision != null &&
                !lastJSONRevision.equals(latestJSONRevision)) {
                // empty item cache
                refreshJSON();
                lastJSONRevision = latestJSONRevision;

            }
    }

    public void refreshDB() {
        collectionFactory.init();
    }

    public void refreshJSON() {
        itemCache.invalidateAll();
    }

}
