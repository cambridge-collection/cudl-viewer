package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A base class for SavedRequest implementations which wrap an existing
 * SavedRequest instance.
 */
public abstract class AbstractSavedRequestWrapper implements SavedRequest {

    protected final SavedRequest target;

    public AbstractSavedRequestWrapper(SavedRequest target) {
        Assert.notNull(target);

        this.target = target;
    }

    @Override
    public String getRedirectUrl() {
        return target.getRedirectUrl();
    }

    @Override
    public List<Cookie> getCookies() {
        return target.getCookies();
    }

    @Override
    public String getMethod() {
        return target.getMethod();
    }

    @Override
    public List<String> getHeaderValues(String name) {
        return target.getHeaderValues(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return target.getHeaderNames();
    }

    @Override
    public List<Locale> getLocales() {
        return target.getLocales();
    }

    @Override
    public String[] getParameterValues(String name) {
        return target.getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return target.getParameterMap();
    }
}
