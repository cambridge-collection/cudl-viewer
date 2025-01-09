package ulcambridge.foundations.viewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySources;

/**
 * Definitions for BeanFactoryPostProocessors which don't get inherited by child
 * contexts, so must be redefined explicitly.
 */
@Configuration
public class BeanFactoryPostProcessorConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
