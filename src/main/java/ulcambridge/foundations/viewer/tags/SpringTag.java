package ulcambridge.foundations.viewer.tags;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * A JSP custom tag base class which resolves Spring injection annotations.
 *
 * This allows spring beans to participate in custom JSP tags.
 */
public abstract class SpringTag extends SimpleTagSupport {

    /**
     * Injects autowired dependencies using the context provided by
     * {@link #getWebApplicationContext()}.
     */
    protected void autowireSelf() {
        this.getWebApplicationContext()
                .getAutowireCapableBeanFactory()
                .autowireBean(this);
    }

    /**
     * Gets the application context to autowire with.
     *
     * This implementation returns the root web application context rather than
     * a servlet specific context.
     */
    protected WebApplicationContext getWebApplicationContext() {
        PageContext ctx = (PageContext)this.getJspContext();
        return WebApplicationContextUtils.getWebApplicationContext(
                ctx.getServletContext());
    }

    @Override
    public final void doTag() throws JspException, IOException {
        this.autowireSelf();
        this.doTagAutowired();
    }

    /**
     * Called to generate the tag content once autowired dependencies have been
     * loaded.
     */
    public abstract void doTagAutowired() throws JspException, IOException;
}
