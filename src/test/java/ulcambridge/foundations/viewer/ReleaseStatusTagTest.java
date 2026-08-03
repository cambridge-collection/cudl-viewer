package ulcambridge.foundations.viewer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

/**
 * release-status.tag is the only place the unreleased badge and notice are worded or
 * gated. A page that inlines its own copy drifts silently; this catches that.
 */
public class ReleaseStatusTagTest {

    private static final Path WEB_INF = Path.of("src/main/webapp/WEB-INF");
    private static final Path TAG = WEB_INF.resolve("tags/release-status.tag");

    /** The one other legitimate reader: it publishes the flag to the client. */
    private static final Path CONTEXT_TAG = WEB_INF.resolve("tags/default-context.tag");

    @Test
    public void theMarkupAndWordingLiveOnlyInTheTag() throws IOException {
        assertThat(filesContaining("badge bg-warning")).containsExactly(TAG);
        assertThat(filesContaining("not yet publicly available")).containsExactly(TAG);
    }

    @Test
    public void theGateLivesOnlyInTheTag() throws IOException {
        assertThat(filesContaining("showReleaseStatus"))
            .containsExactly(TAG, CONTEXT_TAG);
    }

    private static List<Path> filesContaining(String text) throws IOException {
        try (Stream<Path> files = Files.walk(WEB_INF)) {
            return files
                .filter(path -> path.toString().endsWith(".jsp") || path.toString().endsWith(".tag"))
                .filter(path -> read(path).contains(text))
                .collect(Collectors.toList());
        }
    }

    private static String read(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
