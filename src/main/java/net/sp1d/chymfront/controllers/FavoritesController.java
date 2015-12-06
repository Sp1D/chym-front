/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import com.google.api.client.http.HttpRequest;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.User;
import net.sp1d.chym.repos.MovieRepo;
import net.sp1d.chym.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author sp1d
 */
@Controller
@RequestMapping("/favorites")
public class FavoritesController {

    @Autowired
    MovieRepo movieRepo;
    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "add/{reqID}", method = RequestMethod.GET)
    public String addFavorite(@PathVariable String reqID, HttpServletRequest request, HttpSession sess) {
        MovieFull requestedMovie = movieRepo.findByimdbID(reqID);
        User user = (User) sess.getAttribute("user");
        if (user != null && user.getId() != 0) {
            if (user.getFavorites() != null) {
                user.getFavorites().add(requestedMovie);
            } else {
                user.setFavorites(new LinkedList<MovieFull>());
                user.getFavorites().add(requestedMovie);
            }

            User savedUser = userRepo.saveAndFlush(user);
            sess.setAttribute("user", user);
        }
        return "redirect:"+request.getHeader("Referer");
    }
    
    @RequestMapping(value = "remove/{reqID}", method = RequestMethod.GET)
    public String removeFavorite(@PathVariable String reqID, HttpServletRequest request, HttpSession sess) {
        MovieFull requestedMovie = movieRepo.findByimdbID(reqID);
        User user = (User) sess.getAttribute("user");
        if (user != null && user.getId() != 0) {
            if (user.getFavorites() != null) {
                user.getFavorites().remove(requestedMovie);
            }

            User savedUser = userRepo.saveAndFlush(user);
            sess.setAttribute("user", user);
        }
        return "redirect:"+request.getHeader("Referer");
    }
}
