package ulcambridge.foundations.viewer.utils;

import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
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
import java.util.concurrent.atomic.AtomicBoolean;


@Component
@Profile("!test")
public class RefreshCache {

    private final CollectionFactory collectionFactory;
    private final Cache<String, Item> itemCache;
    private final String cachingEnabled;
    private final String itemJSONDirectory;
    private final String unreleasedDataDirectory;
    private long lastModified;
    private final AtomicBoolean refreshInProgress = new AtomicBoolean(false);
    private volatile long lastRefreshStartTime;
    private volatile long lastRefreshEndTime;
    private volatile boolean lastRefreshSuccessful;

    @Autowired
    public RefreshCache(CollectionFactory collectionFactory, @Qualifier("itemCache") Cache<String, Item> itemCache,
                        @Value("${caching.enabled:true}") String cachingEnabled,
                        @Value("${itemJSONDirectory}") String itemJSONDirectory,
                        @Value("${unreleasedDataDirectory:}") String unreleasedDataDirectory) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemCache, "itemCache is required");

        this.collectionFactory = collectionFactory;
        this.itemCache = itemCache;
        this.cachingEnabled = cachingEnabled;
        this.itemJSONDirectory = itemJSONDirectory;
        this.unreleasedDataDirectory = unreleasedDataDirectory;
        lastModified = (new Date()).getTime();
    }

    /**
     * NOTE: refreshing the cache only when the JSON has changed (so if only DB
     * has changed it will not update).
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)  // Check every 10 mins.
    public void checkForUpdates() {

        if (!cachingEnabled.equals("true")) {
            try {
                long latestModified = latestModifiedInDir(new File(itemJSONDirectory));

                // Also check the unreleased json and collections directories so
                // that new unreleased content triggers an auto-refresh too.
                if (!unreleasedDataDirectory.isBlank()) {
                    latestModified = Math.max(latestModified,
                        latestModifiedInDir(new File(unreleasedDataDirectory, "json")));
                    latestModified = Math.max(latestModified,
                        latestModifiedInDir(new File(unreleasedDataDirectory, "collections")));
                }

                if (latestModified > lastModified) {
                    lastModified = latestModified;
                    refreshAllAsync();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the last-modified time (millis) of the most recently changed file
     * in the given directory, or 0 if the directory is absent or empty.
     */
    private long latestModifiedInDir(File dir) throws IOException {
        if (!dir.isDirectory()) return 0;
        File[] files = dir.listFiles((FileFilter) FileFilterUtils.fileFileFilter());
        if (files == null || files.length == 0) return 0;
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return Files.getLastModifiedTime(files[0].toPath()).toMillis();
    }

    /**
     * Trigger a full cache refresh asynchronously. If a refresh is already in
     * progress this call is a no-op.
     */
    @Async
    public void refreshAllAsync() {
        if (!refreshInProgress.compareAndSet(false, true)) {
            return;
        }

        lastRefreshStartTime = System.currentTimeMillis();
        boolean success = false;
        try {
            refreshCollectionData();
            refreshJSON();
            success = true;
        } finally {
            lastRefreshSuccessful = success;
            lastRefreshEndTime = System.currentTimeMillis();
            refreshInProgress.set(false);
        }
    }

    public boolean isRefreshInProgress() {
        return refreshInProgress.get();
    }

    public long getLastRefreshStartTime() {
        return lastRefreshStartTime;
    }

    public long getLastRefreshEndTime() {
        return lastRefreshEndTime;
    }

    public boolean isLastRefreshSuccessful() {
        return lastRefreshSuccessful;
    }

    public void refreshCollectionData() {
        collectionFactory.refreshCollections();
        collectionFactory.init(true);
    }

    public void refreshJSON() {
        itemCache.invalidateAll();
    }

}
