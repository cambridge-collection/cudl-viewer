package ulcambridge.foundations.viewer.authentication.oauth2;

public interface Profile {
    String getTypeName();
    String getId();
    String getEmailAddress();
}
