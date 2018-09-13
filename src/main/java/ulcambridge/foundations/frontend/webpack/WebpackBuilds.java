package ulcambridge.foundations.frontend.webpack;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * Convenience wrapper for {@link #create(URI, Iterable, Map)} which parses
     * the provided InputStreams using {@link #parseDependencies(InputStream)}
     * and {@link #parseMetadata(InputStream)}.
     *
     * @param baseUri The URL that paths in the webpack metadata are relative
     *                to.
     * @param metadata The webpack build metadata file.
     * @param dependencies The initial/entry chunk dependency graph file.
     * @return A {@link WebpackBuild} instance representing the provided
     *         webpack build metadata.
     * @throws IOException If the metadata source can't be read
     * @throws WebpackMetadataException If the metadata is invalid
     */
    public static WebpackBuild create(
            URI baseUri,
            InputStream metadata, InputStream dependencies)
            throws IOException, WebpackMetadataException {

        return create(baseUri, parseMetadata(metadata), parseDependencies(dependencies));
    }

    /**
     * Create a {@link WebpackBuild} from initial/entry chunk definitions and
     * a dependency graph specifying which chunks are required to be loaded
     * before a given chunk.
     *
     * @param baseUri The URL that paths in the webpack metadata are relative
     *                to.
     * @param metadata The webpack build metadata file.
     * @param dependencies The initial/entry chunk dependency graph file.
     * @return A {@link WebpackBuild} instance representing the provided
     *         webpack build metadata.
     * @throws WebpackMetadataException If the metadata is invalid
     */
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
     * <p>Note that this function does not perform any verification of
     * the dependency graph, i.e. cycle detection etc.
     *
     * <p>The format of the JSON file is as follows:
     * <pre>{@code
     * {
     *     "foo": [],
     *     "bar": ["foo"],
     *     "baz": ["bar", "boz"],
     *     "boz": []
     * }
     * }</pre>
     *
     * <p>This specifies that {@code baz} depends on all other chunks,
     * {@code foo} depends on no chunks.
     *
     * @return A map of chunk names to lists of dependency chunks.
     * @throws IOException If the stream can't be read
     * @throws WebpackMetadataException If the input is malformed
     */
    public static Map<String, List<String>> parseDependencies(
            InputStream input) throws WebpackMetadataException, IOException {
        JsonNode root;
        try {
             root = new ObjectMapper().readTree(input);
        }
        catch(JsonProcessingException e) {
            throw new WebpackMetadataException(
                    "Dependencies file was not valid JSON", e);
        }

        if(!root.isObject())
            throw new WebpackMetadataException("Root was not a JSON object");

        ObjectNode chunks = (ObjectNode)root;
        Map<String, List<String>> dependencies =
                new HashMap<String, List<String>>();

        Iterator<String> names = chunks.fieldNames();
        List<String> deps = new ArrayList<String>();
        while(names.hasNext()) {
            String name = names.next();
            JsonNode chunkNode = chunks.get(name);
            if(!chunkNode.isArray()) {
                throw new WebpackMetadataException(
                        "Chunk value was not an array: " + name);
            }

            deps.clear();
            for(JsonNode node : chunkNode) {
                if(!node.isTextual()) {
                    throw new WebpackMetadataException(
                            "Dependencies must be strings (chunk names)");
                }
                deps.add(asString(node));
            }

            dependencies.put(name, deps.isEmpty() ?
                    Collections.<String>emptyList() :
                    Collections.unmodifiableList(new ArrayList<String>(deps)));
        }

        return Collections.unmodifiableMap(dependencies);
    }

    /**
     * Parse an input stream containing a webpack build metadata JSON document.
     *
     * <p>The format of the JSON file is as follows:
     *
     * <pre>{@code
     * {
     *     "foo": { "js": "js/foo.js" },
     *     "bar": {
     *         "js": "js/bar.js",
     *         "css": "css/bar.css"
     *     },
     *     "baz": { "js": "js/baz.js" },
     *     "boz": { "css": "css/boz.css" },
     *     "": {
     *         "png": ["foo.png", "bar.png"],
     *         "svg": ["foo.svg", "bar.svg"]
     *     }
     * }
     * }</pre>
     *
     * <p>Each key of the root object defines a chunk with the key's name.
     * A {@code js} or {@code css} attribute defines the path to the chunk's
     * javascript or css file.
     *
     * Anonymous chunks (e.g. files referenced from but not contained in named
     * chunks) are placed in a special section with an empty name. These
     * anonymous chunks are ignored, as they're not entry points which will be
     * explicitly loaded - they're referenced from other named chunks.
     *
     * Each {@link MetadataRecord} represents a single css or js file, so a
     * chunk can (currently) have one or two records.
     *
     * @param input The JSON document to parse
     * @return A set of the js and css records defined in the metadata.
     * @throws IOException If the stream can't be read
     * @throws WebpackMetadataException If the metadata is malformed
     */
    public static Set<MetadataRecord> parseMetadata(InputStream input)
            throws IOException, WebpackMetadataException {

        JsonNode root;
        try {
            root = new ObjectMapper().readTree(input);
        }
        catch(JsonProcessingException e) {
            throw new WebpackMetadataException(
                    "Metadata file was not valid JSON", e);
        }

        if(!root.isObject())
            throw new WebpackMetadataException("Root was not a JSON object");

        ObjectNode chunks = (ObjectNode)root;
        Set<MetadataRecord> records = new HashSet<MetadataRecord>();

        Iterator<String> names = chunks.fieldNames();
        while(names.hasNext()) {
            String name = names.next();
            JsonNode chunkNode = chunks.get(name);
            if(!chunkNode.isObject())
                throw new WebpackMetadataException("Chunk was not a JSON object");

            // Ignore the anonymous chunks
            if(name.length() == 0)
                continue;

            if(chunkNode.has("css")) {
                records.add(MetadataRecord.fromFilename(
                        name,
                        asString(chunkNode.get("css")),
                        FrontEndBuild.ResourceType.CSS));
            }

            if(chunkNode.has("js")) {
                records.add(MetadataRecord.fromFilename(
                        name,
                        asString(chunkNode.get("js")),
                        FrontEndBuild.ResourceType.JAVASCRIPT));
            }
        }

        return Collections.unmodifiableSet(records);
    }

    private static String asString(JsonNode node) throws WebpackMetadataException{
        if(!node.isTextual())
            throw new WebpackMetadataException(
                    "Unexpected node type: " + node.getNodeType());
        return node.textValue();
    }

    public static class MetadataRecord {
        private final String name;
        private final URI uri;
        private final FrontEndBuild.ResourceType type;

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

        private static MetadataRecord fromFilename(
                String name, String filename, FrontEndBuild.ResourceType type)
                throws WebpackMetadataException {

            Assert.notNull(type);
            if(name.length() == 0)
                throw new WebpackMetadataException(
                        "chunk name was empty: " + name);

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MetadataRecord that = (MetadataRecord) o;

            if (!name.equals(that.name)) return false;
            if (!uri.equals(that.uri)) return false;
            return type == that.type;

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + uri.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "MetadataRecord{" +
                    "name='" + name + '\'' +
                    ", uri=" + uri +
                    ", type=" + type +
                    '}';
        }
    }
}
