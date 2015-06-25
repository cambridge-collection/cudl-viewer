/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rekha
 */
@Controller
public class AdminController {

    @RequestMapping(value = "/adminsuccess", method = RequestMethod.GET)
    public ModelAndView handleAdminSuccessRequest()
            throws Exception {

        ModelAndView mv = new ModelAndView("jsp/adminsuccess");
        DatabaseCopy copyClass = new DatabaseCopy();
        Boolean success=copyClass.init();
        if (success){
            mv.addObject("copysuccess","Copy to Live database was successful!");
        }else{
            mv.addObject("copysuccess","Copy to Live database failed!");
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
