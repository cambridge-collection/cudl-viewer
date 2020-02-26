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
import org.springframework.core.annotation.Order;
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
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.JSONReader;
import ulcambridge.foundations.viewer.authentication.UsersDBDao;
import ulcambridge.foundations.viewer.crowdsourcing.model.GsonFactory;
import ulcambridge.foundations.viewer.dao.*;
import ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround.ImageURLResolution;
import ulcambridge.foundations.viewer.model.Item;

import javax.sql.DataSource;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This configuration class defines the beans used in the Viewer's root
 * {@link org.springframework.context.ApplicationContext}.
 *
 * @see DispatchServletConfig
 */
@Configuration
@PropertySource("classpath:cudl-global.properties")
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

    // Only enable scheduling when the test profile is not enabled
    @Configuration
    @Profile("!test")
    @EnableScheduling
    public class SchedulingConfig { }

    @Configuration
    @Profile("!test")
    public static class UrlsConfig {
        @Bean
        public URI rootUrl(@Value("${rootURL}") URI url) {
            return url;
        }

        @Bean
        public URI iiifImageServer(@Value("${IIIFImageServer}") URI url) {
            return url;
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
        public ItemsDao cachedItemsDAO(@Qualifier("itemCache") Cache<String, Item> itemCache, @Qualifier("upstreamItemsDAO") ItemsDao upstreamItemsDAO) {
            return new CachingItemsDAO(itemCache, upstreamItemsDAO);
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
}
