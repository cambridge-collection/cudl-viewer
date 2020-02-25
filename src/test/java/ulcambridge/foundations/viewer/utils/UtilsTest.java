package ulcambridge.foundations.viewer.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;

public class UtilsTest {
    @Test
    public void getCurrentDateTime() {
        Instant now = Instant.now();
        Date currentDateTime = Utils.getCurrentDateTime();
        assertThat(currentDateTime.toInstant()).isLessThan(now.plus(1, ChronoUnit.SECONDS));
        assertThat(currentDateTime.toInstant()).isGreaterThan(now.minus(1, ChronoUnit.SECONDS));
    }

    private static final long EXAMPLE_DATE_EPOCH_SEC = 1582638470;
    private static final String EXAMPLE_DATE_STR = "2020-02-25 13:47:50 UTC";

    @Test
    public void formatDate() {
        assertThat(Utils.formatDate(new Date(EXAMPLE_DATE_EPOCH_SEC * 1000))).isEqualTo(EXAMPLE_DATE_STR);
    }

    @Test
    public void parseDate() throws ParseException {
        assertThat(Utils.parseDate(EXAMPLE_DATE_STR).toInstant().getEpochSecond()).isEqualTo(EXAMPLE_DATE_EPOCH_SEC);
    }

    @Test
    public void parseDateThrowsOnInvalidDateString() {
        Assertions.assertThrows(ParseException.class, () -> Utils.parseDate("sadf"));
    }

    @Test
    public void stream() {
        assertThat(Utils.stream(ImmutableList.of(1, 2, 3)).toArray()).asList().containsExactly(1, 2, 3);
    }

    @ParameterizedTest
    @CsvSource({
        "true,https",
        "false,http",
    })
    public void populateScheme(boolean isSecure, String scheme) {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.doReturn(isSecure).when(req).isSecure();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("//foo.com/blah");

        Utils.populateScheme(builder, req);

        assertThat(builder.build().getScheme()).isEqualTo(scheme);
    }

    @Test
    public void atomicValues() {
        ImmutableMap<String, ?> atomicValues = ImmutableMap.of("a", 42, "b", "abc", "c", true);
        ImmutableMap<String, ?> compositeValues = ImmutableMap.of("d", ImmutableMap.of(), "e", ImmutableList.of());
        ImmutableMap<String, ?> allValues = ImmutableMap.<String, Object>builder().putAll(atomicValues).putAll(compositeValues).build();
        assertThat(Utils.atomicValues(new JSONObject(allValues))).isEqualTo(atomicValues);
    }
}
