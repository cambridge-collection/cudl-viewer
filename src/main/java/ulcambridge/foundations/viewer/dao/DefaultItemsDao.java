package ulcambridge.foundations.viewer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Item;

@Component
public class DefaultItemsDao implements ItemsDao {
    private final ItemFactory itemFactory;
    private final JSONLoader itemJSONLoader;

    @Autowired
    public DefaultItemsDao(ItemFactory itemFactory, JSONLoader itemJSONLoader) {
        Assert.notNull(itemFactory, "itemFactory is required");
        Assert.notNull(itemJSONLoader, "itemJSONLoader is required");

        this.itemFactory = itemFactory;
        this.itemJSONLoader = itemJSONLoader;
    }

    @Override
    public Item getItem(String itemId) {
        return this.itemFactory.itemFromJSON(itemId, this.itemJSONLoader.loadJSON(itemId));
    }
}
