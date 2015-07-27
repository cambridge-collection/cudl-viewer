package ulcambridge.foundations.viewer;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class RefreshCache {

    private CollectionFactory collectionFactory;
    private ItemFactory itemFactory;
    private final String adminEnabled = Properties.getString("admin.enabled");

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }

    @Scheduled(fixedRate = 300000)  // Check every 5 mins. 
    public void refresh() {
        if (adminEnabled.equals("false")) {
            Timestamp oldtimestamp = this.collectionFactory.getOldTimestamp();
            Timestamp currenttimestamp = this.collectionFactory.getCurrentTimestamp();

            int compareTo = oldtimestamp.compareTo(currenttimestamp);
            if (compareTo < 0) {
                //get fresh collections from the database
                this.collectionFactory.init();
                // empty item cache
                ItemFactory itemfactory = new ItemFactory();
                itemfactory.clearItemCache();
                System.out.println("refreshing...");
            }

        }
    }
}
