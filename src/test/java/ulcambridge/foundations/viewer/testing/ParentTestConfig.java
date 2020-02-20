package ulcambridge.foundations.viewer.testing;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ulcambridge.foundations.viewer.admin.RefreshCache;
import ulcambridge.foundations.viewer.authentication.UsersDao;
import ulcambridge.foundations.viewer.components.Captcha;
import ulcambridge.foundations.viewer.crowdsourcing.dao.CrowdsourcingDao;
import ulcambridge.foundations.viewer.dao.BookmarkDao;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.search.Search;

import java.util.Collections;

import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.Mockito.*;

/**
 * This is is a Spring configuration class which extends/overrides the beans of
 * the Viewer's root {@link ApplicationContext} in tests.
 *
 * <p>It provides NOOP/mock versions of beans so that the test environment can be
 * setup and run without requiring configuration or external services.
 *
 * <p>Tests may need to override beans defined here, use Mockito.when() to specify
 * method implementations.
 *
 * @see ulcambridge.foundations.viewer.config.AppConfig
 * @see ChildTestConfig
 * @see BaseCUDLApplicationContextTest
 */
@Profile("test")
public class ParentTestConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ParentTestConfig.class);

    private static final Answer<Object> THROWS_ON_ANY_INTERACTION = invocation -> {
        String msg = "Unexpected method invocation on unconfigured mock: " + invocation;
        LOG.error(msg);
        throw new IllegalStateException(msg);
    };

    /**
     * Create a mock that throws an IllegalStateException by default, instead of returning null.
     */
    public static <T> T mockWithoutInteractions(Class<T> cls) {
        return mock(cls, THROWS_ON_ANY_INTERACTION);
    }

    @Bean
    public ItemsDao itemsDao() {
        return mockWithoutInteractions(ItemsDao.class);
    }

    @Bean
    public Search search() {
        return mockWithoutInteractions(Search.class);
    }

    @Bean
    public CollectionsDao emptyCollectionsDao() {
        CollectionsDao collectionsDao =  mockWithoutInteractions(CollectionsDao.class);
        doReturn(Collections.emptyList()).when(collectionsDao).getCollectionIds();
        doReturn(0).when(collectionsDao).getCollectionsRowCount();
        doReturn(0).when(collectionsDao).getItemsRowCount();
        doReturn(0).when(collectionsDao).getItemsInCollectionsRowCount();
        return collectionsDao;
    }

    @Bean
    public UsersDao usersDao() {
        return mockWithoutInteractions(UsersDao.class);
    }

    @Bean
    public Captcha captcha() {
        return mockWithoutInteractions(Captcha.class);
    }

    @Bean
    public BookmarkDao bookmarkDao() {
        return mockWithoutInteractions(BookmarkDao.class);
    }

    @Bean
    public RefreshCache refreshCache() {
        return mockWithoutInteractions(RefreshCache.class);
    }

    @Bean
    public CrowdsourcingDao crowdsourcingDao() {
        return mockWithoutInteractions(CrowdsourcingDao.class);
    }
}
