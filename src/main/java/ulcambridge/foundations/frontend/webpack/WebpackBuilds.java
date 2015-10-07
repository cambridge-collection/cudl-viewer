package ulcambridge.foundations.frontend.webpack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.FrontEndBuild;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Helper functions for working with webpack builds.
 */
public final class WebpackBuilds {
    private WebpackBuilds() { throw new RuntimeException(); }

    public static WebpackBuild create(
            URI baseUri,
            InputStream metadata, InputStream dependencies)
            throws IOException, WebpackMetadataException {

        return create(baseUri, parseMetadata(metadata), parseDependencies(dependencies));
    }

    public static WebpackBuild create(
            URI baseUri,
            Iterable<MetadataRecord> metadata,
            Map<String, List<String>> dependencies)
            throws WebpackMetadataException {

        return new DefaultWebpackBuild(baseUri, metadata, dependencies);
    }

    /**
     * Parse a JSON chunk dependencies file.
     *
     * Note that this function does not perform any verification of
     * the dependency graph, i.e. cycle detection etc.
     *
     * @return A map of chunk names to lists of dependency chunks.
     */
    private static Map<String, List<String>> parseDependencies(
            InputStream json) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Parse an input stream containing a webpack build metadata JSON document.
     *
     * @param input The JSON document to parse
     * @return A map of chunk names to chunks
     * @throws IOException
     * @throws WebpackMetadataException If the metadata is malformed
     */
    private static Set<MetadataRecord> parseMetadata(InputStream input)
            throws IOException, WebpackMetadataException {

        JsonNode root = new ObjectMapper().readTree(input);
        if(!root.isObject())
            throw new WebpackMetadataException("Root was not a JSON object");

        ObjectNode chunks = (ObjectNode)root;
        Map<String, MetadataRecord> result = new HashMap<String, MetadataRecord>();

        Iterator<String> names = chunks.fieldNames();
        while(names.hasNext()) {
            String name = names.next();
            JsonNode chunkNode = chunks.get(name);
            if(!chunkNode.isObject())
                throw new WebpackMetadataException("Chunk was not a JSON object");

            if(name.length() == 0)
                throw new WebpackMetadataException("Chunk name was empty");

            result.put(name, MetadataRecord.fromJson(name, (ObjectNode)chunkNode));
        }

        // FIXME: handle chunk entries with css and js
        throw new RuntimeException("FIXME: implement");
//        return result;
    }

    private static String asString(JsonNode node) throws WebpackMetadataException{
        if(!node.isTextual())
            throw new WebpackMetadataException(
                    "Unexpected node type: " + node.getNodeType());
        return node.toString();
    }

    /* package */ static class MetadataRecord {
        private final String name;
        private final URI uri;
        private final FrontEndBuild.ResourceType type;

        // FIXME: change filename to path
        public MetadataRecord(String name, URI uri,
                              FrontEndBuild.ResourceType type) {

            Assert.hasText(name);
            Assert.notNull(uri);
            Assert.hasText(uri.getPath());
            Assert.notNull(type);

            this.name = name;
            this.uri = uri;
            this.type = type;
        }

        private static MetadataRecord fromJson(String name, ObjectNode chunk)
                throws WebpackMetadataException {
            FrontEndBuild.ResourceType type;
            String filename;
            if(chunk.has("css")) {
                type = FrontEndBuild.ResourceType.CSS;
                filename = asString(chunk.get("css"));
            }
            else if(chunk.has("js")) {
                type = FrontEndBuild.ResourceType.JAVASCRIPT;
                filename = asString(chunk.get("js"));
            }
            else {
                throw new WebpackMetadataException(
                        "Chunk did not have a js or css property");
            }

            if(filename.length() == 0)
                throw new WebpackMetadataException(
                        "chunk filename was empty: " + name);

            URI uri;
            try {
                uri = new URI(null, null, null, -1, filename, null, null);
            }
            catch(URISyntaxException e) {
                throw new WebpackMetadataException(
                        "Unable to convert filename into URI path: " +
                                filename);
            }

            return new MetadataRecord(name, uri, type);
        }

        public String getName() {
            return this.name;
        }

        public URI getUri() {
            return this.uri;
        }

        public FrontEndBuild.ResourceType getResourceType() {
            return this.type;
        }
    }
}
