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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author sp1d
 */
@Controller
@RequestMapping("/login")
//@SessionAttributes("user")
public class LoginController {
    
    Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepo userRepo;

    @ModelAttribute
    public User getUser() {
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        User savedUser = userRepo.findByEmail(user.getEmail());
        if (savedUser == null) {
            model.addAttribute("userNotExists", true);
            return "login";
        }
        try {
            if (!savedUser.getSecret().equals(new String(Butler.crypt(user), "UTF-8"))) {
                model.addAttribute("passwordMismatch", true);
                return "login";
            }
        } catch (Exception e){
            log.error("Can not check entered password", e);
        }
        savedUser.setLastVisit(Calendar.getInstance().getTime());
        savedUser = userRepo.saveAndFlush(savedUser);
        request.getSession().invalidate();
//        model.addAttribute("user", savedUser);
        request.getSession().setAttribute("user", savedUser);
        return "redirect:/";
    }
}
