package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.base.MoreObjects;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.UriTemplate;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;
import ulcambridge.foundations.viewer.utils.Utils;

import java.net.URI;
import java.util.Map;

public class PageImageURLResolver implements DecoratedItemFactory.ItemJSONPreProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PageImageURLResolver.class);

    private final String imageURLPropertyName;
    private final UriTemplate imageURLValueTemplate;

    public PageImageURLResolver(String imageURLPropertyName, String imageURLValueTemplate) {
        Assert.notNull(imageURLPropertyName, "imageURLPropertyName is required");
        Assert.notNull(imageURLValueTemplate, "imageURLValueTemplate is required");
        this.imageURLPropertyName = imageURLPropertyName;
        try {
            this.imageURLValueTemplate = new UriTemplate(imageURLValueTemplate);
        }
        catch (RuntimeException e) {
            throw new IllegalArgumentException(String.format("Invalid imageURLValueTemplate \"%s\": %s", imageURLValueTemplate, e));
        }
    }

    @Override
    public JSONObject preprocess(JSONObject itemJSON, String itemID) {
        return ItemDecoration.updatePages(itemID, itemJSON, this::updatePage);
    }

    private void updatePage(String itemID, JSONObject item, JSONObject page) {
        Map<String, ?> values = Utils.atomicValues(page);
        LOG.trace("Rendering template: \"{}\" with values: {} for page property: \"{}\" of item ID: \"{}\"",
            this.imageURLValueTemplate, values, this.imageURLPropertyName, itemID);
        URI result = this.imageURLValueTemplate.expand(values);
        page.put(this.imageURLPropertyName, result.toString());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("imageURLPropertyName", imageURLPropertyName)
            .add("imageURLValueTemplate", imageURLValueTemplate)
            .toString();
    }
}
