package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Lei
 *
 */
public class JsonResponse {

    @JsonProperty("code")
    private String status;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("result")
    private DocumentTerms terms;

    @JsonProperty("redirect")
    private boolean redirect;

    @JsonProperty("redirectURL")
    private String redirectURL;

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public JsonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonResponse(String status, String message, DocumentTerms terms) {
        this.status = status;
        this.message = message;
        this.terms = terms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DocumentTerms getTerms() {
        return terms;
    }

    public void setTerms(DocumentTerms terms) {
        this.terms = terms;
    }

}
