package ulcambridge.foundations.viewer.testing;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ulcambridge.foundations.viewer.config.AppConfig;
import ulcambridge.foundations.viewer.config.DispatchServletConfig;

/**
 * This base class can be extended to create tests which use beans from the
 * Viewer's {@link org.springframework.context.ApplicationContext}.
 *
 * <p>For example, controllers for the purpose of testing controller methods.
 *
 * @see BaseCUDLApplicationContextTestTest
 *      BaseCUDLApplicationContextTestTest is an example of a test class using beans
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextHierarchy({
    @ContextConfiguration(name = "parent", classes = {
        AppConfig.class,
        ParentTestConfig.class
    }),
    @ContextConfiguration(name = "child", classes = {
        DispatchServletConfig.class,
        BaseCUDLApplicationContextTest.TestSpecificBeanConfig.class,
        ChildTestConfig.class
    })
})
@ActiveProfiles("test")
public abstract class BaseCUDLApplicationContextTest {
    public static class TestSpecificBeanConfig {
        @Bean
        @Lazy
        public MockMvc mockMvc(WebApplicationContext wac) {
            return MockMvcBuilders.webAppContextSetup(wac).build();
        }
    }
}
