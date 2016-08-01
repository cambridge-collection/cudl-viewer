package ulcambridge.foundations.viewer.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class RefreshCache {

    private static CollectionFactory collectionFactory;
    private static ItemFactory itemFactory;

    private String jsonLocalPathMasters = Properties.getString("admin.git.json.localpath");
    private String jsonUrl = Properties.getString("admin.git.json.url");
    private GitHelper jsonGit = new GitHelper(jsonLocalPathMasters,jsonUrl);

    private String dbLocalPathMasters = Properties.getString("admin.git.db.localpath");
    private String dbUrl = Properties.getString("admin.git.db.url");
    private GitHelper dbGit = new GitHelper(dbLocalPathMasters,dbUrl);

    private String lastJSONRevision = jsonGit.getLastRevision();
    private String lastDBRevision = dbGit.getLastRevision();

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {

        collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        itemFactory = factory;
    }


    @Scheduled(fixedRate = 300000)  // Check every 5 mins.
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

    public static void refreshDB() {
        collectionFactory.init();
    }

    public static void refreshJSON() {
        itemFactory.clearItemCache();
    }

}
