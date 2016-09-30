package ulcambridge.foundations.viewer.authentication;


import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Represents a method for storing/retrieving a URL fragment identifier in an
 * {@link HttpServletRequest} (they have no concept of a fragment themselves).
 */
public interface HttpServletRequestFragmentStorageStrategy {

    /**
     * Get the fragment previously stored in a request via the
     * {@link #storeFragment(HttpServletRequest, String)} method of this object.
     */
    Optional<String> retrieveFragment(HttpServletRequest request);

    Optional<String> retrieveFragment(SavedRequest request);

    /***
     * Return a new request which will yield an Optional containing the provided
     * fragment when passed to the {@link #retrieveFragment(HttpServletRequest)}
     * method of this object.
     */
    HttpServletRequest storeFragment(
        HttpServletRequest request, String fragment);
}
