package ulcambridge.foundations.viewer.dao;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class CollectionsJSONDaoTest {

    @TempDir
    Path tempDir;

    private static final String MINIMAL_UI_JSON =
        "{\"@type\":\"https://schemas.cudl.lib.cam.ac.uk/cudl/ui/v1/json\"," +
        "\"theme-name\":\"test\",\"theme-data\":{\"collections\":[]}}";

    /** A collection file carrying neither isReleased nor status, as most real ones do. */
    private String collectionJson(String slug, String title) {
        return collectionJson(slug, title, "");
    }

    private String collectionJson(String slug, String title, String releaseProperties) {
        return "{\"name\":{\"url-slug\":\"" + slug + "\",\"full\":\"" + title + "\"}," +
            "\"description\":{\"full\":{\"@id\":\"collections/" + slug + "/summary.html\"}," +
            "\"medium\":\"" + title + " description\"}," +
            "\"credit\":{\"prose\":{\"@id\":\"collections/" + slug + "/sponsors.html\"}}," +
            releaseProperties +
            "\"items\":[]}";
    }

    private File writeDataset(Path dir, String... collectionIds) throws IOException {
        JSONArray collections = new JSONArray();
        for (String id : collectionIds) {
            collections.put(new JSONObject().put("@id", "collections/" + id + ".collection.json"));
        }
        JSONObject dataset = new JSONObject().put("@type", "test").put("collections", collections);
        File datasetFile = dir.resolve("dataset.json").toFile();
        Files.write(datasetFile.toPath(), dataset.toString().getBytes(StandardCharsets.UTF_8));
        return datasetFile;
    }

    private void writeCollection(Path dir, String id, String title) throws IOException {
        Path collectionsDir = dir.resolve("collections");
        Files.createDirectories(collectionsDir);
        Files.write(collectionsDir.resolve(id + ".collection.json"),
            collectionJson(id, title).getBytes(StandardCharsets.UTF_8));
    }

    private Path writeUiFile(Path dir) throws IOException {
        Path uiFile = dir.resolve("cudl.ui.json5");
        Files.write(uiFile, MINIMAL_UI_JSON.getBytes(StandardCharsets.UTF_8));
        return uiFile;
    }

    private Path writeUnreleasedCollection(Path dir, String id, String title, String releaseProperties)
        throws IOException {
        Path unreleasedDir = dir.resolve("unreleased/collections");
        Files.createDirectories(unreleasedDir);
        Files.write(unreleasedDir.resolve(id + ".collection.json"),
            collectionJson(id, title, releaseProperties).getBytes(StandardCharsets.UTF_8));
        return unreleasedDir;
    }

    @Test
    public void skipsCollectionFilesMissingFromDiskAndLoadsRemainder() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "present", "Present Title");
        File datasetFile = writeDataset(tempDir, "present", "missing");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", null);

        List<String> ids = dao.getCollectionIds();
        assertThat(ids).containsExactly("present");
    }

    /**
     * The unreleased directory is merged whatever showReleaseStatus says — the flag
     * gates the badge and notice at the render sites, not whether the collection loads.
     */
    @Test
    public void loadsUnreleasedCollections() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "main-collection", "Main Title");
        File datasetFile = writeDataset(tempDir, "main-collection");

        Path unreleasedDir = writeUnreleasedCollection(
            tempDir, "unreleased-collection", "Unreleased Title", "");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", unreleasedDir);

        assertThat(dao.getCollectionIds()).containsExactly("main-collection", "unreleased-collection");
    }

    @Test
    public void deduplicatesCollectionIdsWithMainTakingPrecedence() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "shared", "Main Title");
        File datasetFile = writeDataset(tempDir, "shared");

        Path unreleasedDir = writeUnreleasedCollection(tempDir, "shared", "Unreleased Title", "");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", unreleasedDir);

        assertThat(dao.getCollectionIds()).containsExactly("shared");
        assertThat(dao.getCollection("shared").getTitle()).isEqualTo("Main Title");
    }

    /** The shape of the five real files in {@code cudl-data-releases/collections/}. */
    @Test
    public void readsReleasedStateFromTheCollectionJson() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        Path collectionsDir = tempDir.resolve("collections");
        Files.createDirectories(collectionsDir);
        Files.write(collectionsDir.resolve("newton.collection.json"),
            collectionJson("newton", "Newton", "\"isReleased\":true,\"status\":\"released\",")
                .getBytes(StandardCharsets.UTF_8));
        File datasetFile = writeDataset(tempDir, "newton");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", null);

        assertThat(dao.getCollection("newton").isReleased()).isTrue();
        assertThat(dao.getCollection("newton").isUnreleased()).isFalse();
        assertThat(dao.getCollection("newton").getStatus()).isEqualTo("released");
    }

    /**
     * The discriminating case: the field says unreleased, so the directory it was found
     * in no longer decides. Matches {@code unreleased/collections/darwin_mss}.
     */
    @Test
    public void readsUnreleasedStateFromTheCollectionJson() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "main-collection", "Main Title");
        File datasetFile = writeDataset(tempDir, "main-collection");

        Path unreleasedDir = writeUnreleasedCollection(
            tempDir, "darwin-mss", "Darwin", "\"isReleased\":false,\"status\":\"draft\",");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", unreleasedDir);

        assertThat(dao.getCollection("darwin-mss").isUnreleased()).isTrue();
        assertThat(dao.getCollection("darwin-mss").getStatus()).isEqualTo("draft");
    }

    /**
     * A collection in the main dataset carrying isReleased:false is unreleased. Under
     * directory provenance this combination was unreachable.
     */
    @Test
    public void mainDatasetCollectionCanBeUnreleased() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        Path collectionsDir = tempDir.resolve("collections");
        Files.createDirectories(collectionsDir);
        Files.write(collectionsDir.resolve("main-collection.collection.json"),
            collectionJson("main-collection", "Main Title", "\"isReleased\":false,\"status\":\"draft\",")
                .getBytes(StandardCharsets.UTF_8));
        File datasetFile = writeDataset(tempDir, "main-collection");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", null);

        assertThat(dao.getCollection("main-collection").isUnreleased()).isTrue();
    }

    /**
     * Almost every real collection file carries neither property — 151 of the 152 in
     * {@code unreleased/collections/}, e.g. {@code baskerville}. Absent must fail safe.
     */
    @Test
    public void defaultsToUnreleasedDraftWhenTheFieldsAreAbsent() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "baskerville", "Baskerville");
        File datasetFile = writeDataset(tempDir, "baskerville");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", null);

        assertThat(dao.getCollection("baskerville").isReleased()).isFalse();
        assertThat(dao.getCollection("baskerville").isUnreleased()).isTrue();
        assertThat(dao.getCollection("baskerville").getStatus()).isEqualTo("draft");
    }

    /** An unrecognised status is carried through; only the wording has to degrade. */
    @Test
    public void carriesAnUnrecognisedStatusThrough() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        Path collectionsDir = tempDir.resolve("collections");
        Files.createDirectories(collectionsDir);
        Files.write(collectionsDir.resolve("embargoed.collection.json"),
            collectionJson("embargoed", "Embargoed", "\"isReleased\":false,\"status\":\"embargoed\",")
                .getBytes(StandardCharsets.UTF_8));
        File datasetFile = writeDataset(tempDir, "embargoed");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", null);

        assertThat(dao.getCollection("embargoed").isUnreleased()).isTrue();
        assertThat(dao.getCollection("embargoed").getStatus()).isEqualTo("embargoed");
    }
}
