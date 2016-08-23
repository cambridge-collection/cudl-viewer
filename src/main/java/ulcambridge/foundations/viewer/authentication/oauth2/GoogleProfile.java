package ulcambridge.foundations.viewer.authentication.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleProfile extends DefaultProfile {

    public GoogleProfile(String id, String email) {
        super("google", id, email);
    }

    @JsonCreator
    public static GoogleProfile create(
        @JsonProperty("sub") String id, @JsonProperty("email") String email,
        @JsonProperty("email_verified") boolean isEmailVerified) {

        return new GoogleProfile(id, isEmailVerified ? email : null);
    }

}
