package ulcambridge.foundations.viewer.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.authentication.AdminUser;
import ulcambridge.foundations.viewer.authentication.User;
import ulcambridge.foundations.viewer.authentication.Users;
import ulcambridge.foundations.viewer.authentication.UsersDao;
import ulcambridge.foundations.viewer.model.Properties;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CollectionFactory collectionFactory;
    private final UsersDao usersDao;
    private final RefreshCache cacheRefresher;

    @Autowired
    public AdminController(CollectionFactory collectionFactory,
                           UsersDao usersDao, RefreshCache cacheRefresher) {
        Assert.notNull(collectionFactory);
        Assert.notNull(usersDao);
        Assert.notNull(cacheRefresher);

        this.collectionFactory = collectionFactory;
        this.usersDao = usersDao;
        this.cacheRefresher = cacheRefresher;
    }

    // on path /admin/publishdb
    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/publishdb", method = RequestMethod.POST)
    public ModelAndView handlePublishDbRequest(HttpSession session) throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");

        // Setup Git
        String localPathMasters = Properties.getString("admin.git.db.localpath");
        String username = Properties.getString("admin.git.db.username");
        String password = Properties.getString("admin.git.db.password");
        String url = Properties.getString("admin.git.db.url");
        String localBranch = Properties.getString("admin.git.db.branch.local");
        String filepath = localPathMasters+Properties.getString("admin.db.filepath");

        GitHelper git = new GitHelper(localPathMasters,url);

        // Copy local DB tables to LIVE DB.
        AdminUser adminUser = Users.currentAdminUser(usersDao);
        DatabaseCopy copyClass = new DatabaseCopy(collectionFactory, filepath, git);
        boolean copySuccess = copyClass.copy(username, password, localBranch, adminUser.getAdminName(), adminUser.getAdminEmail());

        if (copySuccess) {
            mv.addObject("copysuccess", "Copy to Live database was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Live database failed!");
        }

        return mv;
    }

    // on path /admin/publishjson
    @RequestMapping(value = "publishjson", method = RequestMethod.POST)
    @Secured("hasRole('ROLE_ADMIN')")
    public ModelAndView handlePublishJsonRequest() throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");
        String localPathMasters = Properties
                .getString("admin.git.json.localpath");
        String username = Properties.getString("admin.git.json.username");
        String password = Properties.getString("admin.git.json.password");
        String url = Properties.getString("admin.git.json.url");
        String liveBranch = Properties.getString("admin.git.json.branch.live");
        String localBranch = Properties
                .getString("admin.git.json.branch.local");

        GitHelper git = new GitHelper(localPathMasters, url);
        Boolean success = git.push(username, password, localBranch + ":" + liveBranch);

        if (success) {
            mv.addObject("copysuccess", "Copy to Branch was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Branch failed!");
        }
        return mv;
    }

    // on path /admin/publishcontent
    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/publishcontent", method = RequestMethod.POST)
    public ModelAndView handlePublishContentRequest() throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");
        String localPathMasters = Properties
                .getString("admin.git.content.localpath");
        String username = Properties.getString("admin.git.content.username");
        String password = Properties.getString("admin.git.content.password");
        String url = Properties.getString("admin.git.content.url");
        String liveBranch = Properties
                .getString("admin.git.content.branch.live");
        String localBranch = Properties
                .getString("admin.git.content.branch.local");

        GitHelper git = new GitHelper(localPathMasters, url);
        Boolean success = git.push(username, password, localBranch + ":" + liveBranch);

        if (success) {
            mv.addObject("copysuccess", "Copy to Branch was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Branch failed!");
        }
        return mv;
    }

    // on path /admin/
    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ModelAndView handleRefreshRequest() {

        this.cacheRefresher.refreshDB();
        this.cacheRefresher.refreshJSON();

        ModelAndView mv = new ModelAndView("jsp/adminresult");
        mv.addObject("copysuccess", "Cache has been Refreshed");
        return mv;
    }

    // on path /admin/
    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/")
    public ModelAndView handleAdminRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/admin");
        return modelAndView;
    }
}
