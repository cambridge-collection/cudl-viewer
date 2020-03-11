package ulcambridge.foundations.viewer.dao;

import com.google.common.collect.ImmutableList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Item;

import java.util.List;

public class DecoratedItemFactory implements ItemFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DecoratedItemFactory.class);

    @FunctionalInterface
    public interface ItemJSONPreProcessor {
        JSONObject preprocess(JSONObject itemJSON, String itemID);
    }

    @FunctionalInterface
    public interface ItemPostProcessor {
        Item postProcess(Item item, String itemID);
    }

    private final ItemFactory parent;
    private final List<ItemJSONPreProcessor> preProcessors;
    private final List<ItemPostProcessor> postProcessors;

    public DecoratedItemFactory(ItemFactory parent, Iterable<ItemJSONPreProcessor> preProcessors, Iterable<ItemPostProcessor> postProcessors) {
        Assert.notNull(parent, "parent is required");
        Assert.notNull(preProcessors, "preProcessors is required");
        Assert.notNull(postProcessors, "postProcessors is required");
        this.parent = parent;
        try {
            this.preProcessors = ImmutableList.copyOf(preProcessors);
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("preProcessors contained null", e);
        }
        try {
            this.postProcessors = ImmutableList.copyOf(postProcessors);
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("postProcessors contained null", e);
        }
    }

    @Override
    public Item itemFromJSON(final String itemId, JSONObject itemJson) {
        Assert.notNull(itemId, "itemId is required");
        Assert.notNull(itemJson, "itemJson is required");

        if(LOG.isTraceEnabled()) {
            LOG.trace("Item {} JSON before pre-processing: {}", itemId, itemJson.toString());
        }
        for(ItemJSONPreProcessor preProcessor : this.preProcessors) {
            LOG.debug("Pre-processing {} with {}", itemId, preProcessor);
            itemJson = preProcessor.preprocess(itemJson, itemId);
            if(itemJson == null) {
                throw new NullPointerException("ItemJSONPreProcessor returned null: " + preProcessor);
            }
            if(LOG.isTraceEnabled()) {
                LOG.trace("Item {} JSON after {}: {}", itemId, preProcessor, itemJson.toString());
            }
        }
        Item item = this.parent.itemFromJSON(itemId, itemJson);
        LOG.debug("Created item {} with {}: {}", itemId, this.parent, item);

        for(ItemPostProcessor postProcessor : this.postProcessors) {
            LOG.debug("Post-processing {} with {}", itemId, postProcessor);
            item = postProcessor.postProcess(item, itemId);
            if(item == null) {
                throw new NullPointerException("ItemPostProcessor returned null: " + postProcessor);
            }
            if(LOG.isTraceEnabled()) {
                LOG.trace("Item {} after post-processing: {}", itemId, item);
            }
        }

        return item;
    }
}
