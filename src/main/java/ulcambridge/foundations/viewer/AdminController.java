/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.CollectionsDBDao;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsJSONDao;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
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

    @RequestMapping(value = "/adminsuccess", method = RequestMethod.GET)
    public ModelAndView handleAdminSuccessRequest()
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
