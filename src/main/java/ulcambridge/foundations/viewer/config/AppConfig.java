package ulcambridge.foundations.viewer.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestTemplate;
import ulcambridge.foundations.viewer.crowdsourcing.model.GsonFactory;
import ulcambridge.foundations.viewer.dao.*;
import ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround.ImageURLResolution;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.pdf.FullDocumentPdf;
import ulcambridge.foundations.viewer.pdf.SinglePagePdf;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ulcambridge.foundations.viewer.utils.Utils.ensureURLHasPath;

/**
 * This configuration class defines the beans used in the Viewer's root
 * {@link org.springframework.context.ApplicationContext}.
 *
 * @see DispatchServletConfig
 */
@Configuration
@ComponentScan(
    basePackages = "ulcambridge.foundations.viewer",
    excludeFilters = {
        // Controllers are excluded from the root context level as they should
        // be registered in the child context of the DispatchServlet.
        @ComponentScan.Filter(classes = {Controller.class, ControllerAdvice.class}),
        // Config classes are manually included to keep the two contexts separate.
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ulcambridge\\.foundations\\.viewer\\.config\\..*")
    }
)
@Import({BeanFactoryPostProcessorConfig.class, SecurityConfig.class})
@EnableTransactionManagement
public class AppConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    // Only enable scheduling when the test profile is not enabled
    /**
     * Configuration that is not applied when unit testing.
     */
    @Configuration
    @Profile("!test")
    @PropertySource("classpath:cudl-global.properties")
    @PropertySource("classpath:application.properties")
    @EnableScheduling
    public class RuntimeConfig { }

    @Configuration
    public static class UrlsConfig {
        @Bean
        public URI rootUrl(@Value("${rootURL}") URI url) {
            return ensureURLHasPath(url);
        }

        @Bean
        public URI imageServerURL(@Value("${imageServer}") URI url) {
            return ensureURLHasPath(url);
        }

        @Bean
        public URI iiifImageServer(@Value("${IIIFImageServer}") URI url) {
            return ensureURLHasPath(url);
        }

        @Bean
        public URI xtfURL(@Value("${xtfURL}") URI url) {
            return ensureURLHasPath(url);
        }
    }

    @Configuration
    @Profile("!test")
    @Import(ItemsConfig.ItemRewritingConfig.class)
    public static class ItemsConfig {
        @Bean
        @Qualifier("itemCache")
        public Cache<String, Item> itemCache() {
            return Caffeine.newBuilder().maximumSize(500).build();
        }

        @Autowired
        @Bean
        @Primary
        public ItemsDao cachedItemsDAO(@Qualifier("itemCache") Cache<String, Item> itemCache,
                                       @Qualifier("upstreamItemsDAO") ItemsDao upstreamItemsDAO,
                                       @Value("${caching.enabled:true}") String cacheEnabled) {
            if ("true".equalsIgnoreCase(cacheEnabled)) {
                LOG.info("using ITEM CACHE");
                return new CachingItemsDAO(itemCache, upstreamItemsDAO);
            } else {
                LOG.info("not using ITEM CACHE");
                return upstreamItemsDAO;
            }

        }

        @Autowired
        @Bean
        public ItemsDao upstreamItemsDAO(ItemFactory itemFactory, @Qualifier("itemJSONLoader") JSONLoader itemJSONLoader) {
            return new DefaultItemsDao(itemFactory, itemJSONLoader);
        }

        @Bean(name = {ItemRewritingConfig.DECORATED_ITEM_FACTORY_PARENT})
        public DefaultItemFactory defaultItemFactory(ItemStatusOracle itemStatusOracle) {
            return new DefaultItemFactory(itemStatusOracle);
        }

        @Import(ImageURLResolution.class)
        public static class ItemRewritingConfig {
            public static final String DECORATED_ITEM_FACTORY_PARENT = "ulcambridge.foundations.viewer.dao.DecoratedItemFactory#parent";

            @Bean
            @Primary
            public DecoratedItemFactory rewritingItemFactory(@Qualifier(DECORATED_ITEM_FACTORY_PARENT) ItemFactory baseFactory,
                                                             List<DecoratedItemFactory.ItemJSONPreProcessor> preProcessors,
                                                             List<DecoratedItemFactory.ItemPostProcessor> postProcessors) {
                return new DecoratedItemFactory(baseFactory, preProcessors, postProcessors);
            }
        }

        @Autowired
        @Bean
        public ItemStatusOracle itemStatusOracle(JdbcTemplate db) {
            return new DatabaseItemStatusOracle(db, false, false);
        }

        @Autowired
        @Bean
        @Qualifier("itemJSONLoader")
        public JSONLoader itemJSONLoader(@Qualifier("itemJSON") Path itemJSONDirectory) {
            return new FilesystemDirectoryJSONLoader(itemJSONDirectory);
        }

        @Autowired
        @Bean
        @Qualifier("itemJSON")
        public Path itemJSONDirectory(@Value("${itemJSONDirectory}") String itemJSONDirectory) {
            Path dir = Paths.get(itemJSONDirectory);
            if (!Files.isDirectory(dir)) {
                throw new IllegalArgumentException("itemJSONDirectory is not a directory: " + itemJSONDirectory);
            }
            return dir;
        }

    }

    @Configuration
    @Profile("!test")
    public static class DatabaseConfig {
        @Bean
        public DataSource dataSource(
                @Value("${jdbc.driver}") String driverClassName,
                @Value("${jdbc.url}") String url,
                @Value("${jdbc.user}") String username,
                @Value("${jdbc.password}") String password) {

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(driverClassName);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setValidationQuery("SELECT 1");
            ds.setTestOnBorrow(true);

            return ds;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        @Autowired
        public PlatformTransactionManager transactionManager(
                DataSource dataSource) {

            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Bean
    public Gson gson() {
        return GsonFactory.create();
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        Logger log = LoggerFactory.getLogger(RestTemplate.class);
        RestTemplate restTemplate = new RestTemplate();

        if (log.isTraceEnabled()) {
            restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
            restTemplate.getInterceptors().add((request, body, execution) -> {
                log.trace("{} {}: {}", request.getMethod(), request.getURI(), new String(body, UTF_8));
                ClientHttpResponse response = execution.execute(request, body);
                log.trace("Response: {}", StreamUtils.copyToString(response.getBody(), UTF_8));
                return response;
            });
        }

        return restTemplate;
    }


    @Bean
    public SinglePagePdf singlePagePdf(@Value("${IIIFImageServer}") String IIIFImageServer,
                                       @Value("${rootURL}") String baseURL,
                                       @Value("${pdf.header.text}") String headerText,
                                       @Value("${pdf.style.highlight-color.rgb}") int[] pdfColour,
                                       @Value("${pdf.fonts.zip-urls}") String[] zipFonts,
                                       @Value("${pdf.fonts.default}") String defaultFont,
                                       @Value("${pdf.cache.path}") String cachePath) throws IOException {

        return new SinglePagePdf(IIIFImageServer, baseURL, headerText, pdfColour, zipFonts, defaultFont, cachePath);
    }

    @Bean
    public FullDocumentPdf fullDocumentPdf(@Value("${IIIFImageServer}") String IIIFImageServer,
                                           @Value("${rootURL}") String baseURL,
                                           @Value("${pdf.header.text}") String headerText,
                                           @Value("${pdf.style.highlight-color.rgb}") int[] pdfColour,
                                           @Value("${pdf.fonts.zip-urls}") String[] zipFonts,
                                           @Value("${pdf.fonts.default}") String defaultFont,
                                           @Value("${pdf.cache.path}") String cachePath) throws IOException {

        return new FullDocumentPdf(IIIFImageServer, baseURL, headerText, pdfColour, zipFonts, defaultFont, cachePath);
    }
}
