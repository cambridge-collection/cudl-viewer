package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONPropertyName;

/**
 * For this we only care about the theme-ui part of the cudl.ui.json5 file at the moment.
 * To be extended later for the theme-data
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UI {

    protected String themeName;
    protected UITheme themeUI;
    protected UIData themeData;

    public UI() {
    }

    @JsonProperty("theme-name")
    public String getThemeName() {
        return themeName;
    }

    @JsonProperty("theme-name")
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    @JsonProperty("theme-ui")
    public UITheme getThemeUI() {
        return themeUI;
    }

    @JsonProperty("theme-ui")
    public void setThemeUI(UITheme themeUI) {
        this.themeUI = themeUI;
    }

    @JsonProperty("theme-data")
    public UIData getThemeData() {
        return themeData;
    }

    @JsonProperty("theme-data")
    public void setThemeData(UIData themeData) {
        this.themeData = themeData;
    }
}
