package ulcambridge.foundations.viewer.dao;

import com.google.common.collect.ImmutableList;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ulcambridge.foundations.viewer.model.Item;

import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DecoratedItemFactoryTest {
    @Mock(name = "parent")
    ItemFactory parent;

    @Test
    public void rejectsNullArguments() {
        assertThat(assertThrows(IllegalArgumentException.class,
            () -> new DecoratedItemFactory(null, ImmutableList.of(), ImmutableList.of())))
            .hasMessageThat().isEqualTo("parent is required");
        assertThat(assertThrows(IllegalArgumentException.class,
            () -> new DecoratedItemFactory(parent, null, ImmutableList.of())))
            .hasMessageThat().isEqualTo("preProcessors is required");
        assertThat(assertThrows(IllegalArgumentException.class,
            () -> new DecoratedItemFactory(parent, ImmutableList.of(), null)))
            .hasMessageThat().isEqualTo("postProcessors is required");
        assertThat(assertThrows(IllegalArgumentException.class,
            () -> new DecoratedItemFactory(parent, Collections.singletonList(null), ImmutableList.of())))
            .hasMessageThat().isEqualTo("preProcessors contained null");
        assertThat(assertThrows(IllegalArgumentException.class,
            () -> new DecoratedItemFactory(parent, ImmutableList.of(), Collections.singletonList(null))))
            .hasMessageThat().isEqualTo("postProcessors contained null");
    }

    @Test
    public void returnsResultOfParentUnchangedWhenNoProcessorsAreSpecified() {
        Item parentResult = mock(Item.class);
        JSONObject json = mock(JSONObject.class);
        doReturn(parentResult).when(parent).itemFromJSON("EXAMPLE", json);

        Item result = new DecoratedItemFactory(parent, ImmutableList.of(), ImmutableList.of()).itemFromJSON("EXAMPLE", json);

        assertThat(result).isSameInstanceAs(parentResult);

        verify(parent, Mockito.times(1)).itemFromJSON("EXAMPLE", json);
    }

    @Test
    public void appliesProcessorsAroundParent() {
        JSONObject itemJSON1 = mock(JSONObject.class, "Item JSON 1");
        JSONObject itemJSON2 = mock(JSONObject.class, "Item JSON 2");
        JSONObject itemJSON3 = mock(JSONObject.class, "Item JSON 3");

        DecoratedItemFactory.ItemJSONPreProcessor pre1 = mock(DecoratedItemFactory.ItemJSONPreProcessor.class, "Pre 1");
        DecoratedItemFactory.ItemJSONPreProcessor pre2 = mock(DecoratedItemFactory.ItemJSONPreProcessor.class, "Pre 2");
        doReturn(itemJSON2).when(pre1).preprocess(any(), any());
        doReturn(itemJSON3).when(pre2).preprocess(any(), any());

        Item item1 = mock(Item.class, "Item 1");
        Item item2 = mock(Item.class, "Item 2");
        Item item3 = mock(Item.class, "Item 3");

        DecoratedItemFactory.ItemPostProcessor post1 = mock(DecoratedItemFactory.ItemPostProcessor.class, "Post 1");
        DecoratedItemFactory.ItemPostProcessor post2 = mock(DecoratedItemFactory.ItemPostProcessor.class, "Post 2");
        doReturn(item2).when(post1).postProcess(any(), any());
        doReturn(item3).when(post2).postProcess(any(), any());

        doReturn(item1).when(parent).itemFromJSON(any(), any());

        Item result = new DecoratedItemFactory(parent, ImmutableList.of(pre1, pre2), ImmutableList.of(post1, post2))
            .itemFromJSON("EXAMPLE", itemJSON1);

        assertThat(result).isSameInstanceAs(item3);

        verify(pre1).preprocess(itemJSON1, "EXAMPLE");
        verify(pre2).preprocess(itemJSON2, "EXAMPLE");
        verify(parent).itemFromJSON("EXAMPLE", itemJSON3);
        verify(post1).postProcess(item1, "EXAMPLE");
        verify(post2).postProcess(item2, "EXAMPLE");
    }
}
