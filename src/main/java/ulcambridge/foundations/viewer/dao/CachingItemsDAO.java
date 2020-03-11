package ulcambridge.foundations.viewer.dao;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Item;

/**
 * An {@link ItemsDao} which uses a Caffeine cache to cache requested items.
 */
public class CachingItemsDAO implements ItemsDao {
    private final ItemsDao upstreamDAO;
    private final Cache<String, Item> cache;

    public CachingItemsDAO(Cache<String, Item> cache, ItemsDao upstreamDAO) {
        Assert.notNull(cache, "cache is required");
        Assert.notNull(upstreamDAO, "upstreamDAO is required");

        this.upstreamDAO = upstreamDAO;
        this.cache = cache;
    }

    @Override
    public Item getItem(String itemId) {
        return cache.get(itemId, upstreamDAO::getItem);
    }
}
