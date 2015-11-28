/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.User;
import net.sp1d.chym.repos.MovieRepo;
import net.sp1d.chym.repos.UserRepo;
import net.sp1d.chym.entities.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author sp1d
 */
@Controller
@SessionAttributes(value = "user")
public class MainController {

    @Autowired
    MovieRepo movieRepo;

    @Autowired
    UserRepo userRepo;

    final int cols = 6;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model, HttpServletRequest request) {
        List<MovieFull> movies = movieRepo.findAll();
        User user = (User) request.getSession().getAttribute("user");

        String sortParameter = request.getParameter("sortby");
        SortOrder order = null;


        if (sortParameter != null) {
            sortParameter = sortParameter.toUpperCase();
            order = SortOrder.valueOf(sortParameter);
        } else if (user != null) {
            if (user.getSortOrder() != null) {
                order = user.getSortOrder();
            }
        } else {
            order = SortOrder.TITLE;
        }

        if (order == SortOrder.RATING) {
            movies.sort(new RatingComparator());
        }
        if (order == SortOrder.RELEASED) {
            movies.sort(new ReleasedComparator());
        }

//      if user's settings for sorting isnt equal with current sorting order, save current order to user
        if (user != null && order != null && user.getSortOrder() != order) {
            user.setSortOrder(order);
            userRepo.saveAndFlush(user);
        }

//        Number of rows on the main page depending of columns number
        int rows = movies.size() % cols > 0 ? movies.size() / cols + 1 : movies.size() / 3;

        model.addAttribute("MovieFullList", movies);
        model.addAttribute("rows", rows);
        model.addAttribute("cols", cols);
        return "home";
    }

    class RatingComparator implements Comparator<MovieFull> {

        boolean ascending = false;

        public RatingComparator() {
        }

        public RatingComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(MovieFull o1, MovieFull o2) {
            if (ascending) {
                return Double.compare(o1.getImdbRating(), o2.getImdbRating());
            } else {
                return Double.compare(o2.getImdbRating(), o1.getImdbRating());
            }
        }
    }

    class ReleasedComparator implements Comparator<MovieFull> {

        boolean ascending = false;

        public ReleasedComparator() {
        }

        public ReleasedComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(MovieFull o1, MovieFull o2) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date o1d, o2d;
            try {
                o1d = sdf.parse(o1.getReleased());
                o2d = sdf.parse(o2.getReleased());
            } catch (ParseException parseException) {
                return 0;
            }

            if (o1d == null || o2d == null) {
                return 0;
            }

            if (ascending) {
                return o1d.compareTo(o2d);
            } else {
                return o2d.compareTo(o1d);
            }
        }
    }

}
