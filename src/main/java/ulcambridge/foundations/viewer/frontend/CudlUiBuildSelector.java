package ulcambridge.foundations.viewer.frontend;

import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.webpack.WebpackMetadataException;

import java.io.IOException;
import java.net.URI;

/**
 * This
 */
public class CudlUiBuildSelector {
    private final ResourceLoader resourceLoader;

    private final String metadataResourcePath;
    private final String metadataDevserverResourcePath;
    private final String dependenciesResourcePath;

    private final URI devBaseUrl;
    private final URI baseUrl;

    private CudlUiBuildSelector(
            ResourceLoader loader,
            String metadataResourcePath,
            String metadataDevserverResourcePath,
            String dependenciesResourcePath,
            URI devBaseUrl,
            URI baseUrl) {

        Assert.notNull(loader);
        Assert.hasText(metadataResourcePath);
        Assert.hasText(metadataDevserverResourcePath);
        Assert.hasText(dependenciesResourcePath);
        Assert.notNull(devBaseUrl);
        Assert.notNull(baseUrl);

        this.resourceLoader = loader;
        this.metadataResourcePath = metadataResourcePath;
        this.metadataDevserverResourcePath = metadataDevserverResourcePath;
        this.dependenciesResourcePath = dependenciesResourcePath;
        this.devBaseUrl = devBaseUrl;
        this.baseUrl = baseUrl;
    }

    /**
     * Responsible for creating a BuildFactory implementation to provide the
     * Javascript/CSS assets for various pages of the Viewer application.
     *
     * @return A BuildFactory instance
     */
    public BuildFactory getBuildFactory(boolean useDevserver)
            throws IOException, WebpackMetadataException {

        String metadataPath;
        URI base;
        if(useDevserver) {
            metadataPath = this.metadataDevserverResourcePath;
            base = this.devBaseUrl;
        }
        else {
            metadataPath = this.metadataResourcePath;
            base = this.baseUrl;
        }

        return new DefaultBuildFactory(
                this.resourceLoader, metadataPath,
                this.dependenciesResourcePath, base);
    }
}
