package ulcambridge.foundations.viewer.utils;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * Class for utilities related to calling IIIF APIs.
 *
 */
public class IIIFUtils {

    /**
     * Calculate the IIIF request parameters needed to make a maximal centre cut of an original image
     * with the output image sized to match requested dimensions.
     *
     * @param imgWidth Original image width
     * @param imgHeight Original image height
     * @param reqWidth Requested image width
     * @param reqHeight Requested image height
     * @return The IIIF Image API 2.0 URL request parameters
     */
    public static String getCentreCutIIIFRequestParams(int imgWidth, int imgHeight, int reqWidth, int reqHeight) {
        int regionX = 0, regionY = 0, regionW, regionH, sizeW, sizeH;

        sizeW = reqWidth;
        sizeH = reqHeight;

        float imgRatio = (float) imgHeight / imgWidth;
        float reqRatio = (float) reqHeight / reqWidth;

        if (imgRatio > reqRatio) {
            // Original image is more portrait relative to requested image dimensions
            regionW = imgWidth;
            regionH = Math.round(regionW * reqRatio);
            regionY = (imgHeight - regionH) / 2;

        } else if (imgRatio < reqRatio) {
            // Original image is more landscape relative to requested image dimensions
            regionH = imgHeight;
            regionW = Math.round(regionH / reqRatio);
            regionX = (imgWidth - regionW) / 2;

        } else {
            // Both images probably square, so serve whole original image
            regionW = imgWidth;
            regionH = imgHeight;
        }

        String region = String.format("/%d,%d,%d,%d", regionX, regionY, regionW, regionH);
        String size = String.format("/%d,%d", sizeW, sizeH);

        return UriComponentsBuilder.newInstance()
            .path(region).path(size)
            .path("/0").path("/default.jpg")
            .build()
            .encode()
            .toUriString();
    }

}
