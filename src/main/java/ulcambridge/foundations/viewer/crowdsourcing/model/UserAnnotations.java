package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 * @author Lei
 *
 */
public class UserAnnotations {

    @JsonProperty("oid")
    private String userId;

    @JsonProperty("total")
    private int total;

    @JsonProperty("annotations")
    private List<DocumentAnnotations> documentAnnotations;

    public UserAnnotations() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DocumentAnnotations> getDocumentAnnotations() {
        return documentAnnotations;
    }

    public void setDocumentAnnotations(List<DocumentAnnotations> documentAnnotations) {
        this.documentAnnotations = documentAnnotations;
    }

}
