package ulcambridge.foundations.viewer.dao;

import org.json.JSONObject;
import ulcambridge.foundations.viewer.model.Item;

public interface ItemFactory {
    Item itemFromJSON(String itemId, JSONObject itemJson);
}
