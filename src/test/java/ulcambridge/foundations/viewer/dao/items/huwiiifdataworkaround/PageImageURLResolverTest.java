package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class PageImageURLResolverTest {

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

    @ParameterizedTest
    @CsvSource({
        "0,example1,foo/{sequence}.jpg,foo/1.jpg",
        "1,example1,foo/{sequence}.jpg,foo/2.jpg",
        "0,displayImageURL,/content/images/{IIIFImageURL}.dzi,/content/images/MS-ADD-03419-000-00001.dzi",
        "1,displayImageURL,/content/images/{IIIFImageURL}.dzi,/content/images/MS-ADD-03419-000-00002.dzi",
        "0,IIIFImageURL,{IIIFImageURL}.jp2,MS-ADD-03419-000-00001.jp2",
        "1,IIIFImageURL,{IIIFImageURL}.jp2,MS-ADD-03419-000-00002.jp2",
    })
    public void pagesAreUpdated(int page, String name, String template, String expected) {
        PageImageURLResolver resolver = new PageImageURLResolver(name, template);
        JSONObject input = new JSONObject(ITEM);
        JSONObject result = resolver.preprocess(input, "EXAMPLE");

        assertThat(result).isSameInstanceAs(input);
        assertThat(result.getJSONArray("pages").getJSONObject(page).getString(name)).isEqualTo(expected);

        @SuppressWarnings("unchecked")
        Map<String, ?> originalPage = ((List<ImmutableMap<String, ?>>)ITEM.get("pages")).get(page);
        MapDifference<String, Object> diff = Maps.difference(
            originalPage,
            result.getJSONArray("pages").getJSONObject(page).toMap());

        if(originalPage.containsKey(name)) {
            assertThat(diff.entriesDiffering()).hasSize(1);
            assertThat(diff.entriesDiffering()).containsKey(name);
        }
        else {
            assertThat(diff.entriesOnlyOnRight()).hasSize(1);
            assertThat(diff.entriesOnlyOnRight()).containsKey(name);
        }
    }
}
