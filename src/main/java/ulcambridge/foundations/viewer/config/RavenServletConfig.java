package ulcambridge.foundations.viewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ServletWrappingController;
import ulcambridge.foundations.viewer.authentication.RavenLoginServlet;

import java.util.Properties;

/**
 * Configuration for the (hopefully soon to be removed) raven servlet, which
 * performs Raven authentication.
 */
@Configuration
@Import(BeanFactoryPostProcessorConfig.class)
public class RavenServletConfig extends WebMvcConfigurerAdapter {

    private static final String RAVEN_CONTROLLER = "ravenLoginController";

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping s = new SimpleUrlHandlerMapping();

        Properties mappings = new Properties();
        mappings.setProperty("", RAVEN_CONTROLLER);
        mappings.setProperty("/callback", RAVEN_CONTROLLER);

        s.setMappings(mappings);
        return s;
    }

    @Bean(name = RAVEN_CONTROLLER)
    public ServletWrappingController ravenLoginController(
        @Value("${raven.raven-url}") String ravenUrl,
        @Value("${raven.intercept-login-path}") String loginPath,
        @Value("${raven.description}") String desc,
        @Value("${raven.keystore}") String keystore,
        @Value("${raven.keystore-password}") String keystorePassword,
        @Value("${raven.key-prefix}") String keyPrefix) {

        ServletWrappingController controller = new ServletWrappingController();
        controller.setServletClass(RavenLoginServlet.class);

        Properties p = new Properties();
        p.setProperty("gs.spri.raven.raven-url", ravenUrl);
        p.setProperty("gs.spri.raven.intercept-login-path", loginPath);
        p.setProperty("gs.spri.raven.description", desc);
        p.setProperty("gs.spri.raven.keystore", keystore);
        p.setProperty("gs.spri.raven.keystore-password", keystorePassword);
        p.setProperty("gs.spri.raven.key-prefix", keyPrefix);

        controller.setInitParameters(p);

        return controller;
    }
}
