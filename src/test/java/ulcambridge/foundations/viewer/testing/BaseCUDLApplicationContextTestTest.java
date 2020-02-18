package ulcambridge.foundations.viewer.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.web.context.WebApplicationContext;
import ulcambridge.foundations.viewer.SiteViewController;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Items;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTestTest.ParentOverrideConfiguration;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTestTest.ChildOverrideConfiguration;

/**
 * This is an example of a test using the full dependency-injected application
 * via @CUDLApplicationContextTest.
 */
@ContextHierarchy({
        @ContextConfiguration(name = "parent", classes = {ParentOverrideConfiguration.class}),
        @ContextConfiguration(name = "child", classes = {ChildOverrideConfiguration.class})
})
public class BaseCUDLApplicationContextTestTest extends BaseCUDLApplicationContextTest {
    public static class ParentOverrideConfiguration {
        @Bean
        public ItemsDao itemsDao() {
            ItemsDao mockItemsDao = mock(ItemsDao.class);
            when(mockItemsDao.getItem("ABCD")).thenReturn(Items.getExampleItem("ABCD"));
            return mockItemsDao;
        }

        @Bean
        public String foo() {
            return "FOO!";
        }
    }

    public static class ChildOverrideConfiguration {
        @Bean
        public String bar() {
            return "BAR!";
        }
    }

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void twoContextLevelsExist() {
        assertThat(wac.getParent()).isNotNull();
        assertThat(wac.getParent().getParent()).isNull();
    }

    @Test
    public void fooIsInParentContext() {
        assertThat(wac.getBean("foo")).isEqualTo("FOO!");
        assertThat(wac.getParent().getBean("foo")).isEqualTo("FOO!");
    }

    @Test
    public void barIsOnlyInChildContext() {
        assertThat(wac.getBean("bar")).isEqualTo("BAR!");
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> wac.getParent().getBean("bar"));
    }

    @Test
    public void beansFromParentContextAreAvailableFromChild() {
        assertThat(wac.getBean(CollectionsDao.class)).isNotNull();
        assertThat(wac.getParent().getBean(CollectionsDao.class)).isNotNull();
    }

    @Test
    public void controllersAreDefinedInChildContext() {
        assertThat(wac.getBean(SiteViewController.class)).isNotNull();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> wac.getParent().getBean(SiteViewController.class));
    }

    @Test
    public void beanDefinitionsCanBeOverridden() {
        // ItemsDao is overridden in the ParentOverrideConfiguration config class above.
        ItemsDao itemsDao = wac.getBean(ItemsDao.class);
        assertThat(itemsDao.getItem("ABCD").getId()).isEqualTo("ABCD");
        assertThat(itemsDao.getItem("FOO")).isNull();
    }
}
