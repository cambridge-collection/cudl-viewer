package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Id {

    protected String id;

    public Id() {
    }

    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
    }

}
