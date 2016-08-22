package ulcambridge.foundations.viewer.authentication;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implements the username obfuscation used when storing usernames/user IDs in
 * the CUDL database.
 */
public final class Obfuscation {

    /**
     * Obfuscate a username/user ID into the CUDL format.
     *
     * <p>The returned string is of the form {@code <type>:<hash>} where {@code <hash>} is
     * the hex-encoded sha256 hash of the username, and {@code <type>} is the
     * value of the type argument.
     */
    public static String obfuscateUsername(String type, String username) {
        // Note that this practice does not provide any significant level of
        // privacy for users as the sha256 of a typical CRSID can be brute
        // forced in seconds, and a furthermore, a rainbow table can be created.
        return String.format("%s:%s", type, sha256(username));
    }

    public static String obfuscate(String input) {
        return sha256(input);
    }

    private static String sha256(String s) {
        MessageDigest d;
        try {
            d = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            // Should never occur
            throw new RuntimeException("SHA-256 not supported", e);
        }

        return Hex.encodeHexString(d.digest(s.getBytes(Charsets.UTF_8)));
    }

    private Obfuscation() { throw new RuntimeException(); }
}
