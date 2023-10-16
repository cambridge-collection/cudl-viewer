package ulcambridge.foundations.viewer.model;

/**
 * For this we only care about the theme-ui part of the cudl.ui.json5 file at the moment.
 * To be extended later for the theme-data
 */
public class UI {

    protected String themeName;

    protected UITheme themeUI;

    public UI(String themeName, UITheme themeUI) {
        this.themeName = themeName;
        this.themeUI = themeUI;
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
