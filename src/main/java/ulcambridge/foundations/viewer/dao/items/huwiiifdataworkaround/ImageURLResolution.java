package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;

@Import(ImageURLResolution.DziAndIiifImageURLResolution.class)
public final class ImageURLResolution {
    private ImageURLResolution() {}

    /**
     * Generate image URLs for items referencing a DZI image server, but also
     * include a reference to the item's images on a IIIF image server.
     *
     * <p>This is the current behaviour used by cudl.lib.cam.ac.uk, because our
     * IIIF image server is not production ready.</p>
     */
    @Conditional(DziAndIiifImageURLResolution.ActivationCondition.class)
    public static final class DziAndIiifImageURLResolution {
        public static final String TYPE_NAME = "dzi-and-iiif";

        public static final class ActivationCondition implements Condition {
            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                return TYPE_NAME.equals(context.getEnvironment()
                    .getProperty("images.server.type", TYPE_NAME));
            }
        }

        @Bean @Order(0)
        public static DecoratedItemFactory.ItemJSONPreProcessor dmdSectionThumbnailURLResolver() {
            return new DescriptiveMetadataImageURLResolver("thumbnailUrl", "content/images/{thumbnailUrl}_files/8/0_0.jpg");
        }

        @Bean @Order(0)
        public static DecoratedItemFactory.ItemJSONPreProcessor displayImageURLResolver() {
            return new PageImageURLResolver("displayImageURL", "content/images/{IIIFImageURL}.dzi");
        }

        @Bean @Order(0)
        public static DecoratedItemFactory.ItemJSONPreProcessor downloadImageURLResolver() {
            return new PageImageURLResolver("downloadImageURL", "content/images/{IIIFImageURL}.jpg");
        }

        @Bean @Order(0)
        public static  DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver() {
            return new PageImageURLResolver("thumbnailImageURL", "content/images/{IIIFImageURL}_files/8/0_0.jpg");
        }

        @Bean @Order(1)
        public static DecoratedItemFactory.ItemJSONPreProcessor iiifImageURLResolver() {
            return new PageImageURLResolver("IIIFImageURL", "{IIIFImageURL}.jp2");
        }
    }

    /**
     * Generate image URLs for items referencing a IIIF image server.
     */
    @Conditional(IIIFImageURLResolution.ActivationCondition.class)
    public static final class IIIFImageURLResolution {
        public static final String TYPE_NAME = "iiif";

        public static final class ActivationCondition implements Condition {
            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                return TYPE_NAME.equals(context.getEnvironment()
                    .getProperty("images.server.type", TYPE_NAME));
            }
        }

        @Bean @Order(0)
        public static DecoratedItemFactory.ItemJSONPreProcessor dmdSectionThumbnailURLResolver() {
            return new DescriptiveMetadataImageURLResolver("thumbnailUrl", "{thumbnailUrl}.jp2/full/!178,178/0/default.jpg");
        }

        @Bean @Order(0)
        public static  DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver() {
            return new PageImageURLResolver("thumbnailImageURL", "{IIIFImageURL}.jp2/full/!178,178/0/default.jpg");
        }

        @Bean @Order(1)
        public static DecoratedItemFactory.ItemJSONPreProcessor iiifImageURLResolver() {
            return new PageImageURLResolver("IIIFImageURL", "{IIIFImageURL}.jp2");
        }
    }
}
