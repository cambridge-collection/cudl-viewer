package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Lei
 * 
 */
public class JsonResponse {

	@SerializedName("code")
	private String status;

	@SerializedName("msg")
	private String message;

	@SerializedName("result")
	private DocumentTerms terms;

	@SerializedName("redirect")
	private boolean redirect;

	@SerializedName("redirectURL")
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
