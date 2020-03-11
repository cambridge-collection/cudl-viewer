package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ulcambridge.foundations.viewer.exceptions.ValueException;

import static com.google.common.truth.Truth.assertThat;

public class ItemDecorationTest {

    @Test
    public void pagesThrowsIfItemJSONPagesIsNotAnArray() {
        JSONObject invalidItem = new JSONObject(ImmutableMap.of("pages", 42));
        assertThat(Assertions.assertThrows(ValueException.class, () -> ItemDecoration.pages("EXAMPLE", invalidItem)))
            .hasMessageThat().isEqualTo("Invalid Item JSON for \"EXAMPLE\": \"pages\" key is not an array: class java.lang.Integer");
    }

    @Test
    public void pagesReturnsEmptyStreamIfItemHasNoPagesEntry() {
        assertThat(ItemDecoration.pages("EXAMPLE", new JSONObject()).count()).isEqualTo(0);
    }

    @Test
    public void pagesReturnsStreamExcludingNonObjectPages() {
        JSONObject item = new JSONObject(ImmutableMap.of("pages", ImmutableList.of(
            ImmutableMap.of("sequence", 1),
            "not an object for some reason",
            ImmutableMap.of("sequence", 3)
        )));

        assertThat(ItemDecoration.pages("EXAMPLE", item).map(o -> o.getInt("sequence")).toArray())
            .asList().containsExactly(1, 3);
    }
}
