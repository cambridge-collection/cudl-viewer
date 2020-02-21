package ulcambridge.foundations.viewer.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class CaptchaTest {
    private RestTemplate restTemplate;
    private MockRestServiceServer mockRestServiceServer;
    private Captcha captcha;

    @BeforeEach
    public void setup() {
        this.restTemplate = new RestTemplate();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        this.captcha = new Captcha("SITE<>KEY", "SECRET KEY", restTemplate);
    }

    @Test
    public void html() {
        assertThat(captcha.getHtml()).contains("<script src='https://www.google.com/recaptcha/api.js' async='false'></script>");
        assertThat(captcha.getHtml()).contains("<div class='g-recaptcha' data-sitekey='SITE&lt;&gt;KEY'></div>");
    }

    @Test
    public void getResponseParam() {
        assertThat(captcha.getResponseParam()).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void verifiesResponseToken(boolean isValid) {
        String captchaResponse = "{" +
            "  \"success\": %s," +
            "  \"challenge_ts\": \"2018-06-22T14:01:10Z\"," +
            "  \"hostname\": \"testkey.google.com\"" +
            "}";

        mockRestServiceServer.expect(method(HttpMethod.POST))
            .andExpect(requestTo(Captcha.VERIFY_URL))
            .andExpect(MockRestRequestMatchers.content().formData(new LinkedMultiValueMap<>(ImmutableMap.of(
                "secret", ImmutableList.of("SECRET KEY"),
                "response", ImmutableList.of("TOKEN"),
                "remoteip", ImmutableList.of("1.2.3.4")
            ))))
            .andExpect(content().string(containsString("response=TOKEN")))
            .andRespond(withSuccess(String.format(captchaResponse, isValid), MediaType.APPLICATION_JSON));

        assertThat(captcha.verify("TOKEN", "1.2.3.4")).isEqualTo(isValid);

        mockRestServiceServer.verify();
    }
}
