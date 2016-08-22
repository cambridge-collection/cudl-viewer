package ulcambridge.foundations.viewer.authentication.oauth2;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkedinProfile extends DefaultProfile {

    @JsonCreator
    public LinkedinProfile(@JsonProperty("id") String id,
                           @JsonProperty("emailAddress") String emailAddress) {

        super("linkedin", id, emailAddress);
    }
}
