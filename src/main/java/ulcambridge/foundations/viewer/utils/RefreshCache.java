package ulcambridge.foundations.viewer.utils;

import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.model.Item;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;


@Component
@Profile("!test")
public class RefreshCache {

    private final CollectionFactory collectionFactory;
    private final Cache<String, Item> itemCache;
    private final String cachingEnabled;
    private final String itemJSONDirectory;
    private long lastModified;

    @Autowired
    public RefreshCache(CollectionFactory collectionFactory, @Qualifier("itemCache") Cache<String, Item> itemCache,
                        @Value("${caching.enabled:true}") String cachingEnabled, @Value("${itemJSONDirectory}") String itemJSONDirectory) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemCache, "itemCache is required");

        this.collectionFactory = collectionFactory;
        this.itemCache = itemCache;
        this.cachingEnabled = cachingEnabled;
        this.itemJSONDirectory = itemJSONDirectory;
        lastModified = (new Date()).getTime();
    }

    /**
     * NOTE: refreshing the cache only when the JSON has changed (so if only DB
     * has changed it will not update).
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)  // Check every 10 mins.
    public void checkForUpdates() {

        try {

            File dir = new File(itemJSONDirectory);
            if (dir.isDirectory()) {
                File[] dirFiles = dir.listFiles((FileFilter) FileFilterUtils.fileFileFilter());
                if (dirFiles != null && dirFiles.length > 0) {
                    Arrays.sort(dirFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    long lastModifiedFile = Files.getLastModifiedTime(dirFiles[0].toPath()).toMillis();

                    // If file has changed in JSON refresh cache.
                    if (lastModifiedFile>lastModified) {

                        lastModified = lastModifiedFile;
                        refreshDB();
                        refreshJSON();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void refreshDB() {
        collectionFactory.init();
    }

    public void refreshJSON() {
        itemCache.invalidateAll();
    }

}
