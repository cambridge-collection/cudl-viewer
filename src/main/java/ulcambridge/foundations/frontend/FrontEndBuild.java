package ulcambridge.foundations.frontend;

import org.springframework.util.Assert;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a collection of related frontend UI resources required in an HTML
 * document.
 */
public interface FrontEndBuild {
    List<Resource> resources();

    /**
     * Represents an individual resource, such as a CSS or Javascript file.
     */
    public interface Resource {
        /**
         * @return an HTML representation of the resource.
         */
        public String render();
    }

    /**
     * A resource included inline in a document (rather than referenced by URL).
     */
    public class InlineResource implements Resource {
        private final ResourceType type;
        private final String body;

        public InlineResource(ResourceType type, String body) {
            Assert.notNull(type);
            Assert.notNull(body);

            this.type = type;
            this.body = body;
        }

        public String render() {
            throw new RuntimeException("Not implemented");
        }
    }

    /**
     * An external resource which is referenced via URL.
     */
    public interface LinkedResource extends Resource {
        ResourceType getType();
        URI getUri();
    }

    public static abstract class AbstractLinkedResource
            implements LinkedResource {

        private static final Pattern TAG_ATTR =
                Pattern.compile("^[a-zA-Z][\\w-]*$");

        private final URI link;
        private final ResourceType type;
        protected final String tag, linkAttr;

        public AbstractLinkedResource(
                URI link, ResourceType type, String tag, String linkAttr) {

            Assert.notNull(link);
            Assert.notNull(type);
            Assert.hasText(tag);
            Assert.hasText(linkAttr);

            validateTagLinkName(tag);
            validateTagLinkName(linkAttr);

            this.link = link;
            this.type = type;
            this.tag = tag;
            this.linkAttr = linkAttr;
        }

        private static void validateTagLinkName(String name)
                throws HtmlSyntaxException {
            if(!TAG_ATTR.matcher(name).matches())
                throw new HtmlSyntaxException(
                        String.format("Invalid tag/attribute: %s", name));
        }

        @Override
        public URI getUri() {
            return this.link;
        }

        @Override
        public ResourceType getType() {
            return this.type;
        }

        protected String renderAttributes() {
            return String.format("%s=\"%s\"",
                    this.linkAttr,
                    HtmlUtils.htmlEscape(this.getUri().toString()));
        }

        @Override
        public String render() {
            return String.format("<%s %s></%1$s>",
                    this.tag, this.renderAttributes());
        }
    }

    /**
     * Represents a reference to an external CSS file.
     */
    public static class LinkedCSSResource extends AbstractLinkedResource {
        public LinkedCSSResource(URI link) {
            super(link, ResourceType.CSS, "link", "href");
        }

        @Override
        protected String renderAttributes() {
            return super.renderAttributes() + " rel=\"stylesheet\"";
        }
    }

    /**
     * Represents a reference to an external Javascript file.
     */
    public class LinkedJavascriptResource extends AbstractLinkedResource {
        private final boolean isAsync;

        public LinkedJavascriptResource(URI link) {
            this(link, false);
        }

        public LinkedJavascriptResource(URI link, boolean isAsync)
            throws HtmlSyntaxException{
            super(link, ResourceType.JAVASCRIPT, "script", "src");

            this.isAsync = isAsync;
        }

        public boolean isAsync() {
            return this.isAsync;
        }

        @Override
        protected String renderAttributes() {
            String async = this.isAsync ? "async=\"true\"" : "";

            return String.format("%s type=\"%s\"%s",
                    super.renderAttributes(),
                    HtmlUtils.htmlEscape(this.getType().getMime()), async);
        }
    }

    /**
     * The types of resources available.
     */
    enum ResourceType {
        JAVASCRIPT("text/javascript"), CSS("text/css");

        private final String mime;

        ResourceType(String mime) {
            Assert.hasText(mime);
            this.mime = mime;
        }

        String getMime() {
            return this.mime;
        }

        @Override
        public String toString() {
            return this.getMime();
        }
    }

    public static class HtmlSyntaxException extends RuntimeException {
        public HtmlSyntaxException() {
            super();
        }

        public HtmlSyntaxException(String message) {
            super(message);
        }

        public HtmlSyntaxException(String message, Throwable cause) {
            super(message, cause);
        }

        public HtmlSyntaxException(Throwable cause) {
            super(cause);
        }
    }
}
