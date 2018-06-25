package ulcambridge.foundations.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ulcambridge.foundations.viewer.components.EmailHelper;
import ulcambridge.foundations.viewer.config.AppConfig;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isOneOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FormControllerTest {

    @Configuration
    @PropertySource("classpath:cudl-global.properties")
    @ComponentScan(basePackages = "ulcambridge.foundations.viewer.components")
    @Import(FormController.class)
    static class Config extends WebMvcConfigurerAdapter {
        /* TODO: would be good to get the JSP rendered, but I'm not sure how.
         Including DispatchServletConfig right now adds the entire app,
          which would require a lot of mocks to avoid all the db and git actions.
         */

        @Bean @Primary
        public RestTemplate restTemplate() {
            return new AppConfig().restTemplate();
        }

        @Bean @Primary
        public EmailHelper mockEmailHelper() {
            return Mockito.mock(EmailHelper.class);
        }
    }

    @Autowired private WebApplicationContext wac;
    @Autowired private RestTemplate restTemplate;
    @Autowired private EmailHelper mockEmailHelper;
    private MockMvc mockMvc;
    private MockRestServiceServer mockService;

    @Before
    public void setupMocks() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.mockService = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @After
    public void resetMocks() {
        reset(this.mockEmailHelper);
    }

    @Test
    public void getForm() throws Exception {
        mockMvc.perform(get("/feedbackform.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("feedbackForm"))
            .andExpect(model().attribute("captchaHtml", containsString("data-sitekey")));
    }

    @Test
    public void postFormSuccess() throws Exception {
        String captchaResponse = "{" +
            "  \"success\": true," +
            "  \"challenge_ts\": \"2018-06-22T14:01:10Z\"," +
            "  \"hostname\": \"testkey.google.com\"" +
            "}";

        doReturn(true).when(mockEmailHelper).sendEmail(any(), any(), any(), any());

        mockService.expect(method(HttpMethod.POST))
            .andExpect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
            .andExpect(content().string(containsString("response=TOKEN")))
            .andRespond(withSuccess(captchaResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "user@example.test")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback-success"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attribute("submissionFailed", isOneOf(false, null)));

        String expectedMessage = "Feedback from the user 'Foo' (user@example.test):\n\nBar";
        verify(mockEmailHelper).sendEmail(anyString(), anyString(), anyString(), eq(expectedMessage));
    }

    @Test
    public void postFormCaptchaFailed() throws Exception {
        String captchaResponse = "{" +
            "  \"success\": false," +
            "  \"challenge_ts\": \"2018-06-22T14:01:10Z\"," +
            "  \"hostname\": \"testkey.google.com\"" +
            "}";

        doReturn(true).when(mockEmailHelper).sendEmail(any(), any(), any(), any());

        mockService.expect(method(HttpMethod.POST))
            .andExpect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
            .andExpect(content().string(containsString("response=TOKEN")))
            .andRespond(withSuccess(captchaResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "user@example.test")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", isOneOf(false, null)));
    }

    @Test
    public void postFormEmailFailed() throws Exception {
        String captchaResponse = "{" +
            "  \"success\": true," +
            "  \"challenge_ts\": \"2018-06-22T14:01:10Z\"," +
            "  \"hostname\": \"testkey.google.com\"" +
            "}";

        doReturn(false).when(mockEmailHelper).sendEmail(any(), any(), any(), any());

        mockService.expect(method(HttpMethod.POST))
            .andExpect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
            .andExpect(content().string(containsString("response=TOKEN")))
            .andRespond(withSuccess(captchaResponse, MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "user@example.test")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attribute("submissionFailed", true));

        verify(mockEmailHelper).sendEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @Ignore("TODO: Unable to get the @Valid annotation working. No idea how it's enabled for the real server.")
    public void postFormMissingParameters() throws Exception {
        mockMvc.perform(post("/feedbackform.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", false));
    }

    @Test
    @Ignore("TODO: Unable to get the @Valid annotation working. No idea how it's enabled for the real server.")
    public void postFormEmptyParameters() throws Exception {
        mockMvc.perform(post("/feedbackform.html")
            .param("name", "")
            .param("email", "")
            .param("comment", "")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", false));
    }

    @Test
    @Ignore("TODO: Unable to get the @Valid annotation working. No idea how it's enabled for the real sever.")
    public void postFormBadEmail() throws Exception {
        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "not an email")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", false));
    }

}
