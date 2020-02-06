package ulcambridge.foundations.viewer.dao;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ulcambridge.foundations.viewer.model.Item;

import static com.google.common.truth.Truth.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultItemsDAOTest {
    @Mock ItemFactory itemFactory;
    @Mock JSONLoader jsonLoader;

    private DefaultItemsDao itemsDao;

    @BeforeEach
    void beforeEach() {
        itemsDao = new DefaultItemsDao(itemFactory, jsonLoader);
    }

    @Test
    public void getItemUsesFactoryToConstructItemFromJSONProvidedByLoader() {
        Item item = mock(Item.class);
        JSONObject json = new JSONObject();
        when(jsonLoader.loadJSON("foo")).thenReturn(json);
        when(itemFactory.itemFromJSON("foo", json)).thenReturn(item);

        Item actual = itemsDao.getItem("foo");

        assertThat(actual).isSameInstanceAs(item);
        verify(jsonLoader).loadJSON("foo");
        verify(itemFactory).itemFromJSON("foo", json);
    }
}
