package ulcambridge.foundations.viewer.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

// FIXME: replace usages of this with value injection
public class Properties {

    private final static ResourceBundle globalConfig = ResourceBundle
            .getBundle("cudl-global");

    private final static ResourceBundle collectionConfig = ResourceBundle
            .getBundle("collections");

    private final static ResourceBundle applicationConfig = ResourceBundle
        .getBundle("application");

    public static String getString(String key) {

        // Check global properties
        try {
            return globalConfig.getString(key);
        } catch (MissingResourceException e) {
            /* resource not found, look in next bundle */
        }

        // Check collection properties
        try {
            return collectionConfig.getString(key);
        } catch (MissingResourceException e) {
            /* resource not found, look in next bundle */
        }

        // Check application properties
        try {
            return applicationConfig.getString(key);
        } catch (MissingResourceException e) {
            /* resource not found, look in next bundle */
        }

        return null;
    }

}
