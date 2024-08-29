package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UIDataCollection {
    protected Id collection;
    protected String layout;
    protected Id thumbnail;

    public UIDataCollection() {
    }

    @JsonProperty("collection")
    public Id getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(Id collection) {
        this.collection = collection;
    }

    @JsonProperty("layout")
    public String getLayout() { return layout; }

    @JsonProperty("layout")
    public void setLayout(String layout) { this.layout = layout; }

    @JsonProperty("thumbnail")
    public Id getThumbnail() { return thumbnail; }

    @JsonProperty("thumbnail")
    public void setThumbnail(Id thumbnail) { this.thumbnail = thumbnail; }

}
