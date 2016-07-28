package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Lei
 *
 */
public class UserAnnotations {

    @SerializedName("oid")
    private String userId;

    @SerializedName("total")
    private int total;

    @SerializedName("annotations")
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
