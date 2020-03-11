package ulcambridge.foundations.viewer;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;
import ulcambridge.foundations.viewer.components.Captcha;
import ulcambridge.foundations.viewer.components.EmailHelper;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextHierarchy({
    @ContextConfiguration(name = "child", classes = {FormControllerTest.Config.class})
})
public class FormControllerTest extends BaseCUDLApplicationContextTest {

    @Configuration
    static class Config {
        /* TODO: would be good to get the JSP rendered, but I'm not sure how.
         Including DispatchServletConfig right now adds the entire app,
          which would require a lot of mocks to avoid all the db and git actions.
         */
        @Bean
        public MockRestServiceServer mockRestServiceServer(RestTemplate restTemplate) {
            return MockRestServiceServer.bindTo(restTemplate).build();
        }
    }

    @Autowired private EmailHelper mockEmailHelper;
    @Autowired private Captcha captcha;
    @Autowired private MockMvc mockMvc;
    @Autowired private MockRestServiceServer mockService;

    @AfterEach
    public void resetMocks() {
        this.mockService.reset();
    }

    @Test
    public void getForm() throws Exception {
        mockMvc.perform(get("/feedbackform.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("feedbackForm"))
            .andExpect(model().attribute("captchaHtml", equalTo("<captcha/>")));
    }

    @Test
    public void postFormSuccess() throws Exception {
        doReturn(true).when(mockEmailHelper).sendEmail(any(), any(), any(), any());
        doReturn(true).when(captcha).verify(eq("TOKEN"), any());

        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "user@example.test")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback-success"))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attribute("submissionFailed", is(oneOf(false, null))));

        String expectedMessage = "Feedback from the user 'Foo' (user@example.test):\n\nBar";
        verify(mockEmailHelper).sendEmail(anyString(), anyString(), anyString(), eq(expectedMessage));
    }

    @Test
    public void postFormCaptchaFailed() throws Exception {
        doReturn(true).when(mockEmailHelper).sendEmail(any(), any(), any(), any());
        doReturn(false).when(captcha).verify(eq("TOKEN"), any());

        mockMvc.perform(post("/feedbackform.html")
            .param("name", "Foo")
            .param("email", "user@example.test")
            .param("comment", "Bar")
            .param("g-recaptcha-response", "TOKEN"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().attribute("captchaHtml", equalTo("<captcha/>")))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", is(oneOf(false, null))));
    }

    @Test
    public void postFormEmailFailed() throws Exception {
        doReturn(false).when(mockEmailHelper).sendEmail(any(), any(), any(), any());
        doReturn(true).when(captcha).verify(eq("TOKEN"), any());

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

    @ParameterizedTest
    @MethodSource("badFeedbackParameters")
    public void postFormMissingParameters(Map<String, String> parameters) throws Exception {
        MockHttpServletRequestBuilder post = post("/feedbackform.html")
            .param("g-recaptcha-response", "TOKEN");
        parameters.forEach(post::param);
        mockMvc.perform(post)
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/feedback"))
            .andExpect(model().hasErrors())
            .andExpect(model().attribute("submissionFailed", false));
    }

    private static Stream<Arguments> badFeedbackParameters() {
        return Stream.of(
            Arguments.of(ImmutableMap.of()),
            Arguments.of(ImmutableMap.of("name", "", "email", "", "comment", "")),
            Arguments.of(ImmutableMap.of("name", "Foo", "email", "not an email", "comment", "Bar"))
        );
    }
}
