package ulcambridge.foundations.viewer.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// for assertions on Java 8 types (Streams and java.util.Optional)

public class FilesystemDirectoryJSONLoaderTest {
    public static final Path TEST_JSON_DIRECTORY;

    static {
        URL resourceURL = DefaultItemFactoryTest.class.getResource("/example-json/");
        Objects.requireNonNull(resourceURL, "resource directory not found");
        TEST_JSON_DIRECTORY = Paths.get(resourceURL.getPath());
    }

    private FilesystemDirectoryJSONLoader jsonLoader;

    @BeforeEach
    public void beforeEach() {
        jsonLoader = new FilesystemDirectoryJSONLoader(TEST_JSON_DIRECTORY);
    }

    @Test
    public void throwsWhenFileDoesNotExist() {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> jsonLoader.loadJSON("missing"));
        assertThat(exception).hasMessageThat().isEqualTo(String.format("JSON for id \"missing\" does not exist; nested exception is java.nio.file.NoSuchFileException: %s", TEST_JSON_DIRECTORY.resolve("missing.json")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo/bar", "../foo"})
    public void throwsWhenIDContainsInvalidPathSyntax(String invalidID) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jsonLoader.loadJSON(invalidID));
        assertThat(exception).hasMessageThat().isEqualTo(String.format("invalid id: %s", invalidID));
    }

    @Test
    public void throwsWhenFileContainsInvalidJSON() {
        Exception exception = assertThrows(DataRetrievalFailureException.class, () -> jsonLoader.loadJSON("broken"));
        assertThat(exception).hasMessageThat().startsWith(String.format("File contents is not valid JSON: %s - ", TEST_JSON_DIRECTORY.resolve("broken.json")));
    }

    @Test
    public void returnsJSONFromFile() {
        // JSONObject uses object identity for .equals()...
        assertThat(jsonLoader.loadJSON("simple").toString()).isEqualTo("{\"foo\":\"bar\"}");
    }
}
