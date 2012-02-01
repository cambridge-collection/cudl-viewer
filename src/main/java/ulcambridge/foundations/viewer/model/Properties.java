package ulcambridge.foundations.viewer.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Properties {

	private final static ResourceBundle globalConfig = ResourceBundle
			.getBundle("cudl-global");

	private final static ResourceBundle collectionConfig = ResourceBundle
			.getBundle("collections");

	private static String rootURL;

	public static String getString(String key) {

		// Check global properties
		try {
			String globalValue = globalConfig.getString(key);
			if (globalValue != null) {
				return globalValue;
			}
		} catch (MissingResourceException e) { 
			/* resource not found, look in next bundle */
		}

		// Check collection properties
		try {			
			String collectionValue = collectionConfig.getString(key);
			if (collectionValue != null) {
				return collectionValue;
			}
		} catch (MissingResourceException e) { 
			/* resource not found, look in next bundle */
		}

		return null;
	}

	public static void setRootURL(String url) {
		rootURL = url;
	}

	/**
	 * @return The root URL at which this application is running.
	 */
	public static String getRootURL() {
		return "http://cudl.lib.cam.ac.uk";
	}

}
