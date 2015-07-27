/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
@Controller
public class AdminController {

    private CollectionFactory collectionFactory;
    private ItemFactory itemFactory;

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }
    
    //on path /admin/publishdb
    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/publishdb", method = RequestMethod.GET)    
    public ModelAndView handlePublishDbRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");

        DatabaseCopy copyClass = new DatabaseCopy(collectionFactory);
        Boolean success = copyClass.copy();
        if (success) {
            mv.addObject("copysuccess", "Copy to Live database was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Live database failed!");
        }

        return mv;
    }    

    // on path /admin/publishjson
    @RequestMapping(value = "publishjson", method = RequestMethod.GET)
    @Secured("hasRole('ROLE_ADMIN')")    
    public ModelAndView handlePublishJsonRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");
        String localPathMasters = Properties.getString("admin.git.json.localpath");
        String username = Properties.getString("admin.git.json.username");
        String password = Properties.getString("admin.git.json.password");
        String url = Properties.getString("admin.git.json.url");
        String refspec = Properties.getString("admin.git.json.refspec");
        
        GitCudlDataCopy gjson = new GitCudlDataCopy(localPathMasters,username,password,url,refspec);
        Boolean success = gjson.gitcopy();
        if (success) {
            mv.addObject("copysuccess", "Copy to Branch was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Branch failed!");
        }
        return mv;
    }

    // on path /admin/publishcontent
    @Secured("hasRole('ROLE_ADMIN')")    
    @RequestMapping(value = "/publishcontent", method = RequestMethod.GET)
    public ModelAndView handlePublishContentRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminresult");
        String localPathMasters = Properties.getString("admin.git.content.localpath");
        String username = Properties.getString("admin.git.content.username");
        String password = Properties.getString("admin.git.content.password");
        String url = Properties.getString("admin.git.content.url");
        String refspec = Properties.getString("admin.git.content.refspec");
        
        GitCudlDataCopy gjson = new GitCudlDataCopy(localPathMasters,username,password,url,refspec);
        Boolean success = gjson.gitcopy();
        if (success) {
            mv.addObject("copysuccess", "Copy to Branch was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Branch failed!");
        }
        return mv;
    }


    // on path /admin/
    @RequestMapping(value = "/")
    public ModelAndView handleAdminRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/admin");
        return modelAndView;
    }
}
