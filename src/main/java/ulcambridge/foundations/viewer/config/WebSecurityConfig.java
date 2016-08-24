package ulcambridge.foundations.viewer.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.cam.lib.spring.security.raven.AuthenticatedRavenTokenCreator;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationEntryPoint;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationFilter;
import uk.ac.cam.lib.spring.security.raven.RavenAuthenticationProvider;
import uk.ac.cam.lib.spring.security.raven.RavenRequestCreator;
import uk.ac.cam.lib.spring.security.raven.hooks.DefaultRavenRequestCreator;
import uk.ac.cam.lib.spring.security.raven.hooks.DefaultRavenRequestCreator.RequestParam;
import uk.ac.cam.lib.spring.security.raven.hooks.UserDetailsRavenTokenCreator;
import uk.ac.cam.ucs.webauth.WebauthValidator;
import ulcambridge.foundations.viewer.authentication.CudlSavedRequestAwareAuthenticationSuccessHandler;
import ulcambridge.foundations.viewer.authentication.CudlUserDetailsRavenTokenCreator;
import ulcambridge.foundations.viewer.authentication.DeferredEntryPointFilter;
import ulcambridge.foundations.viewer.authentication.DeferredEntryPointFilter.EntryPointSelector;
import ulcambridge.foundations.viewer.authentication.QueryStringRequestMatcher;
import ulcambridge.foundations.viewer.authentication.RavenAuthCancellationFailureHandler;
import ulcambridge.foundations.viewer.authentication.UsersDao;
import ulcambridge.foundations.viewer.authentication.ViewerUserDetailsService;
import ulcambridge.foundations.viewer.authentication.oauth2.CudlProviders;
import ulcambridge.foundations.viewer.authentication.oauth2.FacebookProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.GoogleProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.LinkedinProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.Oauth2AuthenticationFilter;
import ulcambridge.foundations.viewer.utils.RequestMatcherFilterFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Optional;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Import(WebSecurityConfig.Oauth2AuthConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
                               implements BeanFactoryAware {

    public static final String LOGIN_PATH = "/auth/login";

    public static final String RAVEN_RETURN_LOGIN_PATH = "/auth/raven/login";

    private final DataSource dataSource;
    private Optional<BeanFactory> beanFactory = Optional.empty();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = Optional.of(beanFactory);
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory.get();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        Assert.notNull(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(this.dataSource);
    }

    /**
     * Used by Spring Security to get a UserDetails object for a username
     * string.
     */
    @Bean
    @Autowired
    public UserDetailsService userDetailsService(UsersDao usersDao) {
        return new ViewerUserDetailsService(usersDao);
    }

    @Bean(name = "ravenKeystore")
    @Autowired
    public KeyStore ravenKeys(ResourceLoader resourceLoader)
        throws KeyStoreException, CertificateException,
        NoSuchAlgorithmException, IOException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        InputStream ravenKey2 = resourceLoader.getResource(
            "classpath:ulcambridge/foundations/viewer/raven-pubkey2.crt")
            .getInputStream();

        Certificate ravenKey2Cert = CertificateFactory.getInstance("X.509")
            .generateCertificate(ravenKey2);

        keyStore.setCertificateEntry("webauth-pubkey2", ravenKey2Cert);
        return keyStore;
    }

    @Bean
    @Autowired
    public WebauthValidator ravenValidator(
        @Qualifier("ravenKeystore") KeyStore ravenKeys) {

        return new WebauthValidator(ravenKeys);
    }

    @Bean
    @Autowired
    public UserDetailsRavenTokenCreator ravenTokenCreator(
        UserDetailsService userDetailsService) {

        return new CudlUserDetailsRavenTokenCreator(userDetailsService);
    }

    @Bean
    @Autowired
    public RavenAuthenticationProvider ravenAuthenticationProvider(
        WebauthValidator ravenValidator,
        AuthenticatedRavenTokenCreator tokenCreator) {

        return new RavenAuthenticationProvider(ravenValidator, tokenCreator);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(
        RequestCache requestCache) {

        return new CudlSavedRequestAwareAuthenticationSuccessHandler(
            requestCache);
    }

    @Bean(name = "ravenAuthenticationFilter")
    @Autowired
    public RavenAuthenticationFilter ravenAuthenticationFilter(
        RavenRequestCreator requestCreator, RequestCache requestCache,
        RavenAuthCancellationFailureHandler failureHandler,
        AuthenticationSuccessHandler successHandler,
        AuthenticationManager authenticationManager) {

        RavenAuthenticationFilter f = new RavenAuthenticationFilter(
            authenticationManager, requestCreator, requestCache);

        f.setAuthenticationFailureHandler(failureHandler);
        f.setAuthenticationSuccessHandler(successHandler);

        return f;
    }

    @Bean
    public RavenRequestCreator ravenRequestCreator(
        @Value("${rootURL}") String siteUrl) {

        String returnUrl = UriComponentsBuilder
            .fromUriString(siteUrl)
            .replacePath(RAVEN_RETURN_LOGIN_PATH)
            .toUriString();

        return DefaultRavenRequestCreator.builder(returnUrl)
            .set(RequestParam.desc,
                 "Cambridge Digital Library")
            .build();
    }

    @Bean(name = "authenticationEntryPoint")
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(LOGIN_PATH);
    }

    @Bean
    @Autowired
    public RavenAuthenticationEntryPoint ravenAuthEntryPoint(
        RavenRequestCreator requestCreator, RequestCache requestCache) {
        return new RavenAuthenticationEntryPoint(requestCreator, requestCache);
    }


    @Bean(name = "ravenEntryPointSelector")
    @Autowired
    EntryPointSelector ravenEntryPointSelector(
        RavenAuthenticationEntryPoint ravenEntryPoint) {

        RequestMatcher queryMatcher = QueryStringRequestMatcher
            .forKeyWithValue("type", "raven");

        RequestMatcher pathMatcher = new AntPathRequestMatcher(
            LOGIN_PATH, "POST");

        RequestMatcher m = new AndRequestMatcher(pathMatcher, queryMatcher);

        return req -> m.matches(req) ? Optional.of(ravenEntryPoint)
                                     : Optional.empty();
    }

    @Bean
    @Autowired
    public Iterable<EntryPointSelector> entryPointSelectors(
        @Qualifier("ravenEntryPointSelector") EntryPointSelector raven,
        @Qualifier("linkedinEntryPointSelector") EntryPointSelector linkedin,
        @Qualifier("googleEntryPointSelector") EntryPointSelector google,
        @Qualifier("facebookEntryPointSelector") EntryPointSelector facebook) {

        return Arrays.asList(raven, linkedin, google, facebook);
    }

    @Bean(name = "deferredEntryPointFilter")
    @Autowired
    public DeferredEntryPointFilter deferredEntryPointFilter(
        Iterable<EntryPointSelector> entryPointSelectors) {

        return new DeferredEntryPointFilter(entryPointSelectors);
    }

    @Bean
    public RavenAuthCancellationFailureHandler ravenAuthCancellationFailureHandler() {

        return RavenAuthCancellationFailureHandler.redirectingTo(LOGIN_PATH);
    }

    /**
     * Nullify the RequestCacheAwareFilter. I'm not really sure why it exists
     * given that {@link SavedRequestAwareAuthenticationSuccessHandler} exists.
     *
     * <p>If it's active, it removes saved requests from the cache on every
     * request, meaning the aformentioned success handler doesn't find a saved
     * request to use when redirecting the user post-logon.
     */
    @Bean
    public BeanPostProcessor requestCacheFilterPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(
                Object bean, String beanName) throws BeansException {

                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(
                Object bean, String beanName) throws BeansException {

                if(bean instanceof RequestCacheAwareFilter &&
                    !(bean instanceof NoopRequestCacheAwareFilter)) {
                    return new NoopRequestCacheAwareFilter();
                }
                return bean;
            }
        };
    }

    private static final class NoopRequestCacheAwareFilter extends RequestCacheAwareFilter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                             FilterChain chain)
            throws IOException, ServletException {

            // Don't remove a saved request from the cache.
            chain.doFilter(request, response);
        }
    }

    static final Filter restrictFilterToAntPattern(String pattern, Filter filter) {
        return new RequestMatcherFilterFilter(
            new AntPathRequestMatcher(pattern), filter);
    }

    @Configuration
    public static class Oauth2AuthConfig {
        public static final URI LINKEDIN_PROFILE_URL = URI.create(
            "https://api.linkedin.com/v1/people/~:(id,email-address)?format=json");
        public static final URI GOOGLE_PROFILE_URL = URI.create(
            "https://www.googleapis.com/plus/v1/people/me/openIdConnect");
        public static final URI FACEBOOK_PROFILE_URL = URI.create(
            "https://graph.facebook.com/me/");

        public static final String TYPE_LINKEDIN = "linkedin";
        public static final String TYPE_GOOGLE = "google";
        public static final String TYPE_FACEBOOK = "facebook";

        public static final String OAUTH2_LOGIN_PATH = "/auth/oauth2/{type}";

        public static RequestMatcher authFilterUrlMatcher(String type) {
            return new AntPathRequestMatcher(getAuthFilterPath(type));
        }

        public static String getAuthFilterPath(String type) {
            return UriComponentsBuilder.fromPath(OAUTH2_LOGIN_PATH)
                .buildAndExpand(type).toUriString();
        }

        @Bean(name = "oauthAuthenticationProvider")
        @Autowired
        public AuthenticationProvider oauth2AuthenticationProvider(
            UserDetailsService userDetailsService, UsersDao usersDao) {

            return CudlProviders.cudlOauthAuthenticationProvider(
                    userDetailsService, usersDao);
        }

        private static <T extends AbstractAuthenticationProcessingFilter>
        T setSuccessHandler(T filter,
                            AuthenticationSuccessHandler successHandler) {

            filter.setAuthenticationSuccessHandler(successHandler);
            return filter;
        }

        @Bean(name = "facebookAuthFilter")
        @Autowired
        public Oauth2AuthenticationFilter<FacebookProfile> facebookOauth2Filter(
            @Qualifier("facebookOauth")
                OAuth2RestTemplate restTemplate,
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler successHandler) {

            return setSuccessHandler(
                new Oauth2AuthenticationFilter<>(
                    authenticationManager, restTemplate, FACEBOOK_PROFILE_URL,
                    FacebookProfile.class, authFilterUrlMatcher(TYPE_FACEBOOK)),
                successHandler);
        }

        @Bean(name = "googleAuthFilter")
        @Autowired
        public Oauth2AuthenticationFilter<GoogleProfile> googleOauth2Filter(
            @Qualifier("googleOauth")
                OAuth2RestTemplate restTemplate,
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler successHandler) {

            return setSuccessHandler(
                new Oauth2AuthenticationFilter<>(
                    authenticationManager, restTemplate, GOOGLE_PROFILE_URL,
                    GoogleProfile.class, authFilterUrlMatcher(TYPE_GOOGLE)),
                successHandler);
        }

        @Bean(name = "linkedinAuthFilter")
        @Autowired
        public Oauth2AuthenticationFilter<LinkedinProfile> linkedinOauth2Filter(
            @Qualifier("linkedinOauth")
            OAuth2RestTemplate restTemplate,
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler successHandler) {

            return setSuccessHandler(
                new Oauth2AuthenticationFilter<>(
                    authenticationManager, restTemplate, LINKEDIN_PROFILE_URL,
                    LinkedinProfile.class, authFilterUrlMatcher(TYPE_LINKEDIN)),
                successHandler);
        }

        public EntryPointSelector entryPointSelector(String type) {
            RequestMatcher matcher = new AndRequestMatcher(
                new AntPathRequestMatcher(LOGIN_PATH),
                QueryStringRequestMatcher.forKeyWithValue("type", type)
            );

            AuthenticationEntryPoint ep = new LoginUrlAuthenticationEntryPoint(
                getAuthFilterPath(type));

            return req -> matcher.matches(req) ? Optional.of(ep)
                                               : Optional.empty();
        }

        @Bean(name = "linkedinEntryPointSelector")
        public EntryPointSelector linkedinEntryPointSelector() {
            return entryPointSelector(TYPE_LINKEDIN);
        }

        @Bean(name = "googleEntryPointSelector")
        public EntryPointSelector googleEntryPointSelector() {
            return entryPointSelector(TYPE_GOOGLE);
        }

        @Bean(name = "facebookEntryPointSelector")
        public EntryPointSelector facebookEntryPointSelector() {
            return entryPointSelector(TYPE_FACEBOOK);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint entryPoint = getBeanFactory().getBean(
            "authenticationEntryPoint", AuthenticationEntryPoint.class);

        RequestCache requestCache = getBeanFactory().getBean(RequestCache.class);

        RavenAuthenticationFilter ravenAuthFilter =
            getBeanFactory().getBean(RavenAuthenticationFilter.class);

        DeferredEntryPointFilter deferredEntryPointFilter =
            getBeanFactory().getBean(DeferredEntryPointFilter.class);

        http
            .addFilterBefore(
                getBeanFactory().getBean(OAuth2ClientContextFilter.class),
                SecurityContextPersistenceFilter.class)

            // Add our raven auth filter amongst the other auth filters. It has
            // to be after the SECURITY_CONTEXT_FILTER otherwise the auth result
            // doesn't get persisted.
            .addFilterAfter(
                restrictFilterToAntPattern(RAVEN_RETURN_LOGIN_PATH,
                                           ravenAuthFilter),
                AbstractPreAuthenticatedProcessingFilter.class)

            // Add our deferred entry point filter after the
            // ExceptionTranslationFilter as that's the spring filter that
            // invokes the built-in entry points.
            .addFilterAfter(
                restrictFilterToAntPattern(LOGIN_PATH,
                                           deferredEntryPointFilter),
                ExceptionTranslationFilter.class)

            .addFilterAfter(
                getBeanFactory().getBean("linkedinAuthFilter",
                                    Oauth2AuthenticationFilter.class),
                AbstractPreAuthenticatedProcessingFilter.class)

            .addFilterAfter(
                getBeanFactory().getBean("googleAuthFilter",
                                    Oauth2AuthenticationFilter.class),
                AbstractPreAuthenticatedProcessingFilter.class)

            .addFilterAfter(
                getBeanFactory().getBean("facebookAuthFilter",
                                    Oauth2AuthenticationFilter.class),
                AbstractPreAuthenticatedProcessingFilter.class)

            // Add support for authenticating raven auth tokens
            .authenticationProvider(
                getBeanFactory().getBean(RavenAuthenticationProvider.class))
            .authenticationProvider(
                getBeanFactory().getBean("oauthAuthenticationProvider",
                    AuthenticationProvider.class))
            .requestCache()
                .requestCache(requestCache)
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .and()
            .logout()
                // FIXME: Should use .logoutUrl() which POST-only to prevent CSRF logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
                // FIXME: This:
                .logoutSuccessUrl("/auth/login?error=Successfully%20logged%20out &amp;access=/mylibrary/")
                .deleteCookies("JSESSIONID")
                .and()
            .authorizeRequests()
                .antMatchers("/admin/**", "/editor/**").hasRole("ADMIN")
                .antMatchers("/mylibrary/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .and()
            .headers()
                .addHeaderWriter(
                    new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }
}
