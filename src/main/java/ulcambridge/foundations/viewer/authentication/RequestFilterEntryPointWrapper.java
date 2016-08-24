package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class RequestFilterEntryPointWrapper implements AuthenticationEntryPoint {

    public static class Builder {
        private final List<RequestFilter> requestFilters;

        public Builder() {
            this.requestFilters = new ArrayList<>();
        }

        public Builder withFilter(RequestFilter filter) {
            requestFilters.add(filter);
            return this;
        }

        public RequestFilterEntryPointWrapper build(
            AuthenticationEntryPoint entryPoint) {

            return new RequestFilterEntryPointWrapper(
                entryPoint, this.requestFilters);
        }
    }

    private final AuthenticationEntryPoint wrapped;
    private final List<RequestFilter> filters;

    RequestFilterEntryPointWrapper(
        AuthenticationEntryPoint wrapped, Iterable<RequestFilter> filters) {

        Assert.notNull(filters);
        Assert.notNull(wrapped);

        this.wrapped = wrapped;
        this.filters = Collections.unmodifiableList(
            Utils.stream(filters)
            .peek(Assert::notNull)
            .collect(Collectors.toList()));
    }

    public List<RequestFilter> getFilters() {
        return this.filters;
    }

    @Override
    public void commence(
        HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException)
        throws IOException, ServletException {

        this.wrapped.commence(this.filterRequest(request, response), response,
                              authException);
    }

    protected HttpServletRequest filterRequest(
        HttpServletRequest req, HttpServletResponse resp) {

        for(RequestFilter rf : this.getFilters())
            req = rf.filter(req, resp);

        return req;
    }

    public interface RequestFilter {
        HttpServletRequest filter(
            HttpServletRequest req, HttpServletResponse resp);
    }
}
