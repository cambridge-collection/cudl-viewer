package ulcambridge.foundations.viewer.dao;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ulcambridge.foundations.viewer.model.Item;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CachingItemsDAOTest {
    @Mock private ItemsDao upstreamDao;
//
    private Map<String, Item> items = ImmutableMap.of(
        "a", mock(Item.class, "a"),
        "b", mock(Item.class, "b"),
        "c", mock(Item.class, "c"),
        "d", mock(Item.class, "d")
    );

    private CachingItemsDAO cachedDAO;

    @BeforeEach
    public void setUp() {
        when(upstreamDao.getItem(any())).then(answer((String id) -> items.get(id)));

        cachedDAO = new CachingItemsDAO(Caffeine.newBuilder().maximumSize(2).build(), upstreamDao);
    }

    @Test
    public void upstreamDAOIsUsedToPopulateCache() {
        Item actual = cachedDAO.getItem("a");
        assertThat(actual).isSameInstanceAs(items.get("a"));
        verify(upstreamDao).getItem("a");
        verify(upstreamDao, atMostOnce()).getItem(any());
    }

    @Test
    public void upstreamDAOIsNotQueriedIfItemIsCached() {
        Item a1 = cachedDAO.getItem("a");
        Item a2 = cachedDAO.getItem("a");
        assertThat(a1).isSameInstanceAs(items.get("a"));
        assertThat(a2).isSameInstanceAs(items.get("a"));
        verify(upstreamDao).getItem("a");
        verify(upstreamDao, atMostOnce()).getItem(any());
    }

    @Test
    public void itemsAreEvicted() {
        Item a1 = cachedDAO.getItem("a");
        assertThat(a1).isSameInstanceAs(items.get("a"));

        for(int i = 0; i < 100; ++i) {
            Item b = cachedDAO.getItem("b");
            Item c = cachedDAO.getItem("c");

            assertThat(b).isSameInstanceAs(items.get("b"));
            assertThat(c).isSameInstanceAs(items.get("c"));
            verify(upstreamDao, atMostOnce()).getItem("b");
            verify(upstreamDao, atMostOnce()).getItem("c");
        }

        // a should have been evicted now due to b and c being more popular
        Item a2 = cachedDAO.getItem("a");
        assertThat(a2).isSameInstanceAs(items.get("a"));
        verify(upstreamDao, times(2)).getItem("a");
    }
}
