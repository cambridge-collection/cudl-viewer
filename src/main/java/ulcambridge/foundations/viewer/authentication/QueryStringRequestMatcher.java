package ulcambridge.foundations.viewer.authentication;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class QueryStringRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        String query = request.getQueryString();

        if(query == null)
            return false;

        return matches(Arrays.stream(query.split("&"))
            .map(s -> s.split("=", 2))
            .map(kv -> ImmutablePair.of(
                decode(kv[0]),
                Optional.ofNullable(kv.length == 1 ? null : decode(kv[1])))));
    }

    protected abstract boolean matches(Stream<Map.Entry<String, Optional<String>>> query);

    private static String decode(String s) {
        return UriUtils.decode(s, "UTF-8");
    }

    public static QueryStringRequestMatcher from(
        Predicate<Stream<Map.Entry<String, Optional<String>>>> predicate) {
        return new QueryStringRequestMatcher() {
            @Override
            protected boolean matches(Stream<Map.Entry<String, Optional<String>>> query) {
                return predicate.test(query);
            }
        };
    }

    public static QueryStringRequestMatcher forKey(String key) {
        return from(s -> s.filter(e -> e.getKey().equals(key))
                          .findAny().isPresent());
    }

    public static QueryStringRequestMatcher forKeyWithValue(
        String key, String value) {

        Assert.notNull(key);
        Assert.notNull(value);

        return from(s -> s.filter(e -> e.getKey().equals(key) &&
                                       value.equals(e.getValue().orElse(null)))
                          .findAny().isPresent());
    }
}
