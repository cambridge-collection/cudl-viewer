package ulcambridge.foundations.viewer.testing;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.admin.RefreshCache;
import ulcambridge.foundations.viewer.authentication.UsersDao;
import ulcambridge.foundations.viewer.components.Captcha;
import ulcambridge.foundations.viewer.components.EmailHelper;
import ulcambridge.foundations.viewer.crowdsourcing.dao.CrowdsourcingDao;
import ulcambridge.foundations.viewer.dao.BookmarkDao;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.search.Search;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
    public ItemsDao itemsDao(ApplicationContext context) {
        return registerResettableMock(context, ItemsDao.class);
    }

    @Bean
    public Search search(ApplicationContext context) {
        return registerResettableMock(context, Search.class);
    }

    @Bean
    public CollectionsDao emptyCollectionsDao(ApplicationContext context) {
        return registerResettableMock(context, CollectionsDao.class, collectionsDao -> {
            doReturn(Collections.emptyList()).when(collectionsDao).getCollectionIds();
            doReturn(0).when(collectionsDao).getCollectionsRowCount();
            doReturn(0).when(collectionsDao).getItemsRowCount();
            doReturn(0).when(collectionsDao).getItemsInCollectionsRowCount();
        });
    }

    @Bean
    public UsersDao usersDao(ApplicationContext context) {
        return registerResettableMock(context, UsersDao.class);
    }

    @Bean
    public Captcha captcha(ApplicationContext context) {
        return registerResettableMock(context, Captcha.class, captcha -> {
            doReturn(Captcha.RESPONSE_PARAM).when(captcha).getResponseParam();
            doReturn("<captcha/>").when(captcha).getHtml();
        });
    }

    @Bean
    public BookmarkDao bookmarkDao(ApplicationContext context) {
        return registerResettableMock(context, BookmarkDao.class);
    }

    @Bean
    public RefreshCache refreshCache(ApplicationContext context) {
        return registerResettableMock(context, RefreshCache.class);
    }

    @Bean
    public CrowdsourcingDao crowdsourcingDao(ApplicationContext context) {
        return registerResettableMock(context, CrowdsourcingDao.class);
    }

    @Bean
    public EmailHelper emailHelper(ApplicationContext context) {
        return registerResettableMock(context, EmailHelper.class);
    }

    public interface BeanInitialiser<T> {
        T initialise();
        void reInitialise(BeanFactory beanFactory);
        void registerInitialiser(BeanDefinitionRegistry registry);
    }

    public static class SingletonMockMockBeanInitialiser<T> implements BeanInitialiser<T> {
        private final Class<T> mockClass;
        private final Consumer<T> initialiser;
        private final Function<Class<T>, T> mockFactory;

        public SingletonMockMockBeanInitialiser(Class<T> mockClass, Consumer<T> initialiser, Function<Class<T>, T> mockFactory) {
            Assert.notNull(mockClass, "mockClass is required");
            Assert.notNull(initialiser, "initialiser is required");
            Assert.notNull(mockFactory, "mockFactory is required");

            this.mockClass = mockClass;
            this.initialiser = initialiser;
            this.mockFactory = mockFactory;
        }

        public T initialise() {
            T mock = this.mockFactory.apply(this.mockClass);
            this.initialiser.accept(mock);
            return mock;
        }

        protected BeanDefinition getBeanDefinition() {
            GenericBeanDefinition def = new GenericBeanDefinition();
            def.setBeanClass(this.getClass());
            def.setInstanceSupplier(() -> this);
            return def;
        }

        protected String getBeanName() {
            return String.format("%s[%s]", this.getClass().getName(), this.mockClass.getName());
        }

        @Override
        public void registerInitialiser(BeanDefinitionRegistry registry) {
            registry.registerBeanDefinition(this.getBeanName(), this.getBeanDefinition());
        }

        @Override
        public void reInitialise(BeanFactory beanFactory) {
            T mock = this.getMockBean(beanFactory);
            Mockito.reset(mock);
            this.initialiser.accept(mock);
        }

        protected T getMockBean(BeanFactory beanFactory) {
            return beanFactory.getBean(this.mockClass);
        }
    }

    public static <T> T registerResettableMock(ApplicationContext applicationContext, Class<T> type) {
        return registerResettableMock(applicationContext, type, obj -> {});
    }
    public static <T> T registerResettableMock(ApplicationContext applicationContext, Class<T> type, Consumer<T> initialiser) {
        return registerResettableMock(applicationContext, type, initialiser, ParentTestConfig::mockWithoutInteractions);
    }
    public static <T> T registerResettableMock(ApplicationContext applicationContext, Class<T> type, Consumer<T> initialiser, Function<Class<T>, T> mockFactory) {
        BeanInitialiser<T> beanInitialiser = new SingletonMockMockBeanInitialiser<>(type, initialiser, mockFactory);
        if(!(applicationContext instanceof BeanDefinitionRegistry)) {
            throw new IllegalArgumentException("received ApplicationContext is not a BeanDefinitionRegistry, cannot register mocks");
        }
        beanInitialiser.registerInitialiser((BeanDefinitionRegistry)applicationContext);
        return beanInitialiser.initialise();
    }

    interface BeanResetter {
        void resetBeans();
    }

    @Bean
    public BeanResetter mockBeanResetter() {
        return new MockBeanResetter();
    }

    public static final class MockBeanResetter implements ApplicationContextAware, BeanResetter {
        private ListableBeanFactory listableBeanFactory;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.listableBeanFactory = applicationContext;
        }

        @Override
        public void resetBeans() {
            ListableBeanFactory lbf = this.listableBeanFactory;
            if(lbf == null) {
                throw new IllegalStateException("resetMocks() called before setApplicationContext()");
            }
            MockBeanResetter.resetMocks(lbf);
        }

        private static void resetMocks(ListableBeanFactory beanFactory) {
            beanFactory.getBeansOfType(SingletonMockMockBeanInitialiser.class)
                .forEach((name, smbi) -> smbi.reInitialise(beanFactory));
        }
    }
}
