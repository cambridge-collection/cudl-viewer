package ulcambridge.foundations.viewer.model;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class PropertiesTest {
    @Test
    public void testProperties() {
        assertThat(Properties.getString("undefined.key")).isNull();
        assertThat(Properties.getString("rootURL")).isEqualTo("http://digital.library.example.com");
    }
}
