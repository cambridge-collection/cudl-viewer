package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.truth.Truth;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

public class BaseImageURLResolverTest {
    private static final String ITEM_ARRAY_KEY = "exampleObjects";

    private static final Map<String, ?> ITEM = ImmutableMap.of(
        ITEM_ARRAY_KEY, ImmutableList.of(
            ImmutableMap.builder()
                .put("foo", 123)
                .put("bar", "abc")
                .put("nested", ImmutableMap.of())
                .build(),
            ImmutableMap.of(),
            ImmutableMap.builder()
                .put("foo", 456)
                .put("bar", "def")
                .put("example", "default/value")
                .build()
        )
    );

    private static class ExampleImageURLResolver extends BaseImageURLResolver {
        public ExampleImageURLResolver(String imageURLPropertyName, String imageURLValueTemplate) {
            super(ITEM_ARRAY_KEY, imageURLPropertyName, imageURLValueTemplate);
        }

        @Override
        protected Stream<JSONObject> getTargetObjects(JSONObject itemJSON, String itemID) {
            return ItemDecoration._itemArrayObjects("EXAMPLE", itemJSON, ITEM_ARRAY_KEY);
        }
    }

    private DecoratedItemFactory.ItemJSONPreProcessor resolver;

    @BeforeEach
    private void beforeEach() {
        resolver = new ExampleImageURLResolver("example", "some/path/{foo}-{bar}.jpg");
    }

    @Test
    public void objectsWithoutReferencedKeysAreIgnored() {
        JSONObject preprocessedItem = resolver.preprocess(new JSONObject(ITEM), "EXAMPLE");
        Truth.assertThat(preprocessedItem.getJSONArray(ITEM_ARRAY_KEY).getJSONObject(1).toMap())
            .doesNotContainKey("example");
    }

    @Test
    public void objectsAreModifiedInPlace() {
        JSONObject item = new JSONObject(ITEM);
        JSONObject preprocessedItem = resolver.preprocess(item, "EXAMPLE");
        Truth.assertThat(preprocessedItem).isSameInstanceAs(item);
    }

    @ParameterizedTest
    @CsvSource({
        "0,some/path/123-abc.jpg",
        "2,some/path/456-def.jpg",
    })
    public void objectsAreUpdated(int index, String expected) {
        JSONObject preprocessedItem = resolver.preprocess(new JSONObject(ITEM), "EXAMPLE");

        assertThat(preprocessedItem.getJSONArray(ITEM_ARRAY_KEY).getJSONObject(index).getString("example")).isEqualTo(expected);

        @SuppressWarnings("unchecked")
        Map<String, ?> originalPage = ((List<ImmutableMap<String, ?>>)ITEM.get(ITEM_ARRAY_KEY)).get(index);
        MapDifference<String, Object> diff = Maps.difference(
            originalPage,
            preprocessedItem.getJSONArray(ITEM_ARRAY_KEY).getJSONObject(index).toMap());

        if(originalPage.containsKey("example")) {
            assertThat(diff.entriesDiffering()).hasSize(1);
            assertThat(diff.entriesDiffering()).containsKey("example");
            assertThat(diff.entriesOnlyOnRight()).hasSize(0);
            assertThat(diff.entriesOnlyOnLeft()).hasSize(0);
        }
        else {
            assertThat(diff.entriesOnlyOnRight()).hasSize(1);
            assertThat(diff.entriesOnlyOnRight()).containsKey("example");
            assertThat(diff.entriesOnlyOnLeft()).hasSize(0);
            assertThat(diff.entriesDiffering()).hasSize(0);
        }
    }
}
