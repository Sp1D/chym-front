/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.util.Calendar;
import net.sp1d.chym.entities.User;
import net.sp1d.chym.repos.UserRepo;
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
@SessionAttributes("user")
public class LoginController {
    
    @Autowired UserRepo userRepo; 
    
    @ModelAttribute
    public User getUser(){
        return new User();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(){
        return "login";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model){
        User actualUser = userRepo.findByEmail(user.getEmail());
        if (actualUser == null) {
            model.addAttribute("userNotExists", true);
            return "login";
        }
        if (!actualUser.getPassword().equals(user.getPassword())) {
            model.addAttribute("passwordMismatch", true);
            return "login";
        }        
        actualUser.setLastVisit(Calendar.getInstance().getTime());
        actualUser = userRepo.saveAndFlush(actualUser);
        model.addAttribute("user", actualUser);
        return "redirect:/";
    }
}
