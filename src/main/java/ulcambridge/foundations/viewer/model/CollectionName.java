package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class CollectionName {

    private final String urlslug;
    private final String sort;
    private final String shortName;
    private final String full;

    @ConstructorProperties({"urlSlug", "sort", "shortName", "full"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CollectionName(@JsonProperty("url-slug") String urlslug,
                          @JsonProperty("sort") String sort,
                          @JsonProperty("short") String shortName,
                          @JsonProperty("full") String full) {
        this.urlslug = urlslug;
        this.sort = sort;
        this.shortName = shortName;
        this.full = full;
    }

    @JsonProperty("url-slug")
    public String getUrlSlug() {
        return urlslug;
    }

    public String getSort() {
        return sort;
    }

    @JsonProperty("short")
    public String getShortName() {
        return shortName;
    }

    public String getFull() {
        return full;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class CollectionName {\n");
        sb.append("    short: ").append(toIndentedString(shortName)).append("\n");
        sb.append("    url-slug: ").append(toIndentedString(urlslug)).append("\n");
        sb.append("    full: ").append(toIndentedString(full)).append("\n");
        sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
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
        if (obj instanceof CollectionName) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
