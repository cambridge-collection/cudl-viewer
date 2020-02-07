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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.JSONReader;
import ulcambridge.foundations.viewer.authentication.UsersDBDao;
import ulcambridge.foundations.viewer.crowdsourcing.model.GsonFactory;
import ulcambridge.foundations.viewer.dao.*;
import ulcambridge.foundations.viewer.model.Item;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@PropertySource("classpath:cudl-global.properties")
@ComponentScan({
    "ulcambridge.foundations.viewer.admin",
    "ulcambridge.foundations.viewer.crowdsourcing",
    "ulcambridge.foundations.viewer.components",
    "ulcambridge.foundations.viewer.frontend",
    "ulcambridge.foundations.viewer.search",
})
@Import({BeanFactoryPostProcessorConfig.class, SecurityConfig.class})
@EnableScheduling
@EnableTransactionManagement
public class AppConfig {

    @Configuration
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
        public ItemsDao upstreamItemsDAO(ulcambridge.foundations.viewer.dao.ItemFactory itemFactory, @Qualifier("itemJSONLoader") JSONLoader itemJSONLoader) {
            return new DefaultItemsDao(itemFactory, itemJSONLoader);
        }

        @Autowired
        @Bean
        public ulcambridge.foundations.viewer.dao.ItemFactory itemFactory(ItemStatusOracle itemStatusOracle) {
            return new DefaultItemFactory(itemStatusOracle);
        }

        @Autowired
        @Bean
        public ItemStatusOracle itemStatusOracle(JdbcTemplate db) {
            return new DatabaseItemStatusOracle(db);
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
    @ComponentScan("ulcambridge.foundations.viewer.dao")
    @Import({CollectionFactory.class, JSONReader.class, UsersDBDao.class})
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
