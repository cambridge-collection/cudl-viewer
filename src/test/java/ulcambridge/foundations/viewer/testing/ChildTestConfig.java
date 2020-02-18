package ulcambridge.foundations.viewer.testing;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ulcambridge.foundations.embeddedviewer.configuration.Config;
import ulcambridge.foundations.viewer.embedded.Configs;

/**
 * This is is a Spring configuration class which extends/overrides the beans of
 * the Viewer's child DispatchServlet {@link ApplicationContext} in tests.
 *
 * @see ulcambridge.foundations.viewer.config.DispatchServletConfig
 * @see ParentTestConfig
 * @see BaseCUDLApplicationContextTest
 */
public class ChildTestConfig {
    @Bean
    public Config embeddedViewerConfig() {
        return Configs.createEmbeddedViewerConfig(
                "ABC",
                "//services.example.com/v1/metadata/json/",
                "",
                "//images.example.com/",
                "//digital.library.example.com/");
    }
}
