package ulcambridge.foundations.viewer.utils;

import com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class Utils {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss z";
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final Locale LOCALE = Locale.UK;
    private static final ThreadLocal<DateFormat> DATE_FORMATS =
            new ThreadLocal<DateFormat>();

    /**
     * @return a DateFormat instance safe for the current thread to use.
     */
    private static final DateFormat getDateFormat() {
        DateFormat f = DATE_FORMATS.get();

        if(f == null) {
            f = new SimpleDateFormat(DATE_PATTERN, LOCALE);
            DATE_FORMATS.set(f);
        }

        // It's critical that we reset the state of the date format objects as
        // they change their calendar's timezone in response to parse()
        // operations. Fantastic API design. Should probably just use joda time
        // to avoid this bs.
        return resetDateFormat(f);
    }

    /**
     * Reset state of a DateFormat which may have been changed by parse
     * operations.
     */
    private static DateFormat resetDateFormat(DateFormat f) {
        f.setTimeZone(TIME_ZONE);
        return f;
    }

    public static final String formatDate(Date date) {
        return getDateFormat().format(date);
    }

    public static Date parseDate(String date) throws ParseException {
        return getDateFormat().parse(date);
    }

    // get current date time
    public static Date getCurrentDateTime() {
        return new Date();
    }

    // format a double to 5 digit precision
    public static String formatValue(double value) {
        value = (double) Math.round(value * 100000) / 100000;
        return new DecimalFormat("#0.00000").format(value);
    }

    public static <T> Stream<T> stream(Iterable<T> i) {
        return StreamSupport.stream(i.spliterator(), false);
    }

    public static UriComponentsBuilder populateScheme(
        UriComponentsBuilder b, HttpServletRequest request) {

        return b.scheme(request.isSecure() ? "https" : "http");
    }

    public static Map<String, ?> atomicValues(JSONObject obj) {
        ImmutableMap.Builder<String, Object> values = ImmutableMap.builder();
        for(String key : obj.keySet()) {
            Object value = obj.get(key);
            if(!(value instanceof JSONObject || value instanceof JSONArray)) {
                values.put(key, value);
            }
        }
        return values.build();
    }

    /**
     * Give absolute URLs a path of / if they have no path.
     *
     * <p></p>The {@link URI#resolve(URI)} method produces a broken URL if you do
     * {@code URI.create("http://foo").resolve("bar")}. This results in {@code "http://foobar"} not
     * {@code "http://foo/bar"}.
     */
    public static URI ensureURLHasPath(URI url) {
        if(!url.isOpaque() && url.isAbsolute() && url.getPath().equals("")) {
            return url.resolve("/");
        }
        return url;
    }
}
