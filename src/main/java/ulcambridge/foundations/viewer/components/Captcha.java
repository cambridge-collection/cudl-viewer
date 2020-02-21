package ulcambridge.foundations.viewer.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@Component
@Profile("!test")
public class Captcha {
    public final static String RESPONSE_PARAM = "g-recaptcha-response";
    public final static String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String siteKey;
    private final String secretKey;
    private final RestOperations operations;

    public Captcha(
        @Value("${recaptcha.siteKey}") String siteKey,
        @Value("${recaptcha.secretKey}")String secretKey,
        @Autowired RestOperations operations
    ) {
        this.siteKey = siteKey;
        this.secretKey = secretKey;
        this.operations = operations;
    }

    public String getHtml() {
        return String.format(
            "<script src='https://www.google.com/recaptcha/api.js' async='false'></script>" +
            "<div class='g-recaptcha' data-sitekey='%s'></div>",
            HtmlUtils.htmlEscape(this.siteKey)
        );
    }

    public String getResponseParam() {
        return RESPONSE_PARAM;
    }

    public boolean verify(String responseToken) {
        return this.verify(responseToken, null);
    }

    public boolean verify(String responseToken, String remoteAddr) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("secret", this.secretKey);
        request.add("response", responseToken);
        if (remoteAddr != null) {
            request.add("remoteip", remoteAddr);
        }
        Map response = operations.postForObject(VERIFY_URL, request, Map.class);
        return Boolean.TRUE == response.get("success");
    }
}
