package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import ulcambridge.foundations.viewer.config.AppConfig;
import ulcambridge.foundations.viewer.dao.ItemFactory;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static ulcambridge.foundations.viewer.config.AppConfig.ItemsConfig.ItemRewritingConfig.DECORATED_ITEM_FACTORY_PARENT;

@ContextHierarchy({
    @ContextConfiguration(name = "parent", classes = {ImageURLResolutionTest.Config.class})
})
public class ImageURLResolutionTest extends BaseCUDLApplicationContextTest {
    private static final Map<String, ?> ITEM = ImmutableMap.of(
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
            ItemFactory itemFactory = Mockito.mock(ItemFactory.class);
            doAnswer(answer(ImageURLResolutionTest::itemFromJSON)).when(itemFactory).itemFromJSON(any(), any());
            return itemFactory;
        }
    }

    private static Item itemFromJSON(String id, JSONObject json) {
        return new Item(id, "", "", ImmutableList.of(), "", "", "", "", "", ImmutableList.of(), ImmutableList.of(), false, false, json);
    }

    @Autowired
    private ItemFactory itemFactory;

    @Test
    public void testItemPagesHaveDZIAndIIIFImageURLsGenerated() {
        JSONObject itemJSON = new JSONObject(ITEM);
        Item result = itemFactory.itemFromJSON("EXAMPLE", itemJSON);

        assertThat(result.getJSON().getJSONArray("pages").getJSONObject(0).toMap()).containsAtLeast(
            "displayImageURL", "content/images/MS-ADD-03419-000-00001.dzi",
            "downloadImageURL", "content/images/MS-ADD-03419-000-00001.jpg",
            "IIIFImageURL", "MS-ADD-03419-000-00001.jp2",
            "thumbnailImageURL", "content/images/MS-ADD-03419-000-00001_files/8/0_0.jpg"
        );

        assertThat(result.getJSON().getJSONArray("pages").getJSONObject(1).toMap()).containsAtLeast(
            "displayImageURL", "content/images/MS-ADD-03419-000-00002.dzi",
            "downloadImageURL", "content/images/MS-ADD-03419-000-00002.jpg",
            "IIIFImageURL", "MS-ADD-03419-000-00002.jp2",
            "thumbnailImageURL", "content/images/MS-ADD-03419-000-00002_files/8/0_0.jpg"
        );
    }
}
