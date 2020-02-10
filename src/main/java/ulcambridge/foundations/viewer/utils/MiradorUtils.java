package ulcambridge.foundations.viewer.utils;

import org.apache.commons.io.FileUtils;
import ulcambridge.foundations.viewer.model.Properties;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Class for getting mirador configuration information based on itemId
 * and application properties set.
 */
public class MiradorUtils {

    private final String id;
    private final String manifestId;
    private final String canvasId;

    public MiradorUtils(String id, String manifestId, String canvasId) {
        this.id = id;
        this.manifestId = manifestId;
        this.canvasId = canvasId;
    }

    public String getMiradorConfig() {

        try {
            String propsPath = Properties.getString("mirador.options.default.config");
            String itemProps = Properties.getString("mirador.options."+id+".config");
            if (itemProps!=null) {
                propsPath = itemProps;
            }

            assert propsPath != null;

            String props = FileUtils.readFileToString(new File(propsPath), "UTF-8");

            // replace placeholders
            props = props.replaceAll("<!--manifestId-->", manifestId);
            props = props.replaceAll("<!--canvasId-->", canvasId);

            return props;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public int getCompanionWindowsProperty() {
        String itemCompanionWindows = Properties.getString("mirador.options."+id+".companionWindows");
        if (itemCompanionWindows!=null) {
            return Integer.parseInt(itemCompanionWindows);
        }
        return Integer.parseInt(Objects.requireNonNull(Properties.getString("mirador.options.default.companionWindows")));
    }

    public String getCompanionWindowsAlignProperty(int index) {
        String itemCompanionWindows = Properties.getString("mirador.options."+id+".companionWindows."+index+
            ".align");
        if (itemCompanionWindows!=null) {
            return itemCompanionWindows;
        }
        return Properties.getString("mirador.options.default.companionWindows.align");
    }

    public String getCompanionWindowsContentProperty(int index) {
        String itemCompanionWindows = Properties.getString("mirador.options."+id+".companionWindows."+index+
            ".content");
        if (itemCompanionWindows!=null) {
            return itemCompanionWindows;
        }
        return Properties.getString("mirador.options.default.companionWindows.content");
    }

    public String getCompanionWindows (int index) {
        return "{"+
            "\"content\": \""+getCompanionWindowsContentProperty(index)+"\","+
            "\"default\": true,"+
            "\"position\": \""+getCompanionWindowsAlignProperty(index)+"\""+
        "}";
    }
}
