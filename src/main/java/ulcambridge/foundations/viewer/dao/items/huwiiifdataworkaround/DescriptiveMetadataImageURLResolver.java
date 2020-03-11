package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.json.JSONObject;

import java.util.stream.Stream;

public class DescriptiveMetadataImageURLResolver extends BaseImageURLResolver {
    public DescriptiveMetadataImageURLResolver(String imageURLPropertyName, String imageURLValueTemplate) {
        super("descriptiveMetadata", imageURLPropertyName, imageURLValueTemplate);
    }

    @Override
    protected Stream<JSONObject> getTargetObjects(JSONObject itemJSON, String itemID) {
        return ItemDecoration.descriptiveMetadata(itemID, itemJSON);
    }
}
