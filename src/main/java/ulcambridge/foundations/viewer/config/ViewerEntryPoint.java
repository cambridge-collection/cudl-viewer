package ulcambridge.foundations.viewer.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Responsible for bootstrapping the CUDL Viewer webapp.
 *
 * This registers the main Spring dispatcher servlet with the servlet container,
 * which in turn
 */
public class ViewerEntryPoint
    extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    public void onStartup(ServletContext container) throws ServletException {
        // Register a root context and dispatch servlet with sub-context
        super.onStartup(container);

        container.getJspConfigDescriptor();

        // Register a filter proxy to be used by @EnableOAuth2Client
        registerProxyFilter(container, "oauth2ClientContextFilter");
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
            AppConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
            DispatchServletConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{
            "/"
        };
    }

    private void registerProxyFilter(
        ServletContext servletContext, String name, String...patterns) {

            DelegatingFilterProxy filter = new DelegatingFilterProxy(name);
            filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
            servletContext
                .addFilter(name, filter)
                .addMappingForUrlPatterns(null, false, patterns);
    }

    private void registerProxyFilter(
        ServletContext servletContext, String name) {

        registerProxyFilter(servletContext, name, "/*");
    }
}
