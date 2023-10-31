package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UICollection {

    @JsonProperty("collection")
    private final Id collection;
    @JsonProperty("layout")
    private final String layout;
    @JsonProperty("thumbnail")
    private final Id thumbnail;

    @ConstructorProperties({"collection", "layout", "thumbnail"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UICollection(@JsonProperty("collection") Id collection,
                        @JsonProperty("layout") String layout,
                        @JsonProperty("thumbnail") Id thumbnail) {
        this.collection = collection;
        this.layout = layout;
        this.thumbnail = thumbnail;

    }

    @JsonProperty("collection")
    public Id getCollection() {
        return collection;
    }

    @JsonProperty("layout")
    public String getLayout() {
        return layout;
    }

    @JsonProperty("thumbnail")
    public Id getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        sb.append("    \"collection\": ").append(toIndentedString(collection)).append("\n");
        sb.append("    \"layout\": ").append(toIndentedString(layout)).append("\n");
        sb.append("    \"thumbnail\": ").append(toIndentedString(thumbnail)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
