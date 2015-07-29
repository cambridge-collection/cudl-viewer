package ulcambridge.foundations.viewer.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.dao.LastUpdateDao;
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class RefreshCache {

    private CollectionFactory collectionFactory;
    private ItemFactory itemFactory;
    private final String adminEnabled = Properties.getString("admin.enabled");
    private LastUpdateDao lastUpdateDao;
    private Timestamp lastJSONLoad = new Timestamp((new Date()).getTime());
    private Timestamp lastDBLoad = new Timestamp((new Date()).getTime());
    
    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }
    
    @Autowired
    public void setLastUpdateDao(LastUpdateDao dao) {
        this.lastUpdateDao = dao;
    }

    @Scheduled(fixedRate = 300000)  // Check every 5 mins. 
    public void refresh() {
    	
            Hashtable<String, Timestamp> lastUpdates = lastUpdateDao.getLastUpdate(); 
            		
            if (lastDBLoad.compareTo(lastUpdates.get("db")) < 0) {
                //get fresh collections from the database
                collectionFactory.init();
                lastDBLoad = lastUpdates.get("db");
                System.out.println("refreshing db...");
            }   
            
            if (lastJSONLoad.compareTo(lastUpdates.get("cudl-data")) < 0) {
                // empty item cache
                itemFactory.clearItemCache();
                lastJSONLoad = lastUpdates.get("json");
                System.out.println("refreshing json...");
            }
    }
    
}
