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
        Assert.notNull(itemJSON, "itemJSON is required");

        Object pagesValue;
        try { pagesValue = itemJSON.get("pages"); }
        catch (JSONException e) {
            assert !itemJSON.has("pages");
            return Stream.of();
        }
        if(!(pagesValue instanceof JSONArray))
            throw new ValueException(String.format("Invalid Item JSON for \"%s\": \"pages\" key is not an array: %s", itemID, pagesValue.getClass()));

        JSONArray pages = (JSONArray)pagesValue;
        return StreamSupport.stream(Spliterators.spliterator(pages.iterator(), pages.length(), 0), false)
            .map(obj -> obj instanceof JSONObject ? (JSONObject)obj : null)
            .filter(Objects::nonNull);
    }

    @FunctionalInterface
    public interface ItemJSONHandler<T> {
        void handle(String itemID, JSONObject itemRoot, T component);
    }

    /**
     * Do something with each page of an Item JSON object.
     *
     * @param itemID The ID of the item
     * @param itemJSON The Item's JSON
     * @param pageHandler A function to be called for each page in the item
     * @return The same instance as itemJSON, after invoking the pageHandler on each page
     */
    public static JSONObject updatePages(String itemID, JSONObject itemJSON, ItemJSONHandler<JSONObject> pageHandler) {
        pages(itemID, itemJSON).forEach(page -> pageHandler.handle(itemID, itemJSON, page));
        return itemJSON;
    }

}
