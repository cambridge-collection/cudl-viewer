package ulcambridge.foundations.viewer.frontend;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.FrontEndBuild;
import ulcambridge.foundations.frontend.webpack.WebpackBuild;
import ulcambridge.foundations.frontend.webpack.WebpackBuild.Chunk;
import ulcambridge.foundations.frontend.webpack.WebpackBuilds;
import ulcambridge.foundations.frontend.webpack.WebpackFrontEndBuildAdaptor;
import ulcambridge.foundations.frontend.webpack.WebpackMetadataException;

import java.io.IOException;
import java.net.URI;

/**
 * A BuildFactory which provides builds which use a running webpack dev server.
 */
public class DefaultBuildFactory implements BuildFactory {

    // /ulcambridge/foundations/viewer/viewer-frontend/webpack-assets-dev.json
    // /ulcambridge/foundations/viewer/viewer-frontend/deps.json

    private final ResourceLoader resourceLoader;

    private final URI baseUri;
    private final String buildMetadataResourcePath;
    private final String buildDependenciesResourcePath;

    private final WebpackBuild build;

    public DefaultBuildFactory(
            ResourceLoader resourceLoader,
            String buildMetadataResourcePath,
            String buildDependenciesResourcePath,
            URI baseUri)
            throws IOException, WebpackMetadataException {

        Assert.notNull(resourceLoader);
        Assert.hasText(buildMetadataResourcePath);
        Assert.hasText(buildDependenciesResourcePath);
        Assert.notNull(baseUri);

        this.resourceLoader = resourceLoader;
        this.baseUri = baseUri;
        this.buildMetadataResourcePath = buildMetadataResourcePath;
        this.buildDependenciesResourcePath = buildDependenciesResourcePath;

        this.build = this.createBuild();
    }

    private WebpackBuild createBuild()
            throws IOException, WebpackMetadataException {

        Resource metadata = this.resourceLoader.getResource(
                this.buildMetadataResourcePath);
        Resource dependencies = this.resourceLoader.getResource(
                this.buildDependenciesResourcePath);

        return WebpackBuilds.create(
                this.baseUri,
                metadata.getInputStream(),
                dependencies.getInputStream());
    }

    @Override
    public FrontEndBuild getBuild(PageType pageType) {
        Chunk initialChunk = this.build.getChunk(pageType.getChunkName());
        return new WebpackFrontEndBuildAdaptor(initialChunk);
    }
}
