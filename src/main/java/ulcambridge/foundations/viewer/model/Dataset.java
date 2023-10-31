package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.util.List;

public class Dataset {

    private final String type;
    private final String name;
    private final List<Id> collections;

    @ConstructorProperties({"@type", "name", "collections"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Dataset(@JsonProperty("@type") String type,
                   @JsonProperty("name") String name,
                   @JsonProperty("collections") List<Id> collections) {
        this.type = type;
        this.name = name;
        this.collections = collections;

    }

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("collections")
    public List<Id> getCollections() {
        return collections;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class Dataset {\n");
        sb.append("    @type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    collections: ").append(toIndentedString(collections)).append("\n");
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
