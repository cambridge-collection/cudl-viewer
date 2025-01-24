package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;

@Component
@PropertySource(value = "classpath:/ulcambridge/foundations/viewer/test-defaults.properties", ignoreResourceNotFound=true)
@PropertySource(value = "classpath:cudl-global.properties", ignoreResourceNotFound=true)
@PropertySource("classpath:application.properties")
public final class ImageURLResolution {
    private ImageURLResolution() {}

    @Bean
    @Qualifier("appendToThumbnail")
    @Order(0)
    public String getAppendToThumbnail(@Value("${appendToThumbnail}") String appendToThumbnail) {
        return appendToThumbnail;
    }

    @Bean
    @Qualifier("appendToImage")
    @Order(0)
    public String appendToImage(@Value("${appendToImage}") String appendToImage) {
        return appendToImage;
    }

    @Bean @Order(0)
    public static DecoratedItemFactory.ItemJSONPreProcessor dmdSectionThumbnailURLResolver(@Autowired @Qualifier("appendToThumbnail") String appendToThumbnail) {
        return new DescriptiveMetadataImageURLResolver("thumbnailUrl", "{thumbnailUrl}"+appendToThumbnail);
    }

    @Bean @Order(0)
    public static DecoratedItemFactory.ItemJSONPreProcessor displayImageURLResolver() {
        return new PageImageURLResolver("displayImageURL", "content/images/{IIIFImageURL}");
    }

    @Bean @Order(0)
    public static DecoratedItemFactory.ItemJSONPreProcessor downloadImageURLResolver() {
        return new PageImageURLResolver("downloadImageURL", "{IIIFImageURL}");
    }

    @Bean @Order(0)
    public static  DecoratedItemFactory.ItemJSONPreProcessor thumbnailImageURLResolver(@Autowired @Qualifier("appendToThumbnail") String appendToThumbnail) {
        return new PageImageURLResolver("thumbnailImageURL", "{IIIFImageURL}"+appendToThumbnail);
    }

    @Bean @Order(1)
    public static DecoratedItemFactory.ItemJSONPreProcessor iiifImageURLResolver(@Autowired @Qualifier("appendToImage") String appendToImage) {
        return new PageImageURLResolver("IIIFImageURL", "{IIIFImageURL}"+appendToImage);
    }
}
