package ulcambridge.foundations.viewer.config;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
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

    public static final String RAVEN_SERVLET_NAME = "raven";
    public static final String RAVEN_SERVLET_MAPPING = "/raven/*";

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        // Register a root context and dispatch servlet with sub-context
        super.onStartup(container);

        // In addition we need to register a second dispatch servlet to house
        // the (temporary) raven auth servlet.
        registerRavenServlet(container);

        container.getJspConfigDescriptor();
    }

    void registerRavenServlet(ServletContext container) {
        AnnotationConfigWebApplicationContext ravenContext =
            new AnnotationConfigWebApplicationContext();
        ravenContext.register(RavenServletConfig.class);

        ServletRegistration.Dynamic ravenServlet = container.addServlet(
            RAVEN_SERVLET_NAME, new DispatcherServlet(ravenContext));

        ravenServlet.setLoadOnStartup(1);
        ravenServlet.addMapping(RAVEN_SERVLET_MAPPING);
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

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {

        };
    }
}
