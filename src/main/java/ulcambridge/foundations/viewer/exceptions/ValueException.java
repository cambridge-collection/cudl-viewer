package ulcambridge.foundations.viewer.exceptions;

/**
 * Exception thrown when a value is invalid or illegal in some way.
 */
public class ValueException extends RuntimeException {
    public ValueException() {
    }

    public ValueException(String message) {
        super(message);
    }

    public ValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueException(Throwable cause) {
        super(cause);
    }

    public ValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
