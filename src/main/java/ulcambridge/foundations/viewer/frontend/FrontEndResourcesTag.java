package ulcambridge.foundations.viewer.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import ulcambridge.foundations.frontend.FrontEndBuild.Resource;
import ulcambridge.foundations.viewer.tags.SpringTag;

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

        JspWriter out = this.getJspContext().getOut();
        for(Resource resource :
                this.buildFactory.getBuild(this.pageType).resources()) {
            out.write(resource.render());
        }
    }
}
