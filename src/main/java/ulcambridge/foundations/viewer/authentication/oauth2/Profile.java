package ulcambridge.foundations.viewer.authentication.oauth2;

import java.util.Optional;

public interface Profile {
    String getTypeName();
    String getId();
    Optional<String> getEmailAddress();
}
