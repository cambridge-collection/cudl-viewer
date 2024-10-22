//package ulcambridge.foundations.viewer.utils;
//
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.util.Assert;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public class RequestMatcherFilterFilter implements Filter {
//
//    private final RequestMatcher requestMatcher;
//    private final Filter filteredFilter;
//
//    public RequestMatcherFilterFilter(
//        RequestMatcher requestMatcher, Filter filteredFilter) {
//
//        Assert.notNull(requestMatcher);
//        Assert.notNull(filteredFilter);
//
//        this.requestMatcher = requestMatcher;
//        this.filteredFilter = filteredFilter;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException { }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain)
//        throws IOException, ServletException {
//
//        if(requestMatcher.matches((HttpServletRequest)request))
//            this.filteredFilter.doFilter(request, response, chain);
//        else
//            chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() { }
//}
