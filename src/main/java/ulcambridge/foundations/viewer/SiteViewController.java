package ulcambridge.foundations.viewer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.admin.RefreshCache;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Properties;
import ulcambridge.foundations.viewer.utils.MiradorUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

@Controller
public class SiteViewController {
    private String showHoldingPage = Properties.getString("showHoldingPage");
    private final CollectionFactory collectionFactory;
    private static final ModelAndView FOUR_OH_FOUR_MAV = new ModelAndView("jsp/errors/404");
    private final String contentHtmlUrl;
    private final RefreshCache cacheRefresher;

    @Autowired
    public SiteViewController(CollectionFactory collectionFactory,
                              @Value("${cudl-viewer-content.html.path}") String contentHtmlPath,
                              RefreshCache cacheRefresher) {
        Assert.notNull(collectionFactory);
        Assert.notNull(contentHtmlPath, "cudl-viewer-content.html.path is required");

        this.collectionFactory = collectionFactory;
        this.contentHtmlUrl = Paths.get(contentHtmlPath).toUri().toString();
        this.cacheRefresher = cacheRefresher;
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
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);

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
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    // on path /about/
    @RequestMapping(value = "/about")
    public ModelAndView handleAboutRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/about");
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    // on path /dl-platform/
    @RequestMapping(value = "/about-dl-platform")
    public ModelAndView handleDLPlatformRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/about-dl-platform");
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    // on path /help/
    @RequestMapping(value = "/help")
    public ModelAndView handleHelpRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/help");
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    // on path /mirador/
    @RequestMapping(value = "/mirador/{id}/{pagenum}")
    public ModelAndView handleMiradorRequest(@PathVariable("id") String id,
            @PathVariable("pagenum") int pagenum, HttpServletRequest request) {

        String baseURL = request.getScheme() + "://" + request.getServerName();
        if (!(request.getServerPort()==443)&&!(request.getServerPort()==80)) {
            baseURL+= ":" + request.getServerPort();
        }

        // Show page 1 if anything below 1 is requested.
        if (pagenum<1) { pagenum=1; }

        String manifestId = baseURL+"/iiif/"+id;
        String canvasId = manifestId + "/canvas/" +pagenum;

        MiradorUtils miradorUtils = new MiradorUtils(id, manifestId, canvasId);

        ModelAndView modelAndView = new ModelAndView("jsp/mirador");
        modelAndView.addObject("id", id);
        modelAndView.addObject("pagenum", pagenum);
        modelAndView.addObject("baseURL", baseURL);
        modelAndView.addObject("miradorUtils", miradorUtils);
        return modelAndView;
    }

    // Enable from properties. Should only be enabled on staging / dev
    @RequestMapping(value = "/refresh", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String handleRefreshRequest() {

        if (Properties.getString("enable.refresh").toLowerCase().equals("true")) {
            this.cacheRefresher.refreshDB();
            this.cacheRefresher.refreshJSON();

            return "{ \"refresh_successful\":true }";
        } else {
            return "{ \"refresh_successful\":false }";
        }
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
        modelAndView.addObject("contentHTMLURL", contentHtmlUrl);
        return modelAndView;
    }

    // on path /errors/404.html
    @RequestMapping(value = "/errors/404.html")
    public ModelAndView handle404() {
        return FOUR_OH_FOUR_MAV;
    }

    private String formatThrowable(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private Throwable getErrorException(HttpServletRequest request) {
        Object exc = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if(exc == null)
            return null;

        // Should always be the case
        assert exc instanceof Throwable;
        return (Throwable)exc;
    }

    // on path /errors/500.html
    @RequestMapping(value = "/errors/500.html")
    public ModelAndView handle500(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("jsp/errors/500");

        Throwable t = this.getErrorException(request);
        if(t != null) {
            mv.addObject("exception", t);
        }

        return mv;
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
