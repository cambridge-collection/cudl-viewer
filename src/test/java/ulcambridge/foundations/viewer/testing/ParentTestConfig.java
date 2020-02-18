package ulcambridge.foundations.viewer.testing;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Bean
    public ItemsDao itemsDao() {
        return mock(ItemsDao.class);
    }

    @Bean
    public Search search() {
        return mock(Search.class);
    }

    @Bean
    public CollectionsDao emptyCollectionsDao() {
        CollectionsDao collectionsDao =  mock(CollectionsDao.class);
        when(collectionsDao.getCollectionIds()).thenReturn(Collections.emptyList());
        return collectionsDao;
    }

    @Bean
    public UsersDao usersDao() {
        return mock(UsersDao.class);
    }

    @Bean
    public Captcha captcha() {
        return mock(Captcha.class);
    }

    @Bean
    public BookmarkDao bookmarkDao() {
        return mock(BookmarkDao.class);
    }

    @Bean
    public RefreshCache refreshCache() {
        return mock(RefreshCache.class);
    }

    @Bean
    public CrowdsourcingDao crowdsourcingDao() {
        return mock(CrowdsourcingDao.class);
    }
}
