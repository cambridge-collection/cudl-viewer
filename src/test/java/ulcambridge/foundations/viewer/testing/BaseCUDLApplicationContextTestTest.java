package ulcambridge.foundations.viewer.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.web.context.WebApplicationContext;
import ulcambridge.foundations.viewer.SiteViewController;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.model.Items;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

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

    class ExampleEntity {
        public String foo() {
            return "bar";
        }
    }

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

        @Bean
        public ExampleEntity exampleEntity(ApplicationContext context) {
            return ParentTestConfig.registerResettableMock(context, ExampleEntity.class,
                exampleEntity -> doReturn("lol").when(exampleEntity).foo());
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

    @Autowired
    private ExampleEntity exampleEntity;

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

    @Test
    public void mockBeansAreResetBetweenTests_1() {
        // If mocks are not reset then this test or its pair would fail, depending on execution order
        assertThat(exampleEntity.foo()).isEqualTo("lol");
        verify(exampleEntity, times(1)).foo();
    }

    @Test
    public void mockBeansAreResetBetweenTests_2() {
        // If mocks are not reset then this test or its pair would fail, depending on execution order
        assertThat(exampleEntity.foo()).isEqualTo("lol");
        verify(exampleEntity, times(1)).foo();
    }
}
