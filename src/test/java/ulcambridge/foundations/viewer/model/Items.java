package ulcambridge.foundations.viewer.model;

import org.json.JSONObject;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Items {
    private Items() {}

    public static Item getExampleItem(String itemId) {
        Assert.notNull(itemId, "itemId is required");

        final Person aut = new Person(
            "Test Person, 2012",
            "Test Person",
            "test authority ID",
            "test authority",
            "test value uri",
            "test type",
            "aut");

        final List<Person> authors = Arrays.asList(aut);
        final List<String> pageLabels = new ArrayList<>();
        final List<String> pageThumbnailURLs = new ArrayList<>();

        JSONObject json = new JSONObject();

        return new Item(
            itemId,
            "bookormanuscript",
            "Test Title", authors,
            "test shelfLocator",
            "test abstract",
            "test thumbnail URL",
            "test thumbnail orientation",
            "http://example.com",
            pageLabels,
            pageThumbnailURLs,
            true,
            false,
            json);

    }
}
