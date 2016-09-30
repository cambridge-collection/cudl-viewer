package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class AbstractRequestCacheWrapper implements RequestCache {

    protected final RequestCache target;

    public AbstractRequestCacheWrapper(RequestCache target) {
        Assert.notNull(target);

        this.target = target;
    }

    @Override
    public void saveRequest(HttpServletRequest request,
                            HttpServletResponse response) {
        target.saveRequest(request, response);
    }

    @Override
    public SavedRequest getRequest(HttpServletRequest request,
                                   HttpServletResponse response) {
        return target.getRequest(request, response);
    }

    @Override
    public HttpServletRequest getMatchingRequest(HttpServletRequest request,
                                                 HttpServletResponse response) {
        return target.getMatchingRequest(request, response);
    }

    @Override
    public void removeRequest(HttpServletRequest request,
                              HttpServletResponse response) {
        target.removeRequest(request, response);
    }
}
