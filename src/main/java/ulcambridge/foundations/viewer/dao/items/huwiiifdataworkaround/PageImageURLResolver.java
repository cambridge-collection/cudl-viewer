package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.json.JSONObject;

import java.util.stream.Stream;

public class PageImageURLResolver extends BaseImageURLResolver {
    public PageImageURLResolver(String imageURLPropertyName, String imageURLValueTemplate) {
        super("pages", imageURLPropertyName, imageURLValueTemplate);
    }

    @Override
    protected Stream<JSONObject> getTargetObjects(JSONObject itemJSON, String itemID) {
        return ItemDecoration.pages(itemID, itemJSON);
    }
}
