package ulcambridge.foundations.viewer.config;

import com.google.common.collect.ImmutableList;
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
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
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
import uk.ac.cam.lib.spring.security.raven.hooks.DefaultRavenRequestCreator.PerRequestParamProducer;
import uk.ac.cam.lib.spring.security.raven.hooks.DefaultRavenRequestCreator.RequestParam;
import uk.ac.cam.lib.spring.security.raven.hooks.UserDetailsRavenTokenCreator;
import uk.ac.cam.ucs.webauth.WebauthValidator;
import ulcambridge.foundations.viewer.authentication.CudlSavedRequestAwareAuthenticationSuccessHandler;
import ulcambridge.foundations.viewer.authentication.DeferredEntryPointFilter;
import ulcambridge.foundations.viewer.authentication.DeferredEntryPointFilter.EntryPointSelector;
import ulcambridge.foundations.viewer.authentication.EntryPointRequestFilters;
import ulcambridge.foundations.viewer.authentication.FragmentAwareRequestCache;
import ulcambridge.foundations.viewer.authentication.HeaderValueHttpServletRequestFragmentStorageStrategy;
import ulcambridge.foundations.viewer.authentication.HttpServletRequestFragmentStorageStrategy;
import ulcambridge.foundations.viewer.authentication.Obfuscation;
import ulcambridge.foundations.viewer.authentication.Urls;
import ulcambridge.foundations.viewer.authentication.Urls.UrlCodecStrategy;
import ulcambridge.foundations.viewer.authentication.QueryStringRequestMatcher;
import ulcambridge.foundations.viewer.authentication.RedirectingLogoutSuccessHandler;
import ulcambridge.foundations.viewer.authentication.RequestFilterEntryPointWrapper;
import ulcambridge.foundations.viewer.authentication.UrlQueryParamAuthenticationEntryPoint;
import ulcambridge.foundations.viewer.authentication.UsersDao;
import ulcambridge.foundations.viewer.authentication.ViewerUserDetailsService;
import ulcambridge.foundations.viewer.authentication.oauth2.CudlProviders;
import ulcambridge.foundations.viewer.authentication.oauth2.FacebookProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.GoogleProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.LinkedinProfile;
import ulcambridge.foundations.viewer.authentication.oauth2.Oauth2AuthenticationFilter;
import ulcambridge.foundations.viewer.authentication.raven.AutoRegisteringRavenTokenCreatorWrapper;
import ulcambridge.foundations.viewer.authentication.raven.CrsidObfuscatingRavenTokenCreator;
import ulcambridge.foundations.viewer.authentication.raven.RavenAuthCancellationFailureHandler;
import ulcambridge.foundations.viewer.utils.RequestMatcherFilterFilter;
import ulcambridge.foundations.viewer.utils.SecureRequestProxyHeaderFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
import java.util.function.Function;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Import(WebSecurityConfig.Oauth2AuthConfig.class)
public class WebSecurityConfig {

    public static final String LOGIN_PATH = "/auth/login";

    public static final String RAVEN_RETURN_LOGIN_PATH = "/auth/raven/login";

    /**
     * Create an AuthenticationManager bean without the use of Spring Sec's
     * ridiculous config building spaghetti. Required as the manager has to
     * exist outside the {@link GeneralSecurityConfig} and I don't feel like
     * using 5 layers of delegated/lazily instantiated facades to achieve that.
     */
    @Bean
    @Autowired
    public AuthenticationManager authenticationManager(
        RavenAuthenticationProvider ravenAuthProvider,
        @Qualifier("oauthAuthenticationProvider")
            AuthenticationProvider oauthAuthenticationProvider) {

        return new ProviderManager(ImmutableList.of(
            ravenAuthProvider, oauthAuthenticationProvider));
    }

    @Bean
    public HttpServletRequestFragmentStorageStrategy fragmentStorageStrategy() {
        return new HeaderValueHttpServletRequestFragmentStorageStrategy();
    }

    @Bean
    @Autowired
    public RequestCache requestCache(
        HttpServletRequestFragmentStorageStrategy fragmentStorageStrategy) {

        return new FragmentAwareRequestCache(
            new HttpSessionRequestCache(), fragmentStorageStrategy);
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
    public AuthenticatedRavenTokenCreator ravenTokenCreator(
        UserDetailsService userDetailsService, UsersDao usersDao) {

        Function<String, String> usernameEncoder =
            s -> Obfuscation.obfuscateUsername("raven", s);

        AuthenticatedRavenTokenCreator tokenCreator =
            new UserDetailsRavenTokenCreator(userDetailsService);

        // CUDL obfuscates CRSIDs using sha256
        tokenCreator = new CrsidObfuscatingRavenTokenCreator(
            tokenCreator, usernameEncoder);

        // Auto register users with the DAO when they log in for the first time
        tokenCreator = new AutoRegisteringRavenTokenCreatorWrapper(
            tokenCreator, usersDao, usernameEncoder, Obfuscation::obfuscate);

        return tokenCreator;
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

    @Bean()
    @Autowired
    public PerRequestParamProducer ravenReturnUrlProducer(
        @Value("${rootURL}") String siteUrl) {

        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(siteUrl)
            .replacePath(RAVEN_RETURN_LOGIN_PATH);

        String returnUrlHttp = b.scheme("http").toUriString();
        String returnUrlHttps = b.scheme("https").toUriString();

        return (p, r) -> r.isSecure() ? returnUrlHttps : returnUrlHttp;
    }

    @Bean
    @Autowired
    public RavenRequestCreator ravenRequestCreator(
        PerRequestParamProducer ravenReturnUrlProducer) {

        return DefaultRavenRequestCreator.builder(ravenReturnUrlProducer)
            .withValue(RequestParam.desc, "Cambridge Digital Library")
            .build();
    }

    @Bean(name = "authenticationEntryPoint")
    @Autowired
    public AuthenticationEntryPoint authenticationEntryPoint(
        @Qualifier("homepageUrl") URI homepage, RequestCache requestCache,
        UrlCodecStrategy urlCodec) {

        URI loginPage = UriComponentsBuilder.fromUri(homepage)
            .replacePath(LOGIN_PATH)
            .build().encode().toUri();

        return new UrlQueryParamAuthenticationEntryPoint(
            loginPage, requestCache, urlCodec);
    }

    @Bean
    public URI homepageUrl(@Value("${rootURL}") String siteUrl) {
        URI homepage = URI.create(siteUrl);

        Assert.isTrue("".equals(homepage.getRawPath()));

        return homepage;
    }

    @Bean
    @Autowired
    public UrlCodecStrategy queryParamUrlCodec(
        @Qualifier("homepageUrl") URI homepage) {

        return Urls.sameSiteCodecStrategy(
            Urls.relativisingUrlCodec(homepage), homepage, false);
    }

    @Bean
    public String nextPageQueryParam() {
        return UrlQueryParamAuthenticationEntryPoint.DEFAULT_PARAM_NAME;
    }

    @Bean
    @Autowired
    public Function<AuthenticationEntryPoint, AuthenticationEntryPoint>
    entryPointWrapper(
        RequestCache requestCache,
        @Qualifier("nextPageQueryParam") String nextPageQueryParam,
        @Qualifier("homepageUrl") URI homepage,
        UrlCodecStrategy urlCodec,
        HttpServletRequestFragmentStorageStrategy fragmentStorageStrategy) {

        RequestFilterEntryPointWrapper.RequestFilter saveRequestFromQueryParam =
            EntryPointRequestFilters.saveRequestFromQueryParamUrl(
                requestCache, nextPageQueryParam, homepage, urlCodec,
                Optional.of(fragmentStorageStrategy));

        return new RequestFilterEntryPointWrapper.Builder()
            .withFilter(saveRequestFromQueryParam)
            ::build;

    }

    @Bean
    @Autowired
    public AuthenticationEntryPoint ravenAuthEntryPoint(
        RavenRequestCreator requestCreator,
        @Qualifier("entryPointWrapper")
        Function<AuthenticationEntryPoint, AuthenticationEntryPoint>
            entryPointWrapper) {

        return entryPointWrapper.apply(
            new RavenAuthenticationEntryPoint(requestCreator));
    }

    @Bean(name = "ravenEntryPointSelector")
    @Autowired
    EntryPointSelector ravenEntryPointSelector(
        @Qualifier("ravenAuthEntryPoint")
            AuthenticationEntryPoint ravenEntryPoint) {

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
                "https://graph.facebook.com/me?fields=id,email");            

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

        @Bean
        public Function<String, EntryPointSelector> entryPointSelector(
            @Qualifier("entryPointWrapper")
                Function<AuthenticationEntryPoint, AuthenticationEntryPoint>
                entryPointWrapper) {

            return type -> {
                RequestMatcher matcher = new AndRequestMatcher(
                    new AntPathRequestMatcher(LOGIN_PATH),
                    QueryStringRequestMatcher.forKeyWithValue("type", type)
                );

                AuthenticationEntryPoint ep = entryPointWrapper.apply(
                    new LoginUrlAuthenticationEntryPoint(
                        getAuthFilterPath(type)));

                return req -> matcher.matches(req) ? Optional.of(ep)
                                                   : Optional.empty();
            };
        }

        @Bean(name = "linkedinEntryPointSelector")
        public EntryPointSelector linkedinEntryPointSelector(
            Function<String, EntryPointSelector> entryPointSelector) {

            return entryPointSelector.apply(TYPE_LINKEDIN);
        }

        @Bean(name = "googleEntryPointSelector")
        public EntryPointSelector googleEntryPointSelector(
            Function<String, EntryPointSelector> entryPointSelector) {

            return entryPointSelector.apply(TYPE_GOOGLE);
        }

        @Bean(name = "facebookEntryPointSelector")
        public EntryPointSelector facebookEntryPointSelector(
            Function<String, EntryPointSelector> entryPointSelector) {

            return entryPointSelector.apply(TYPE_FACEBOOK);
        }
    }

    @Bean
    @Autowired
    public LogoutSuccessHandler logoutSuccessHandler(
        @Qualifier("homepageUrl") URI homepageUrl) {

        return new RedirectingLogoutSuccessHandler(homepageUrl);
    }

    // Issue: this gets called after security entry points.
    // FIXME: register w/ spring security below
    // Remove extra filter proxy definition from web.xml
    @Bean(name = "secureRequestProxyHeaderFilter")
    public SecureRequestProxyHeaderFilter secureRequestProxyHeaderFilter() {
        return new SecureRequestProxyHeaderFilter();
    }

    private static abstract class AbstractWebSecurityConfig
        extends WebSecurityConfigurerAdapter
        implements BeanFactoryAware {

        private Optional<AuthenticationManager> authenticationManager =
            Optional.empty();

        private Optional<BeanFactory> beanFactory = Optional.empty();

        @Autowired
        public void setAuthenticationManager(AuthenticationManager authenticationManager) {
            this.authenticationManager = Optional.of(authenticationManager);
        }

        @Override
        protected AuthenticationManager authenticationManager() throws Exception {
            return this.authenticationManager.get();
        }

        protected BeanFactory getBeanFactory() {
            return this.beanFactory.get();
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = Optional.of(beanFactory);
        }
    }

    @Configuration
    @Order(1)
    public static class EmbeddedViewerSecurityConfig
        extends AbstractWebSecurityConfig {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher(DispatchServletConfig.EMBEDDED_VIEWER_PATTERN)
                .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                .headers()
                    // The embedded viewer has to be embeddable on external
                    // domains.
                    .frameOptions().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class GeneralSecurityConfig
        extends AbstractWebSecurityConfig {

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
                // Mark proxied secure requests as being secure.
                .addFilterBefore(
                    getBeanFactory()
                        .getBean(SecureRequestProxyHeaderFilter.class),
                    ChannelProcessingFilter.class)
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

                .requestCache()
                    .requestCache(requestCache)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(entryPoint)
                    .and()
                .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler(
                        getBeanFactory().getBean(LogoutSuccessHandler.class))
                    .deleteCookies("JSESSIONID")
                    .and()
                .authorizeRequests()
                    .antMatchers("/admin/**", "/editor/**").hasRole("ADMIN")
                    .antMatchers("/mylibrary/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                    .and()
                .headers()
                    .frameOptions().sameOrigin();
        }
    }
}
