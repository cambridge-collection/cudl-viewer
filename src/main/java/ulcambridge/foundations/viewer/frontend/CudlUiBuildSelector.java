package ulcambridge.foundations.viewer.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.webpack.WebpackMetadataException;

import java.io.IOException;
import java.net.URI;

/**
 * This
 */
@Component
public class CudlUiBuildSelector {
    private final ResourceLoader resourceLoader;

    private final String metadataResourcePath;
    private final String metadataDevserverResourcePath;
    private final String dependenciesResourcePath;

    private final URI devBaseUrl;
    private final URI baseUrl;

    @Autowired
    private CudlUiBuildSelector(
            ResourceLoader loader,
            @Value("${cudl.ui.metadata:classpath:ulcambridge/foundations/viewer/viewer-ui/webpack-assets.json}")
            String metadataResourcePath,
            @Value("${cudl.ui.dev.metadata:classpath:ulcambridge/foundations/viewer/viewer-ui/webpack-assets-dev.json}")
            String metadataDevserverResourcePath,
            @Value("${cudl.ui.dependencies:classpath:ulcambridge/foundations/viewer/viewer-ui/deps.json}")
            String dependenciesResourcePath,
            @Value("${cudl.ui.dev.baseUrl:http://localhost:8080/}")
            URI devBaseUrl,
            @Value("${cudl.ui.baseUrl:/ui/}")
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
    @Bean
    public BuildFactory getBuildFactory(
            @Value("${cudl.ui.dev:false}") boolean useDevserver)
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
