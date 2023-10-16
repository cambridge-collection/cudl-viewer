package ulcambridge.foundations.viewer.model;

/**
 * This class represents an theme image as defined in the cudl.ui.json5 file.
 */
public class UIThemeImage {
    protected String src;
    protected String alt;
    protected String href;
    /*
        ignore ImageSet at this time.
     */

    public UIThemeImage(String src, String alt, String href) {
        this.src = src;
        this.alt = alt;
        this.href = href;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
