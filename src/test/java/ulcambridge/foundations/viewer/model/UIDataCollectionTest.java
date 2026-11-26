package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UIDataCollectionTest {

    private UIDataCollection parse(String map) throws Exception {
        String json = "{\"layout\":\"organisation\"" + (map == null ? "" : ",\"map\":" + map) + "}";
        return new ObjectMapper().readValue(json, UIDataCollection.class);
    }

    private void assertMap(String map, boolean enabled, boolean collapsed) throws Exception {
        UIDataCollection collection = parse(map);
        assertEquals(enabled, collection.isMapEnabled(), "enabled for " + map);
        assertEquals(collapsed, collection.isMapCollapsed(), "collapsed for " + map);
    }

    @Test
    public void readsTheMapSetting() throws Exception {
        assertMap(null, false, false);
        assertMap("false", false, false);
        assertMap("null", false, false);
        assertMap("true", true, false);
        assertMap("{}", true, false);
        assertMap("{\"collapsed\":false}", true, false);
        assertMap("{\"collapsed\":true}", true, true);
    }

    @Test
    public void readsTheCollectionMapSettingSeparately() throws Exception {
        UIDataCollection collection = new ObjectMapper().readValue(
            "{\"map\":true,\"collectionMap\":{\"collapsed\":true}}", UIDataCollection.class);
        assertEquals(true, collection.isCollectionMapEnabled());
        assertEquals(true, collection.isCollectionMapCollapsed());
        assertEquals(false, collection.isMapCollapsed());

        UIDataCollection searchOnly = parse("true");
        assertEquals(true, searchOnly.isMapEnabled());
        assertEquals(false, searchOnly.isCollectionMapEnabled());

        UIDataCollection pageOnly = new ObjectMapper().readValue(
            "{\"collectionMap\":true}", UIDataCollection.class);
        assertEquals(false, pageOnly.isMapEnabled());
        assertEquals(true, pageOnly.isCollectionMapEnabled());
        assertEquals(false, pageOnly.isCollectionMapCollapsed());
    }
}
