package ulcambridge.foundations.viewer.embedded;


import ulcambridge.foundations.embeddedviewer.configuration.Config;
import ulcambridge.foundations.embeddedviewer.configuration.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean definitions for serving the CUDL embedded viewer assets.
 */
public final class Configs {

    private Configs() { throw new RuntimeException(); }


    public static Config createEmbeddedViewerConfig(
            String gaTrackingId,
            String metadataUrlPrefix,
            String metadataUrlSuffix,
            String dziUrlPrefix,
            String metadataUrlHost) {
        Map<ConfigProperty, String> values = new HashMap<ConfigProperty, String>();

        values.put(ConfigProperty.GA_TRACKING_ID, gaTrackingId);
        values.put(ConfigProperty.METADATA_URL_PREFIX, metadataUrlPrefix);
        values.put(ConfigProperty.METADATA_URL_SUFFIX, metadataUrlSuffix);
        values.put(ConfigProperty.DZI_URL_PREFIX, dziUrlPrefix);
        values.put(ConfigProperty.METADATA_URL_HOST, metadataUrlHost);

        return new Config(values);
    }
}
