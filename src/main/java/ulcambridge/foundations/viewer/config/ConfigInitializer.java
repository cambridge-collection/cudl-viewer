package ulcambridge.foundations.viewer.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class ConfigInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {

        // rule over all the others:
        try {
            ctx.getEnvironment().getPropertySources().
                addFirst(new ResourcePropertySource("file:/etc/cudl-viewer/cudl-global.properties"));


        // rule over application.properties but not argument or systemproperties etc
        ctx.getEnvironment().getPropertySources().
            addBefore("applicationConfig: [classpath:/application.properties]",
                new ResourcePropertySource("classpath:cudl-global.properties"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
