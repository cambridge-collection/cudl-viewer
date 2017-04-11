package ulcambridge.foundations.viewer.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import uk.ac.cam.lib.web.frontend.BuildFactory;
import uk.ac.cam.lib.web.frontend.FrontEndBuild;
import uk.ac.cam.lib.web.frontend.jsp.tag.SpringTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class FrontEndResourcesTag extends SpringTag {

    @Autowired
    private BuildFactory buildFactory;

    private PageType pageType;

    public void setPagetype(String pageType) {
        this.pageType = PageType.valueOf(pageType);
    }

    @Override
    public void doTagAutowired() throws JspException, IOException {
        Assert.notNull(this.buildFactory);
        Assert.notNull(this.pageType);

        final JspWriter out = this.getJspContext().getOut();
        for(final FrontEndBuild.Resource resource :
                this.buildFactory.getBuild(this.pageType.getChunkName()).resources()) {
            out.write(resource.render());
        }
    }
}
