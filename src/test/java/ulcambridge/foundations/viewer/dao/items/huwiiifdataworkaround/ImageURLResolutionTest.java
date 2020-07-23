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
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import ulcambridge.foundations.viewer.config.AppConfig;
import ulcambridge.foundations.viewer.dao.DefaultItemFactory;
import ulcambridge.foundations.viewer.dao.ItemFactory;
import ulcambridge.foundations.viewer.dao.ItemStatusOracle;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.net.URI;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static ulcambridge.foundations.viewer.config.AppConfig.ItemsConfig.ItemRewritingConfig.DECORATED_ITEM_FACTORY_PARENT;

public class ImageURLResolutionTest {
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
        @Bean
        public ItemStatusOracle itemStatusOracle() {
            ItemStatusOracle itemStatusOracle = mock(ItemStatusOracle.class);
            doReturn(false).when(itemStatusOracle).isTaggingEnabled(any());
            doReturn(false).when(itemStatusOracle).isIIIFEnabled(any());
            return itemStatusOracle;
        }

        @Bean(name = DECORATED_ITEM_FACTORY_PARENT)
        public ItemFactory parentItemFactory(ItemStatusOracle itemStatusOracle) {
            return new DefaultItemFactory(itemStatusOracle);
        }
    }

    private static Item itemFromJSON(String id, JSONObject json) {
        return new Item(id, "", "", ImmutableList.of(), "", "", "", "", "", ImmutableList.of(), ImmutableList.of(), false, false, json);
    }

    public static class AbstractImageURLResolutionTest extends BaseCUDLApplicationContextTest {
        @Autowired
        @Qualifier("imageServerURL")
        protected URI imageServerURL;

        @Autowired
        protected ItemFactory itemFactory;
        protected Item item;


        @BeforeEach
        protected void beforeEach() {
            JSONObject itemJSON = new JSONObject(ITEM);
            item = itemFactory.itemFromJSON("EXAMPLE", itemJSON);
        }
    }

    @ContextHierarchy({
        @ContextConfiguration(name = "parent", classes = {ImageURLResolutionTest.Config.class})
    })
    @TestPropertySource(properties = {
        "images.server.type=iiif"
    })
    public static class IiifImageURLResolutionTest extends AbstractImageURLResolutionTest {
        @Test
        public void itemPagesHaveIIIFImageURLsGenerated() {
            assertThat(item.getJSON().getJSONArray("pages").getJSONObject(0).toMap()).containsAtLeast(
                "thumbnailImageURL", "MS-ADD-03419-000-00001.jp2/full/!178,178/0/default.jpg",
                "IIIFImageURL", "MS-ADD-03419-000-00001.jp2"
            );

            assertThat(item.getJSON().getJSONArray("pages").getJSONObject(1).toMap()).containsAtLeast(
                "IIIFImageURL", "MS-ADD-03419-000-00002.jp2"
            );
        }

        @Test
        public void itemObjectReportsExpectedPageThumbnailURLs() {
            assertThat(item.getPageThumbnailURLs()).containsExactly(
                "MS-ADD-03419-000-00001.jp2/full/!178,178/0/default.jpg",
                "MS-ADD-03419-000-00002.jp2/full/!178,178/0/default.jpg"
            );
        }

        @Test
        public void descriptiveMetadataSectionsHaveThumbnailURLGenerated() {
            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(0).toMap()).containsAtLeast(
                "thumbnailUrl", "MS-ADD-03419-000-00001.jp2/full/!178,178/0/default.jpg"
            );

            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(1).toMap())
                .doesNotContainKey("thumbnailUrl");

            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(2).toMap()).containsAtLeast(
                "thumbnailUrl", "MS-ADD-03419-000-00034.jp2/full/!178,178/0/default.jpg"
            );
        }

        @Test
        public void itemObjectsReportExpectedThumbnailURL() {
            final String expectedURL = imageServerURL.resolve("MS-ADD-03419-000-00001.jp2/full/!178,178/0/default.jpg").toString();
            assertThat(item.getThumbnailURL()).isEqualTo(expectedURL);
            assertThat(item.getSimplifiedJSON().getString("thumbnailURL")).isEqualTo(expectedURL);
        }

    }

    @ContextHierarchy({
        @ContextConfiguration(name = "parent", classes = {ImageURLResolutionTest.Config.class})
    })
    @TestPropertySource(properties = {
        "images.server.type=dzi-and-iiif"
    })
    public static class DziAndIiifImageURLResolutionTest extends AbstractImageURLResolutionTest {
        @Test
        public void itemPagesHaveDZIAndIIIFImageURLsGenerated() {
            assertThat(item.getJSON().getJSONArray("pages").getJSONObject(0).toMap()).containsAtLeast(
                "displayImageURL", "content/images/MS-ADD-03419-000-00001.dzi",
                "downloadImageURL", "content/images/MS-ADD-03419-000-00001.jpg",
                "IIIFImageURL", "MS-ADD-03419-000-00001.jp2",
                "thumbnailImageURL", "content/images/MS-ADD-03419-000-00001_files/8/0_0.jpg"
            );

            assertThat(item.getJSON().getJSONArray("pages").getJSONObject(1).toMap()).containsAtLeast(
                "displayImageURL", "content/images/MS-ADD-03419-000-00002.dzi",
                "downloadImageURL", "content/images/MS-ADD-03419-000-00002.jpg",
                "IIIFImageURL", "MS-ADD-03419-000-00002.jp2",
                "thumbnailImageURL", "content/images/MS-ADD-03419-000-00002_files/8/0_0.jpg"
            );
        }

        @Test
        public void itemObjectReportsExpectedPageThumbnailURLs() {
            assertThat(item.getPageThumbnailURLs()).containsExactly(
                "content/images/MS-ADD-03419-000-00001_files/8/0_0.jpg",
                "content/images/MS-ADD-03419-000-00002_files/8/0_0.jpg"
            );
        }

        @Test
        public void descriptiveMetadataSectionsHaveThumbnailURLGenerated() {
            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(0).toMap()).containsAtLeast(
                "thumbnailUrl", "content/images/MS-ADD-03419-000-00001_files/8/0_0.jpg"
            );

            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(1).toMap())
                .doesNotContainKey("thumbnailUrl");

            assertThat(item.getJSON().getJSONArray("descriptiveMetadata").getJSONObject(2).toMap()).containsAtLeast(
                "thumbnailUrl", "content/images/MS-ADD-03419-000-00034_files/8/0_0.jpg"
            );
        }

        @Test
        public void itemObjectsReportExpectedThumbnailURL() {
            final String expectedURL = imageServerURL.resolve("content/images/MS-ADD-03419-000-00001_files/8/0_0.jpg").toString();
            assertThat(item.getThumbnailURL()).isEqualTo(expectedURL);
            assertThat(item.getSimplifiedJSON().getString("thumbnailURL")).isEqualTo(expectedURL);
        }
    }
}
