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

    private String collectionJson(String slug, String title) {
        return "{\"name\":{\"url-slug\":\"" + slug + "\",\"full\":\"" + title + "\"}," +
            "\"description\":{\"full\":{\"@id\":\"collections/" + slug + "/summary.html\"}," +
            "\"medium\":\"" + title + " description\"}," +
            "\"credit\":{\"prose\":{\"@id\":\"collections/" + slug + "/sponsors.html\"}}," +
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

    @Test
    public void skipsCollectionFilesMissingFromDiskAndLoadsRemainder() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "present", "Present Title");
        File datasetFile = writeDataset(tempDir, "present", "missing");

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", false, null);

        List<String> ids = dao.getCollectionIds();
        assertThat(ids).containsExactly("present");
    }

    @Test
    public void loadsUnreleasedCollectionsWhenEnabled() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "main-collection", "Main Title");
        File datasetFile = writeDataset(tempDir, "main-collection");

        Path unreleasedDir = tempDir.resolve("unreleased/collections");
        Files.createDirectories(unreleasedDir);
        Files.write(unreleasedDir.resolve("unreleased-collection.collection.json"),
            collectionJson("unreleased-collection", "Unreleased Title").getBytes(StandardCharsets.UTF_8));

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", true, unreleasedDir);

        assertThat(dao.getCollectionIds()).containsExactly("main-collection", "unreleased-collection");
    }

    @Test
    public void doesNotLoadUnreleasedCollectionsWhenDisabled() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "main-collection", "Main Title");
        File datasetFile = writeDataset(tempDir, "main-collection");

        Path unreleasedDir = tempDir.resolve("unreleased/collections");
        Files.createDirectories(unreleasedDir);
        Files.write(unreleasedDir.resolve("unreleased-collection.collection.json"),
            collectionJson("unreleased-collection", "Unreleased Title").getBytes(StandardCharsets.UTF_8));

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", false, null);

        assertThat(dao.getCollectionIds()).containsExactly("main-collection");
    }

    @Test
    public void deduplicatesCollectionIdsWithMainTakingPrecedence() throws IOException {
        Path uiFile = writeUiFile(tempDir);
        writeCollection(tempDir, "shared", "Main Title");
        File datasetFile = writeDataset(tempDir, "shared");

        Path unreleasedDir = tempDir.resolve("unreleased/collections");
        Files.createDirectories(unreleasedDir);
        Files.write(unreleasedDir.resolve("shared.collection.json"),
            collectionJson("shared", "Unreleased Title").getBytes(StandardCharsets.UTF_8));

        CollectionsJSONDao dao = new CollectionsJSONDao(
            datasetFile, uiFile.toString(), "true", true, unreleasedDir);

        assertThat(dao.getCollectionIds()).containsExactly("shared");
        assertThat(dao.getCollection("shared").getTitle()).isEqualTo("Main Title");
    }
}
