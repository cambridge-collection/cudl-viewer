package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Optional;

/**
 * A {@link RequestCache} which is capable of storing redirects to URLs with
 * fragments. The built-in Spring Security request saving machinery can't do
 * this because it's built around {@link HttpServletRequest} objects, which
 * don't know about fragments.
 *
 * <p>A {@link HttpServletRequestFragmentStorageStrategy} is used to retrieve a
 * fragment from the saved request. If a fragment is retrieved, it's added to
 * the URL returned by {@link SavedRequest#getRedirectUrl()} method of saved
 * requests returned by
 * {@link #getRequest(HttpServletRequest, HttpServletResponse)}.
 */
public class FragmentAwareRequestCache extends AbstractRequestCacheWrapper {

    private final HttpServletRequestFragmentStorageStrategy
        fragmentStorageStrategy;

    public FragmentAwareRequestCache(
        RequestCache target,
        HttpServletRequestFragmentStorageStrategy fragmentStorageStrategy) {

        super(target);

        Assert.notNull(fragmentStorageStrategy);
        this.fragmentStorageStrategy = fragmentStorageStrategy;
    }

    @Override
    public SavedRequest getRequest(HttpServletRequest request,
                                   HttpServletResponse response) {

        return new FragmentAwareSavedRequest(
            this.target.getRequest(request, response),
            this.fragmentStorageStrategy);
    }

    private static class FragmentAwareSavedRequest
        extends AbstractSavedRequestWrapper {

        private final HttpServletRequestFragmentStorageStrategy
            fragmentStorageStrategy;

        public FragmentAwareSavedRequest(
            SavedRequest target,
            HttpServletRequestFragmentStorageStrategy fragmentStorageStrategy) {

            super(target);

            this.fragmentStorageStrategy = fragmentStorageStrategy;
        }

        private Optional<String> getSavedFragment() {
            return fragmentStorageStrategy.retrieveFragment(this);
        }

        private static String encodeFragment(String frag) {
            try {
                return "#" + UriUtils.encodeFragment(frag, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public String getRedirectUrl() {
            String redirectUrl = super.getRedirectUrl();

            return this.getSavedFragment()
                .map(FragmentAwareSavedRequest::encodeFragment)
                .map(frag -> URI.create(redirectUrl).resolve(frag))
                .map(Object::toString)
                .orElse(redirectUrl);
        }
    }
}
