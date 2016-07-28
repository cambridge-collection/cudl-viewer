package ulcambridge.foundations.viewer.transcriptions;

/**
 * Interface for a Transcription formatter.  Implementing classes will
 * provide a method to format content, producing a version of the source that
 * has been altered as required. Used for when we don't want to display the HTML
 * of a transcription exactly as supplied to us.
 *
 * @author jennie
 *
 */
public interface TranscriptionFormatter {

    public String format(String sourceURL, String sourcePage);

}
