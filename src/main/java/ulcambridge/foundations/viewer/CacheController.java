package ulcambridge.foundations.viewer;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ulcambridge.foundations.viewer.model.Item;

@Controller
@Profile("!test")
@RequestMapping("/cache")
public class CacheController {

    private final CollectionFactory collectionFactory;
    private final Cache<String, Item> itemCache;

    @Autowired
    public CacheController(CollectionFactory collectionFactory, @Qualifier("itemCache") Cache<String, Item> itemCache) {
        Assert.notNull(collectionFactory, "collectionFactory is required");
        Assert.notNull(itemCache, "itemCache is required");

        this.collectionFactory = collectionFactory;
        this.itemCache = itemCache;
    }

    // on path /cache/
    // TODO restrict access?
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    @ResponseBody
    public String handleRefreshRequest() {

        collectionFactory.init();
        itemCache.invalidateAll();

        return "{cache-refresh:true}";
    }

}
