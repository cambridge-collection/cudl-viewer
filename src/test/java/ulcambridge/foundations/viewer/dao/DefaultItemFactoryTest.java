package ulcambridge.foundations.viewer.dao;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ulcambridge.foundations.viewer.model.EssayItem;
import ulcambridge.foundations.viewer.model.Item;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultItemFactoryTest {
    private static final String ID_A = "MS-ADD-01809";
    private static final String ID_B = "MS-ADD-04004";
    private static final String ESSAY_ITEM_ID = "ES-LON-00001";

    @Mock ItemStatusOracle itemStatusOracle;

    private static JSONObject getTestItemJSON(String itemID) {
        String itemJSONText;
        try {
            URL resourceURL = DefaultItemFactoryTest.class.getResource(String.format("/cudl-data/%s.json", itemID));
            if(resourceURL == null) {
                throw new IllegalArgumentException("No test item found with ID: " + itemID);
            }
            itemJSONText = new String(Files.readAllBytes(Paths.get(resourceURL.getPath())), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return new JSONObject(itemJSONText);
        }
        catch (JSONException e) {
            throw new RuntimeException("Test item has invalid JSON: " + itemID, e);
        }
    }

    private Item createItem(String id) {
        DefaultItemFactory f = new DefaultItemFactory(itemStatusOracle);
        return f.itemFromJSON(id, getTestItemJSON(id));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void iiifEnabled(boolean isIIIFEnabled) {
        when(itemStatusOracle.isIIIFEnabled(ID_A)).thenReturn(isIIIFEnabled);
        Item item = createItem(ID_A);

        assertEquals(isIIIFEnabled, item.getIIIFEnabled());
        verify(itemStatusOracle).isIIIFEnabled(ID_A);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void taggingStatus(boolean isTaggingEnabled) {
        when(itemStatusOracle.isTaggingEnabled(ID_A)).thenReturn(isTaggingEnabled);
        Item item = createItem(ID_A);

        assertEquals(isTaggingEnabled, item.getTaggingStatus());
        verify(itemStatusOracle).isTaggingEnabled(ID_A);
    }

    @Test
    public void essayItem() {
        Item item = createItem(ESSAY_ITEM_ID);

        assertTrue(item instanceof EssayItem);
        assertEquals(ESSAY_ITEM_ID, item.getId());
        assertEquals("Artificial Horizon", item.getTitle());
    }

    @Test
    public void item() {
        Item item = createItem(ID_A);

        assertNotNull(item);
        assertFalse(item instanceof EssayItem);
        assertEquals(ID_A, item.getId());
        assertEquals("Daśāśrutaskandhasūtraṭīkā", item.getTitle());
    }
}
