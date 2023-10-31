package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.beans.ConstructorProperties;

public class CollectionDescription {

    private final String shortDescription;
    private final String medium;
    private Id full;

    @ConstructorProperties({"shortDescription", "full", "medium"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CollectionDescription(@JsonProperty("short") String shortDescription,
                                 @JsonProperty("full") Id full,
                                 @JsonProperty("medium") String medium) {
        this.shortDescription = shortDescription;
        this.medium = medium;
        this.full = full;
    }

    public static CollectionDescription copyOf(CollectionDescription other) {
        Preconditions.checkNotNull(other);
        return new CollectionDescription(other.shortDescription, other.full, other.medium);
    }

    @JsonProperty("short")
    public String getShortDescription() {
        return shortDescription;
    }

    public Id getFull() {
        return full;
    }

    public void setFull(Id full) {
        this.full = full;
    }

    public String getMedium() {
        return medium;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class CollectionDescription {\n");
        sb.append("    short: ").append(toIndentedString(shortDescription)).append("\n");
        sb.append("    medium: ").append(toIndentedString(medium)).append("\n");
        sb.append("    full: ").append(toIndentedString(full)).append("\n");
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CollectionDescription) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
