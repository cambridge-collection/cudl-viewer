package ulcambridge.foundations.viewer;

import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.mock.env.MockEnvironment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;

/**
 * default-context.tag publishes showReleaseStatus into the page's data-context,
 * where the UI's badge renderers gate on it. json:property renders a String value as a
 * JSON string and "false" is truthy in Javascript, so the flag has to reach the JSON as
 * a boolean or the gate silently does nothing. This evaluates the expression the tag
 * actually ships rather than a copy of it, so dropping the type from the tag fails here.
 */
public class DefaultContextTagTest {

    private static final Path TAG_FILE =
        Path.of("src/main/webapp/WEB-INF/tags/default-context.tag");

    private static final Pattern EVAL_EXPRESSION =
        Pattern.compile("<spring:eval\\b[^>]*\\bexpression=\"([^\"]+)\"");

    @Test
    public void flagIsBooleanTrueWhenEnabled() throws IOException {
        assertThat(evaluate("true")).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void flagIsBooleanFalseWhenDisabled() throws IOException {
        assertThat(evaluate("false")).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void flagDefaultsToFalseWhenAbsent() throws IOException {
        assertThat(evaluate(null)).isEqualTo(Boolean.FALSE);
    }

    private static Object evaluate(String propertyValue) throws IOException {
        MockEnvironment environment = new MockEnvironment();
        if (propertyValue != null) {
            environment.setProperty("showReleaseStatus", propertyValue);
        }

        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver((context, beanName) -> {
            assertThat(beanName).isEqualTo("environment");
            return environment;
        });

        return new SpelExpressionParser()
            .parseExpression(tagExpression())
            .getValue(evaluationContext);
    }

    private static String tagExpression() throws IOException {
        Matcher matcher = EVAL_EXPRESSION.matcher(
            Files.readString(TAG_FILE, StandardCharsets.UTF_8));
        assertThat(matcher.find()).isTrue();
        return matcher.group(1);
    }
}
