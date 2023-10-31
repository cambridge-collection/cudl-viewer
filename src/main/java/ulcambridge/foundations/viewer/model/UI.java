package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

/**
 * For this we only care about the theme-ui part of the cudl.ui.json5 file at the moment.
 * To be extended later for the theme-data
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UI {

    protected UITheme themeUI;

    @JsonProperty("@type")
    protected String type;

    @JsonProperty("theme-name")
    protected String theme_name;

    @JsonProperty("theme-data")
    protected UIThemeData theme_data;

    @ConstructorProperties({"@type", "theme-name", "theme-data", "theme-ui"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UI(@JsonProperty("@type") String type,
              @JsonProperty("theme-name") String theme_name,
              @JsonProperty("theme-data") UIThemeData theme_data,
              @JsonProperty("theme-ui") UITheme theme_ui) {
        this.type = type;
        this.theme_name = theme_name;
        this.theme_data = theme_data;
        this.themeUI = theme_ui;
    }

    @JsonProperty("theme-ui")
    public UITheme getThemeUI() {
        return themeUI;
    }


    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("theme-name")
    public String getThemeName() {
        return theme_name;
    }

    @JsonProperty("theme-data")
    public UIThemeData getThemeData() {
        return theme_data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class Dataset {\n");
        sb.append("    @type: ").append(toIndentedString(type)).append("\n");
        sb.append("    theme-name: ").append(toIndentedString(theme_name)).append("\n");
        sb.append("    theme-data: ").append(toIndentedString(theme_data)).append("\n");
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
