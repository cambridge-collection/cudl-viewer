package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UIDataCollection {
    protected Id collection;
    protected String layout;
    protected Id thumbnail;
    protected boolean mapEnabled;
    protected boolean mapCollapsed;

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

    /**
     * Whether the collection's search results can be shown on a map:
     * {@code "map": true}, or {@code "map": {"collapsed": true}} for a map that
     * starts closed because many of the collection's items have no location.
     * Absent or {@code false} means no map.
     */
    @JsonProperty("map")
    public void setMap(JsonNode map) {
        this.mapEnabled = map != null && (map.isObject() || map.asBoolean(false));
        this.mapCollapsed = this.mapEnabled && map.path("collapsed").asBoolean(false);
    }

    public boolean isMapEnabled() { return mapEnabled; }

    public boolean isMapCollapsed() { return mapCollapsed; }

}
