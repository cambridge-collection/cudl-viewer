package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * For this we only care about the theme-ui part of the cudl.ui.json5 file at the moment.
 * To be extended later for the theme-data
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UI {

    protected String themeName;

    protected UITheme themeUI;

    public UI() {
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public UITheme getThemeUI() {
        return themeUI;
    }

    public void setThemeUI(UITheme themeUI) {
        this.themeUI = themeUI;
    }
}
