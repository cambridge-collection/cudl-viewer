package ulcambridge.foundations.viewer.model;

import java.util.HashMap;
import java.util.List;

public class UITheme {
    protected List<String> css;
    protected List<String> js;
    protected HashMap<String,UIThemeImage> images;

    public UITheme(List<String> css, List<String> js, HashMap<String,UIThemeImage> images) {
        this.css = css;
        this.js = js;
        this.images = images;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public HashMap<String,UIThemeImage> getImages() {
        return images;
    }

    public void setImages(HashMap<String,UIThemeImage> images) {
        this.images = images;
    }

    public UIThemeImage getImage(String name) {
        return images.get(name);
    }
}
