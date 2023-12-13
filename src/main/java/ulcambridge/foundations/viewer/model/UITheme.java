package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UITheme {
    protected List<String> css;
    protected List<String> js;
    protected HashMap<String,UIThemeImage> images;
    protected String title;
    protected String description;
    protected String attribution;

    public UITheme() {
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> cssList) {

        List<String> updatedCSS = new ArrayList<>();
        for (String css : cssList) {
            updatedCSS.add(css.replaceFirst("^ui/","/themeui/" ));
        }

        this.css = updatedCSS;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> jsList) {

        List<String> updatedJS = new ArrayList<>();
        for (String js : jsList) {
            updatedJS.add(js.replaceFirst("^ui/","/themeui/" ));
        }

        this.js = updatedJS;
    }

    public HashMap<String,UIThemeImage> getImages() {
        return images;
    }

    public void setImages(HashMap<String,UIThemeImage> images) {

        for (Map.Entry<String,UIThemeImage> entry: images.entrySet()) {
            String src = entry.getValue().src.replaceFirst("^ui/", "/themeui/");
            entry.getValue().setSrc(src);
        }
        this.images = images;
    }

    public UIThemeImage getImage(String name) {
        return images.get(name);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
}
