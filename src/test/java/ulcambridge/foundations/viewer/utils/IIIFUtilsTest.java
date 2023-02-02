package ulcambridge.foundations.viewer.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class IIIFUtilsTest {

    @ParameterizedTest
    @CsvSource({
        "4643,5715,500,1000, '/892,0,2858,5715/500,1000/0/default.jpg'", // Portrait original, portrait output
        "4643,5715,1200,630, '/0,1638,4643,2438/1200,630/0/default.jpg'", // Portrait original, landscape output
        "464,571,1200,630, '/0,163,464,244/1200,630/0/default.jpg'", // Small portrait original, large landscape output
        "464,571,1200,630, '/0,163,464,244/1200,630/0/default.jpg'", // Small portrait original, large landscape output
        "13397,2420,1200,630, '/4393,0,4610,2420/1200,630/0/default.jpg'", // Landscape original, landscape output
        "13397,2420,500,1000, '/6093,0,1210,2420/500,1000/0/default.jpg'", // Landscape original, portrait output
        "4789,4789,500,1000, '/1197,0,2395,4789/500,1000/0/default.jpg'", // Square original, portrait output
        "4789,4789,1200,630, '/0,1137,4789,2514/1200,630/0/default.jpg'", // Square original, landscape output
        "4789,4789,1000,1000, '/0,0,4789,4789/1000,1000/0/default.jpg'", // Square original, square output
    })
    public void getCentreCutIIIFRequestParams(int imgWidth, int imgHeight, int reqWidth, int reqHeight,
                                              String expectedRequestParams) {
        Assertions.assertEquals(expectedRequestParams,
            IIIFUtils.getCentreCutIIIFRequestParams(imgWidth, imgHeight, reqWidth, reqHeight));

    }


}
