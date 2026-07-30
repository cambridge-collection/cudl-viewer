package ulcambridge.foundations.viewer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class CollectionFactoryTest {

    @TempDir
    Path workDir;

    private Path mainJsonDir;
    private Path unreleasedJsonDir;

    @BeforeEach
    void setUp() throws IOException {
        mainJsonDir = workDir.resolve("json");
        Files.createDirectories(mainJsonDir);
        unreleasedJsonDir = workDir.resolve("unreleased").resolve("json");
        Files.createDirectories(unreleasedJsonDir);
    }

    private CollectionsDao daoWithItems(String... itemIds) {
        return new CollectionsDao() {
            @Override
            public List<String> getCollectionIds() {
                return List.of("test-collection");
            }

            @Override
            public Collection getCollection(String id) {
                return new Collection("test-collection", "Test", new ArrayList<>(Arrays.asList(itemIds)),
                    "collections/test-collection/summary.html",
                    "collections/test-collection/sponsors.html",
                    "virtual", null, "");
            }
        };
    }

    @Test
    public void filtersOutItemsAbsentFromBothDirectories() throws IOException {
        Files.createFile(mainJsonDir.resolve("present-item.json"));

        CollectionFactory factory = new CollectionFactory(
            daoWithItems("present-item", "absent-item"), "true", mainJsonDir, false, "");

        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("present-item");
    }

    @Test
    public void retainsItemsPresentOnlyInUnreleasedDirWhenEnabled() throws IOException {
        Files.createFile(mainJsonDir.resolve("main-item.json"));
        Files.createFile(unreleasedJsonDir.resolve("unreleased-item.json"));

        String unreleasedDataDir = workDir.resolve("unreleased").toString();
        CollectionFactory factory = new CollectionFactory(
            daoWithItems("main-item", "unreleased-item"), "true", mainJsonDir,
            true, unreleasedDataDir);

        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("main-item", "unreleased-item");
    }

    @Test
    public void removesItemsPresentOnlyInUnreleasedDirWhenDisabled() throws IOException {
        Files.createFile(mainJsonDir.resolve("main-item.json"));
        Files.createFile(unreleasedJsonDir.resolve("unreleased-item.json"));

        CollectionFactory factory = new CollectionFactory(
            daoWithItems("main-item", "unreleased-item"), "true", mainJsonDir, false, "");

        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("main-item");
    }

    @Test
    public void filtersOutAbsentItemsWithCachingDisabled() throws IOException {
        Files.createFile(mainJsonDir.resolve("present-item.json"));

        CollectionFactory factory = new CollectionFactory(
            daoWithItems("present-item", "absent-item"), "false", mainJsonDir, false, "");

        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("present-item");
    }

    @Test
    public void retainsUnreleasedOnlyItemsWithCachingDisabled() throws IOException {
        Files.createFile(mainJsonDir.resolve("main-item.json"));
        Files.createFile(unreleasedJsonDir.resolve("unreleased-item.json"));

        CollectionFactory factory = new CollectionFactory(
            daoWithItems("main-item", "unreleased-item"), "false", mainJsonDir,
            true, workDir.resolve("unreleased").toString());

        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("main-item", "unreleased-item");
    }

    @Test
    public void picksUpItemsAddedAfterStartupOnlyOnceTheListingIsRefreshed() throws IOException {
        Files.createFile(mainJsonDir.resolve("main-item.json"));

        CollectionFactory factory = new CollectionFactory(
            daoWithItems("main-item", "late-item"), "false", mainJsonDir, false, "");
        Files.createFile(mainJsonDir.resolve("late-item.json"));

        // Requests filter against the cached directory listing rather than stat-ing each
        // item, so a file added since the last listing is not visible yet...
        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("main-item");

        // ...until the listing is rebuilt (startup, /refresh, or the RefreshCache poll).
        factory.init(true);
        assertThat(factory.getCollectionFromId("test-collection").getItemIds())
            .containsExactly("main-item", "late-item");
    }
}
