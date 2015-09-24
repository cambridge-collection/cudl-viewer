package ulcambridge.foundations.viewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDao;

import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Properties;

@Controller
public class SiteViewController {

    protected final Log logger = LogFactory.getLog(getClass());
    private String showHoldingPage = Properties.getString("showHoldingPage");
    private ItemFactory itemFactory;
    private CollectionFactory collectionFactory;

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    // on path /
    @RequestMapping(value = "/")
    public ModelAndView handleRequest() {
        
        if (showHoldingPage != null && showHoldingPage.equals("true")) {
            ModelAndView modelAndView = new ModelAndView(
                    "jsp/errors/holdingpage");
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("jsp/index");

        modelAndView.addObject("downtimeWarning",
                Properties.getString("downtimeWarning"));

        DecimalFormat formatter = new DecimalFormat("###,###,###");

        modelAndView
                .addObject("itemCount", formatter.format(this.collectionFactory
                                .getAllItemIds().size()));

        List<Collection> rootCollections = this.collectionFactory
                .getRootCollections();
		// Collections.shuffle(allCollections); // shuffle to randomise
        // collection order.
        modelAndView.addObject("rootCollections", rootCollections);

        return modelAndView;
    }

    // on path /news/
    @RequestMapping(value = "/news")
    public ModelAndView handleNewsRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/news");
        return modelAndView;
    }

    // on path /about/
    @RequestMapping(value = "/about")
    public ModelAndView handleAboutRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/about");
        return modelAndView;
    }

    // on path /help/
    @RequestMapping(value = "/help")
    public ModelAndView handleHelpRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/help");
        return modelAndView;
    }

    // on path /terms/
    @RequestMapping(value = "/terms")
    public ModelAndView handleTermsRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/terms");
        return modelAndView;
    }

    // on path /contributors/
    @RequestMapping(value = "/contributors")
    public ModelAndView handleContributorsRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/contributors");
        return modelAndView;
    }

    // on path /nojavascript
    @RequestMapping(value = "/nojavascript")
    public ModelAndView handleNoJavascriptRequest(
            @RequestParam("url") String url) {

        ModelAndView modelAndView = new ModelAndView("jsp/errors/nojavascript");
        modelAndView.addObject("requestURL", url);
        return modelAndView;
    }

    // on path /errors/404.html
    @RequestMapping(value = "/errors/404.html")
    public ModelAndView handle404() {

        ModelAndView modelAndView = new ModelAndView("jsp/errors/404");
        return modelAndView;
    }

    // on path /errors/500.html
    @RequestMapping(value = "/errors/500.html")
    public ModelAndView handle500() {

        ModelAndView modelAndView = new ModelAndView("jsp/errors/500");
        return modelAndView;
    }

    // on path /robots.txt
    @RequestMapping(value = "/robots.txt")
    public ModelAndView handleRobots(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String baseURL = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(Properties.getString("robots.useragent"));
        out.println(Properties.getString("robots.disallow"));
        out.println("Sitemap: " + baseURL + "sitemap.xml");
        out.close();
        return null;
    }

    // on path /sitemap.xml
    @RequestMapping(value = "/sitemap.xml")
    public ModelAndView handleSiteMap(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/xml");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" ");
        out.println("xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" ");
        out.println("xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\"> ");

        Set<String> ids = collectionFactory.getAllItemIds();

        for (String itemId : ids) {

            String itemBaseURL = String.format("%s://%s:%d/view/", request.getScheme(), request.getServerName(), request.getServerPort());

            out.println("<url><loc>");
            out.print(itemBaseURL + itemId);
            out.println("</loc>");
            out.println("</url>");

        }
        out.println("</urlset>");

        out.flush();
        out.close();
        return null;
    }
}
