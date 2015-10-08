package ulcambridge.foundations.frontend.webpack;

import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.FrontEndBuild.ResourceType;
import ulcambridge.foundations.frontend.webpack.WebpackBuilds.MetadataRecord;

import java.net.URI;
import java.util.*;


/* package */ class DefaultWebpackBuild implements WebpackBuild {

    private final URI baseUri;
    private final Map<String, List<String>> dependencies;
    private final Map<String, Map<ResourceType, MetadataRecord>> records;
    private final Map<String, Chunk> namedChunks;

    DefaultWebpackBuild(Iterable<MetadataRecord> metadata,
                        Map<String, List<String>> dependencies)
            throws WebpackMetadataException {
        this(URI.create(""), metadata, dependencies);
    }

    DefaultWebpackBuild(URI baseUri, Iterable<MetadataRecord> metadata,
                               Map<String, List<String>> dependencies)
            throws WebpackMetadataException {

        Assert.notNull(baseUri);

        Map<String, List<String>> deps = new HashMap<String, List<String>>();
        for(String name : dependencies.keySet()) {
            if(name == null || name.length() == 0)
                throw new WebpackMetadataException(
                        "dependencies contained empty chunk name");

            if(dependencies.get(name) == null)
                throw new WebpackMetadataException(String.format(
                        "Null dependency list for name: %s", name));

            deps.put(name, new ArrayList<String>(dependencies.get(name)));
        }

        Map<String, Map<ResourceType, MetadataRecord>> records =
                new HashMap<String, Map<ResourceType, MetadataRecord>>();
        for(MetadataRecord record : metadata) {
            if(!records.containsKey(record.getName())) {
                records.put(record.getName(),
                        new EnumMap<ResourceType, MetadataRecord>(
                                ResourceType.class));
            }

            records.get(record.getName()).put(record.getResourceType(), record);
        }

        Map<String, Chunk> namedChunks = new HashMap<String, Chunk>();
        for(Map<ResourceType, MetadataRecord> chunkRecords : records.values()) {
            assert chunkRecords.size() > 0;
            String name = chunkRecords.values().iterator().next().getName();
            namedChunks.put(name, new DefaultChunk(this, name));
        }

        // Validate that all dependency chunk names exist
        for(List<String> depNames : deps.values()) {
            for(String depName : depNames) {
                if(!namedChunks.containsKey(depName))
                    throw new WebpackMetadataException(
                            "Undefined chunk referenced from dependencies: " +
                                    depName);
            }
        }

        this.baseUri = baseUri;
        this.dependencies = deps;
        this.records = records;
        this.namedChunks = Collections.unmodifiableMap(namedChunks);

        // Ensure the dependency graph has no chunks which depend on themselves
        checkDependencyCycles();
    }

    private URI resolveChunkUri(URI chunkUri) {
        return this.baseUri.resolve(chunkUri);
    }

    @Override
    public Chunk getChunk(String name) {
        Chunk c = this.getChunksByName().get(name);
        if(c == null)
            throw new IllegalArgumentException("No chunk named: " + name);
        return c;
    }

    @Override
    public Map<String, Chunk> getChunksByName() {
        return this.namedChunks;
    }

    private void checkDependencyCycles() throws WebpackMetadataException {
        Set<Chunk> seen = new HashSet<Chunk>();
        for(Chunk c : this.namedChunks.values()) {
            checkDependencyCycles(c, seen);
            assert seen.size() == 0;
        }
    }

    private void checkDependencyCycles(Chunk chunk, Set<Chunk> seen) throws WebpackMetadataException {
        if(seen.contains(chunk))
            throw new WebpackMetadataException(
                    "Dependency graph contains a cycle involving chunk: " +
                            chunk.getName());
        int size = seen.size();
        seen.add(chunk);
        for(Chunk c : chunk.getDependencies())
            checkDependencyCycles(c, seen);
        boolean removed = seen.remove(chunk);
        assert removed;
        assert seen.size() == size;
    }

    private void topologicalSortDfs(Chunk chunk, Set<Chunk> marked,
                                    List<Chunk> sorted) {

        // Don't allow cycles (i.e. a chunk can't depend on itself, directly
        // or indirectly)
        if(marked.contains(chunk)) {
            // This will never happen as we validate that the graph is cycle-
            // free in the constructor.
            throw new AssertionError(
                    "Dependency graph contains a cycle involving chunk: " +
                            chunk.getName());
        }
        // Don't add a subtree twice
        if(sorted.contains(chunk))
            return;

        marked.add(chunk);

        for(Chunk c : chunk.getDependencies()) {
            topologicalSortDfs(c, marked, sorted);
        }
        marked.remove(chunk);
        sorted.add(0, chunk);
    }

    @Override
    public List<Chunk> getLinearisedDependencies(Chunk chunk) {
        List<Chunk> sorted = new LinkedList<Chunk>();
        Set<Chunk> marked = new HashSet<Chunk>();

        topologicalSortDfs(chunk, marked, sorted);
        assert marked.size() == 0;

        return sorted;
    }

    @Override
    public List<Chunk> getLinearisedDependencies(String chunkName) {
        Chunk chunk = this.namedChunks.get(chunkName);
        if(chunk == null)
            throw new IllegalArgumentException("No such chunk: " + chunk);
        return getLinearisedDependencies(chunk);
    }

    @Override
    public Iterator<Chunk> iterator() {
        return this.namedChunks.values().iterator();
    }

    private static class DefaultChunk implements WebpackBuild.Chunk {
        private final DefaultWebpackBuild build;
        private final String name;
        private Map<ResourceType, URI> uris;
        private List<Chunk> dependencyChunks;

        public DefaultChunk(DefaultWebpackBuild build, String name) {
            Assert.notNull(build);
            Assert.hasText(name);

            this.build = build;
            this.name = name;
            this.uris = null;
            this.dependencyChunks = null;
        }

        @Override
        public WebpackBuild getBuild() {
            return this.build;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Map<ResourceType, URI> getUris() {
            if(this.uris == null) {
                Map<ResourceType, URI> uris =
                        new EnumMap<ResourceType, URI>(ResourceType.class);

                for(MetadataRecord record : build.records
                        .get(this.name).values()) {

                    uris.put(record.getResourceType(),
                             build.resolveChunkUri(record.getUri()));
                }

                this.uris = Collections.unmodifiableMap(uris);
            }

            assert this.uris != null;
            return this.uris;
        }

        @Override
        public List<Chunk> getDependencies() {
            if(this.dependencyChunks == null) {
                List<String> depNames = build.dependencies.get(this.name);
                if(depNames == null)
                    depNames = Collections.emptyList();
                List<Chunk> deps = new ArrayList<Chunk>(depNames.size());

                for(String name : depNames) {
                    assert build.namedChunks.get(name) != null;
                    assert build.namedChunks.get(name).getBuild() == this.build;
                    deps.add(build.namedChunks.get(name));
                }

                this.dependencyChunks = Collections.unmodifiableList(deps);
            }

            assert this.dependencyChunks != null;
            return this.dependencyChunks;
        }
    }
}
