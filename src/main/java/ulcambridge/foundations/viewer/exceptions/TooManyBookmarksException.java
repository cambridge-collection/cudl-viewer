package ulcambridge.foundations.viewer.exceptions;

public class TooManyBookmarksException extends Exception {


    /**
     *
     */
    private static final long serialVersionUID = -3909713742410068391L;

    public TooManyBookmarksException(String msg) {
        super(msg);
    }

    public TooManyBookmarksException(String msg, Throwable t) {
        super(msg, t);
    }

    public TooManyBookmarksException(Throwable cause) {
        super(cause);
    }
}
