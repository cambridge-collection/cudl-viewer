package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import ulcambridge.foundations.viewer.config.AppConfig;
import ulcambridge.foundations.viewer.dao.DefaultItemFactory;
import ulcambridge.foundations.viewer.dao.ItemFactory;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.net.URI;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static ulcambridge.foundations.viewer.config.AppConfig.ItemsConfig.ItemRewritingConfig.DECORATED_ITEM_FACTORY_PARENT;

@ContextHierarchy({
    @ContextConfiguration(name = "parent", classes = {ImageURLResolutionTest.Config.class})
})
public class ImageURLResolutionTest extends BaseCUDLApplicationContextTest {
    @Autowired @Qualifier("imageServerURL")
    private URI imageServerURL;

    private static final Map<String, ?> ITEM = ImmutableMap.of(
        "descriptiveMetadata", ImmutableList.of(
            ImmutableMap.builder()
                .put("ID", "DMD1")
                .put("title", ImmutableMap.of("displayForm", "Foo"))
                .put("thumbnailUrl", "MS-ADD-03419-000-00001")
                .put("thumbnailOrientation", "portrait")
                .build(),
            ImmutableMap.builder()
                .put("ID", "DMD2")
                .put("title", ImmutableMap.of("displayForm", "Bar"))
                .build(),
            ImmutableMap.builder()
                .put("ID", "DMD3")
                .put("title", ImmutableMap.of("displayForm", "Baz"))
                .put("thumbnailUrl", "MS-ADD-03419-000-00034")
                .put("thumbnailOrientation", "portrait")
                .build()
        ),
        "pages", ImmutableList.of(
            ImmutableMap.builder()
                .put("label", "1r")
                .put("physID", "PHYS-1")
                .put("sequence", 1)
                .put("IIIFImageURL", "MS-ADD-03419-000-00001")
                .put("thumbnailImageOrientation", "portrait")
                .put("imageWidth", 4355)
                .put("imageHeight", 5449)
                .build(),
            ImmutableMap.builder()
                .put("label", "1v")
                .put("physID", "PHYS-2")
                .put("sequence", 2)
                .put("IIIFImageURL", "MS-ADD-03419-000-00002")
                .put("thumbnailImageOrientation", "portrait")
                .put("imageWidth", 4365)
                .put("imageHeight", 5419)
                .build()
        )
    );

    @Import(AppConfig.ItemsConfig.ItemRewritingConfig.class)
    public static class Config {

        @Bean(name = DECORATED_ITEM_FACTORY_PARENT)
        public ItemFactory parentItemFactory() {
            return new DefaultItemFactory();
        }
    }

    private static Item itemFromJSON(String id, JSONObject json) {
        return new Item(id, "", "", ImmutableList.of(), "", "", "", "", "", ImmutableList.of(), ImmutableList.of(), false, false, json);
    }
    @Autowired
    private ItemFactory itemFactory;
    private Item item;


    @BeforeEach
    private void beforeEach() {
        JSONObject itemJSON = new JSONObject(ITEM);
        item = itemFactory.itemFromJSON("EXAMPLE", itemJSON);
    }

    @Test
    public void itemPagesHaveDZIAndIIIFImageURLsGenerated() {
        assertThat(item.getJSON().getJSONArray("pages").getJSONObject(0).toMap()).containsAtLeast(
            "displayImageURL", "content/images/MS-ADD-03419-000-00001",
            "downloadImageURL", "content/images/MS-ADD-03419-000-00001.jpg",
            "IIIFImageURL", "MS-ADD-03419-000-00001",
            "thumbnailImageURL", "MS-ADD-03419-000-00001/full/,180/0/default.jpg"
        );

        assertThat(item.getJSON().getJSONArray("pages").getJSONObject(1).toMap()).containsAtLeast(
            "displayImageURL", "content/images/MS-ADD-03419-000-00002",
            "downloadImageURL", "content/images/MS-ADD-03419-000-00002.jpg",
            "IIIFImageURL", "MS-ADD-03419-000-00002",
            "thumbnailImageURL", "MS-ADD-03419-000-00002/full/,180/0/default.jpg"
        );
    }

    @Test
    public void itemObjectReportsExpectedPageThumbnailURLs() {
        assertThat(item.getPageThumbnailURLs()).containsExactly(
            "MS-ADD-03419-000-00001/full/,180/0/default.jpg",
            "MS-ADD-03419-000-00002/full/,180/0/default.jpg"
        );
    }

    @Test
    public void descriptiveMetadataSectionsHaveThumbnailURLGenerated() {
        assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(0).toMap()).containsAtLeast(
            "thumbnailUrl", "MS-ADD-03419-000-00001"
        );

        assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(1).toMap())
            .doesNotContainKey("thumbnailUrl");

        assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(2).toMap()).containsAtLeast(
            "thumbnailUrl", "MS-ADD-03419-000-00034"
        );
    }

    @Test
    public void itemObjectsReportExpectedThumbnailURL() {
        final String expectedURL = imageServerURL.resolve("MS-ADD-03419-000-00001").toString();
        assertThat(item.getThumbnailURL()).isEqualTo(expectedURL);
        assertThat(item.getSimplifiedJSON().getString("thumbnailURL")).isEqualTo(expectedURL);
    }
}
