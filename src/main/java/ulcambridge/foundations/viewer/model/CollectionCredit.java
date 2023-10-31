package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.beans.ConstructorProperties;

public class CollectionCredit {

    private Id prose;

    @ConstructorProperties({"prose"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CollectionCredit(@JsonProperty("prose") Id prose) {
        this.prose = prose;
    }

    public static CollectionCredit copyOf(CollectionCredit other) {
        Preconditions.checkNotNull(other);
        return new CollectionCredit(other.prose);
    }

    public Id getProse() {
        return prose;
    }

    public void setProse(Id id) {
        this.prose = id;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class CollectionCredit {\n");
        sb.append("    prose: ").append(toIndentedString(prose)).append("\n");
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
        if (obj instanceof CollectionCredit) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
