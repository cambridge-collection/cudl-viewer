package ulcambridge.foundations.viewer.model;

import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.Assert;

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
        final List<String> pageLabels = ImmutableList.of("1", "2", "3");
        final List<String> pageThumbnailURLs = ImmutableList.of(
            String.format("%s-000-00001_files/8/0_0.jpg", itemId),
            "", // Missing URLs are represented by empty strings
            String.format("%s-000-00003_files/8/0_0.jpg", itemId)
        );

        JSONObject json = new JSONObject();
        JSONArray pages = new JSONArray();
        pages.put(new JSONObject().put("IIIFImageURL", String.format("%s-000-00001.jp2", itemId)));
        json.put("pages", pages);

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
