package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.UriTemplate;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;
import ulcambridge.foundations.viewer.utils.Utils;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public abstract class BaseImageURLResolver implements DecoratedItemFactory.ItemJSONPreProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BaseImageURLResolver.class);

    private final String imageURLPropertyName;
    private final UriTemplate imageURLValueTemplate;
    private final String objectName;

    public BaseImageURLResolver(String objectName, String imageURLPropertyName, String imageURLValueTemplate) {
        Assert.notNull(objectName, "objectName is required");
        Assert.notNull(imageURLPropertyName, "imageURLPropertyName is required");
        Assert.notNull(imageURLValueTemplate, "imageURLValueTemplate is required");
        this.objectName = objectName;
        this.imageURLPropertyName = imageURLPropertyName;
        try {
            this.imageURLValueTemplate = new UriTemplate(imageURLValueTemplate);
        }
        catch (RuntimeException e) {
            throw new IllegalArgumentException(String.format("Invalid imageURLValueTemplate \"%s\": %s", imageURLValueTemplate, e));
        }
    }

    protected abstract Stream<JSONObject> getTargetObjects(JSONObject itemJSON, String itemID);

    @Override
    public JSONObject preprocess(JSONObject itemJSON, String itemID) {
        this.getTargetObjects(itemJSON, itemID).forEach(obj -> this.updateObject(itemID, obj));
        return itemJSON;
    }

    private void updateObject(String itemID, JSONObject object) {
        Map<String, ?> values = Utils.atomicValues(object);
        Set<String> missingVars = Sets.difference(ImmutableSet.copyOf(this.imageURLValueTemplate.getVariableNames()), values.keySet());
        if(!missingVars.isEmpty()) {
            LOG.trace("Not rendering template \"{}\" for {} property: \"{}\" of item ID: \"{}\"; referenced keys are missing: {}",
                this.imageURLValueTemplate, this.objectName, this.imageURLPropertyName, itemID, missingVars);
            return;
        }

        LOG.trace("Rendering template: \"{}\" with values: {} for {} property: \"{}\" of item ID: \"{}\"",
            this.imageURLValueTemplate, values, this.objectName, this.imageURLPropertyName, itemID);
        URI result = this.imageURLValueTemplate.expand(values);
        object.put(this.imageURLPropertyName, result.toString());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("imageURLPropertyName", imageURLPropertyName)
            .add("imageURLValueTemplate", imageURLValueTemplate)
            .toString();
    }
}
