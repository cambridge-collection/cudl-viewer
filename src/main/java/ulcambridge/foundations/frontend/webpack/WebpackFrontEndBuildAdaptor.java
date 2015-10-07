package ulcambridge.foundations.frontend.webpack;

import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.FrontEndBuild;

import java.util.ArrayList;
import java.util.List;

/**
 * An adaptor which presents a chunk (and it's dependencies) from a
 * {@link WebpackBuild} as a {@link FrontEndBuild}.
 */
public class WebpackFrontEndBuildAdaptor implements FrontEndBuild {

    private final WebpackBuild webpackBuild;
    private final WebpackBuild.Chunk chunk;

    public WebpackFrontEndBuildAdaptor(WebpackBuild.Chunk chunk) {
        Assert.notNull(chunk);
        this.webpackBuild = chunk.getBuild();
        assert this.webpackBuild != null;
        this.chunk = chunk;
    }

    @Override
    public List<Resource> resources() {
        List<Resource> resources = new ArrayList<Resource>();

        List<WebpackBuild.Chunk> deps =
                this.webpackBuild.getLinearisedDependencies(this.chunk);

        assert ResourceType.values().length == 2 :
                "Only Javascript and CSS resources are supported";

        // Chunks can have extracted CSS as well as javascript. We'll expose all
        // the CSS URLs first, then the JS URLs as:
        // - The loading order of the CSS files doesn't matter from the point of
        //   view of other CSS files
        // - The JS could conceivably expect some CSS to be defined before it
        //   is executed.
        // - The CSS can be loaded in parallel, without being blocked by
        //   interleaved javascript loads

        for(WebpackBuild.Chunk c : deps) {
            if(c.getUris().containsKey(ResourceType.CSS)) {
                resources.add(new LinkedCSSResource(
                        c.getUris().get(ResourceType.CSS)));
            }
        }

        for(WebpackBuild.Chunk c : deps) {
            if(c.getUris().containsKey(ResourceType.JAVASCRIPT)) {
                resources.add(new LinkedJavascriptResource(
                        c.getUris().get(ResourceType.JAVASCRIPT)));
            }
        }

        return resources;
    }
}
