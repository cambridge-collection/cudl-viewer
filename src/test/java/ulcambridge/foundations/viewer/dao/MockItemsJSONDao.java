package ulcambridge.foundations.viewer.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;

public class MockItemsJSONDao implements ItemsDao {

    @Override
    public Item getItem(String itemId) {
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
            "MS-ADD-04004",
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
