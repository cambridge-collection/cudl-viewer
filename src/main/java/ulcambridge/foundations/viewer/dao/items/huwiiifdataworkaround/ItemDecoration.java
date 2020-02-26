package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.exceptions.ValueException;

import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ItemDecoration {
    private ItemDecoration() { }

    /**
     * Get a stream containing the individual page objects of an Item JSON object.
     */
    public static Stream<JSONObject> pages(String itemID, JSONObject itemJSON) {
        return _itemArrayObjects(itemID, itemJSON, "pages");
    }

    /**
     * Get a stream containing the individual descriptiveMetadata objects of an Item JSON object.
     */
    public static Stream<JSONObject> descriptiveMetadata(String itemID, JSONObject itemJSON) {
        return _itemArrayObjects(itemID, itemJSON, "descriptiveMetadata");
    }

    /**
     * Get a stream containing the descriptive metadata section objects of an Item JSON object.
     */
    static Stream<JSONObject> _itemArrayObjects(String itemID, JSONObject itemJSON, String key) {
        Assert.notNull(itemJSON, "itemJSON is required");

        Object value;
        try { value = itemJSON.get(key); }
        catch (JSONException e) {
            assert !itemJSON.has(key);
            return Stream.of();
        }
        if(!(value instanceof JSONArray))
            throw new ValueException(String.format("Invalid Item JSON for \"%s\": \"%s\" key is not an array: %s", itemID, key, value.getClass()));

        JSONArray objects = (JSONArray)value;
        return StreamSupport.stream(Spliterators.spliterator(objects.iterator(), objects.length(), 0), false)
            .map(obj -> obj instanceof JSONObject ? (JSONObject)obj : null)
            .filter(Objects::nonNull);
    }
}
