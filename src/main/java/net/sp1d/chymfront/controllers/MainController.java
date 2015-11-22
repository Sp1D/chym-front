/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;


import java.util.List;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 *
 * @author sp1d
 */
//@Component
@Controller
public class MainController {
    
    @Autowired
    MovieRepo movieRepo;
    
    final int cols = 3;
    
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model) {        
        List<MovieFull> movies = movieRepo.findAll();
        int rows = movies.size() % cols > 0 ? movies.size()/cols+1 : movies.size()/3;        
        
        model.addAttribute("MovieFullList", movies);        
        model.addAttribute("rows", rows);
        model.addAttribute("cols", cols);
        
        return "home";
    }
}
