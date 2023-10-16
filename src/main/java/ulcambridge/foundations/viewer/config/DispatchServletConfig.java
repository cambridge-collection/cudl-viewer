package ulcambridge.foundations.viewer.config;

import com.google.common.base.Charsets;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ulcambridge.foundations.embeddedviewer.configuration.Config;
import ulcambridge.foundations.embeddedviewer.configuration.EmbeddedViewerConfiguringResourceTransformer;
import ulcambridge.foundations.viewer.embedded.Configs;

import java.util.List;
import java.util.Properties;

/**
 * This configuration class defines the beans used in the Viewer's child
 * {@link org.springframework.context.ApplicationContext} which contains the
 * Spring Web MVC DispatchServlet, with which the various Controller classes are
 * registered.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
    basePackages = {"ulcambridge.foundations.viewer"},
    includeFilters = {@Filter({Controller.class, ControllerAdvice.class})})
@Import({BeanFactoryPostProcessorConfig.class})
public class DispatchServletConfig
    implements WebMvcConfigurer, BeanFactoryAware {

    public static final String EMBEDDED_VIEWER_PATTERN = "/embed/**";

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Register the UTF-8 message converter
        converters.add(0, this.beanFactory.getBean(
            StringHttpMessageConverter.class));
    }

    /**
     * A message converter which encodes strings using UTF-8. By default Spring
     * uses latin-1 for some reason.
     */
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter converter =
            new StringHttpMessageConverter(Charsets.UTF_8);

        // Don't write a massive Accept-Charset header with every charset
        // imaginable.
        converter.setWriteAcceptCharset(false);

        return converter;
    }

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        resolver.setContentType(new MimeType("text", "html",
                                Charsets.UTF_8).toString());
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        resolver.setExposedContextBeanNames("globalproperties");

        return resolver;
    }

    @Bean(name = "multipartResolver")
    public static CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver c = new CommonsMultipartResolver();
        c.setDefaultEncoding("utf-8");
        c.setMaxUploadSize(1024 * 1024 * 20);
        c.setMaxInMemorySize(1024 * 1024);
        return c;
    }

    @Configuration
    public static class StaticResourcesConfigurer implements WebMvcConfigurer {

        private final Environment env;
        private final Config embeddedViewerConfig;

        public StaticResourcesConfigurer(Environment env, Config embeddedViewerConfig) {
            Assert.notNull(env, "env is required");
            Assert.notNull(embeddedViewerConfig, "embeddedViewerConfig is required");
            this.env = env;
            this.embeddedViewerConfig = embeddedViewerConfig;
        }

        private String resolve(String text) {
            return env.resolveRequiredPlaceholders(text);
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/models/**")
                .addResourceLocations("/models/");

            registry.addResourceHandler(
                    resolve("${cudl-viewer-content.images.url}/**"))
                .addResourceLocations(
                    resolve("file:${cudl-viewer-content.images.path}/"));

            registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/");

            registry.addResourceHandler("/mirador-ui/**")
            .addResourceLocations("/mirador-ui/");

            registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("/favicon.ico");

            registry.addResourceHandler("/themeui/**")
                .addResourceLocations(resolve("file:${dataUIThemeResources}/"));

            addViewerUiAssets(registry);
            addEmbeddedViewerAssets(registry);
        }

        private void addViewerUiAssets(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/ui/**")
                .addResourceLocations(
                    "classpath:ulcambridge/foundations/viewer/viewer-ui/assets/")
                .setCachePeriod(60 * 60 * 24 * 365)  // 1 year
                .resourceChain(true)
                    .addResolver(new EncodedResourceResolver());
        }

        private void addEmbeddedViewerAssets(ResourceHandlerRegistry registry) {
            // Cache headers are not set here but in urlrewrite.xml. This is
            // because the viewer.html needs different cache-control values to
            // the rest of the assets.
            registry.addResourceHandler(EMBEDDED_VIEWER_PATTERN)
                .addResourceLocations(
                    "classpath:ulcambridge/foundations/embeddedviewer/assets/")
                .resourceChain(true)
                    .addResolver(new EncodedResourceResolver())
                    .addTransformer(
                        new EmbeddedViewerConfiguringResourceTransformer(
                            embeddedViewerConfig));
        }
    }

    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        mappings.setProperty("EmptyResultDataAccessException", "jsp/errors/404");
        r.setExceptionMappings(mappings);  // None by default

        Properties statusCodes = new Properties();
        statusCodes.setProperty("jsp/errors/404", "404");
        r.setStatusCodes(statusCodes);

        r.setDefaultErrorView("jsp/errors/500");
        r.setExceptionAttribute("exception");

        r.setWarnLogCategory("ulcambridge.foundations.viewer.error");
        return r;
    }

    @Configuration
    @Profile("!test")
    public static class RuntimeConfigurableBeans {
        @Bean
        public Config embeddedViewerConfig(
                @Value("${cudl.viewer.analytics.embedded.gaid:}") String gaTrackingId,
                @Value("${services://services.cudl.lib.cam.ac.uk}/v1/metadata/json/")
                        String metadataUrlPrefix,
                @Value("${metadataUrlSuffix:}") String metadataUrlSuffix,
                @Value("${imageServer://image01.cudl.lib.cam.ac.uk/}")
                        String dziUrlPrefix,
                @Value("${rootURL://cudl.lib.cam.ac.uk}") String metadataUrlHost) {

            return Configs.createEmbeddedViewerConfig(
                    gaTrackingId, metadataUrlPrefix, metadataUrlSuffix, dziUrlPrefix,
                    metadataUrlHost);
        }
    }
}
