/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;

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

    // on path /admin/jsonsuccess
    @RequestMapping(value = "/jsonsuccess", method = RequestMethod.GET)
    public ModelAndView handleAdminSuccessRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminsuccess");
        GitJsonCopy gjson = new GitJsonCopy();
        Boolean success = gjson.merge();
        if (success) {
            mv.addObject("copysuccess", "Copy to Branch was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Branch failed!");
        }
        return mv;
    }

    //on path /admin/dbsuccess
    @RequestMapping(value = "/dbsuccess", method = RequestMethod.GET)
    public ModelAndView handleDbSuccessRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminsuccess");

        DatabaseCopy copyClass = new DatabaseCopy(collectionFactory);
        Boolean success = copyClass.init();
        if (success) {
            mv.addObject("copysuccess", "Copy to Live database was successful!");
        } else {
            mv.addObject("copysuccess", "Copy to Live database failed!");
        }
        this.collectionFactory.init();
        ItemFactory itemfactory = new ItemFactory();
        itemfactory.clearItemCache();
        return mv;
    }

    // on path /admin/
    @RequestMapping(value = "/")
    public ModelAndView handleAdminRequest() {

        ModelAndView modelAndView = new ModelAndView("jsp/admin");
        return modelAndView;
    }
}
