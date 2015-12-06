/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import net.sp1d.chym.entities.User;
import net.sp1d.chym.repos.UserRepo;
import net.sp1d.chymfront.Settings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author sp1d
 */
@Controller
@RequestMapping("/signup")
//@SessionAttributes(value = "user")
public class SignUpController {
    
    Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepo userRepo;
    
    @Autowired
    UserValidator userValidator;
    
    @ModelAttribute
    public User getUser(){
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String signUp(Model model) {
//        model.addAttribute("user", new User());
        model.addAttribute("settings",Settings.getSettings());
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "user") User user, BindingResult result, Model model, HttpServletRequest request) {
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        } else {
            user.setLastVisit(Calendar.getInstance().getTime());            
            
            try {
                byte[] hash = Butler.crypt(user);
                user.setSecret(new String(hash, "UTF-8"));
            } catch (Exception ex) {
                log.error("Can not generate password hash", ex);                
            }            
            User loggedUser = userRepo.saveAndFlush(user);
//            request.changeSessionId();
            request.getSession().invalidate();
            request.getSession().setAttribute("user", loggedUser);
//            model.addAttribute("user", loggedUser);            
            return "registered";
        }
    }
}
