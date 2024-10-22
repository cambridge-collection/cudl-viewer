package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UIData {
    protected List<UIDataCollection> collections;

    public UIData() {
    }

    @JsonProperty("collections")
    public List<UIDataCollection> getCollections() {
        return collections;
    }

    @JsonProperty("collections")
    public void setCollections(List<UIDataCollection> collections) {
        this.collections = collections;
    }
}
