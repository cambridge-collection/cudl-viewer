package ulcambridge.foundations.frontend.webpack;

/**
 * Created by hal on 05/10/15.
 */
public class WebpackMetadataException extends Exception {
    public WebpackMetadataException() {
        super();
    }

    public WebpackMetadataException(String message) {
        super(message);
    }

    public WebpackMetadataException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebpackMetadataException(Throwable cause) {
        super(cause);
    }
}
