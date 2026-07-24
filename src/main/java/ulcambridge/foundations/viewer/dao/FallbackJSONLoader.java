package ulcambridge.foundations.viewer.dao;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * A JSONLoader that tries a primary loader first and falls back to a secondary
 * loader when the primary signals the resource is not found. Used to serve
 * unreleased item JSON from a separate directory without modifying the main
 * item JSON directory.
 */
public class FallbackJSONLoader implements JSONLoader {

    private final JSONLoader primary;
    private final JSONLoader fallback;

    public FallbackJSONLoader(JSONLoader primary, JSONLoader fallback) {
        this.primary = primary;
        this.fallback = fallback;
    }

    @Override
    public JSONObject loadJSON(String id) {
        try {
            return primary.loadJSON(id);
        } catch (EmptyResultDataAccessException e) {
            // Item not found in primary directory; try the fallback (unreleased) directory.
            return fallback.loadJSON(id);
        }
    }
}
