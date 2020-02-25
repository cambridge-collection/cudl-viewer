package ulcambridge.foundations.viewer.dao.items.huwiiifdataworkaround;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import ulcambridge.foundations.viewer.dao.DecoratedItemFactory;

public final class ImageURLResolution {
    private ImageURLResolution() {}

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
