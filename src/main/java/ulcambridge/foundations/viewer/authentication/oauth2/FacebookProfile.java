package ulcambridge.foundations.viewer.authentication.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookProfile extends DefaultProfile {

    @JsonCreator
    public FacebookProfile(@JsonProperty("id") String id,
                           @JsonProperty("email") String email) {
        super("facebook", id, email);
    }
}
