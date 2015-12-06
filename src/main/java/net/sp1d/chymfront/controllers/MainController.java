/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.User;
import net.sp1d.chym.repos.MovieRepo;
import net.sp1d.chym.repos.UserRepo;
import net.sp1d.chym.entities.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    final Integer DEF_PAGESIZE = 10;
    final SortOrder DEF_SORTORDER = SortOrder.TITLE;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String home(Model model, HttpServletRequest request) {
        List<MovieFull> movies = null;
        User user = (User) request.getSession().getAttribute("user");
        Direction direction = Direction.DESC;
        Map<String, Object> chParams = checkParameters(request.getParameterMap(), user, request);

        boolean favoritesFirst = (boolean) chParams.get("favoritesFirst");
        int page = (int) chParams.get("page");
        int pagesize = (int) chParams.get("pageSize");
        String sortProperty = (String) chParams.get("sortProperty");
        SortOrder curSort = (SortOrder) chParams.get("sortOrder");
        if (curSort == SortOrder.TITLE) {
            direction = Direction.ASC;
        }
        Sort sort = new Sort(direction, sortProperty);
        
        Page<MovieFull> moviesPage = movieRepo.findAll(new PageRequest(page, pagesize, sort));        
        movies = new LinkedList<MovieFull>(moviesPage.getContent());

        if (favoritesFirst && user != null && user.getFavorites() != null) {
            movies.removeAll(user.getFavorites());
            movies.addAll(0, user.getFavorites());
        }

//        Number of rows on the main page depending of columns number
        int rows = movies.size() % cols > 0 ? movies.size() / cols + 1 : movies.size() / 3;

        model.addAttribute("MovieFullList", movies);
        model.addAttribute("rows", rows);
        model.addAttribute("cols", cols);
        model.addAttribute("pageSize", pagesize);
        model.addAttribute("sortOrder", curSort.toString().toLowerCase());
        model.addAttribute("sortValues", SortOrder.stringValues());
        model.addAttribute("favoritesFirst", favoritesFirst);
        model.addAttribute("pagesTotal", moviesPage.getTotalPages());
        model.addAttribute("pagesCurrent", moviesPage.getNumber());
        model.addAttribute("pagesFirst", moviesPage.isFirst());
        model.addAttribute("pagesLast", moviesPage.isLast());
        
        List<MovieFull> testlist = movieRepo.findAllll(user, new PageRequest(0, pagesize)).getContent();
        model.addAttribute("testlist", testlist);
                
        return "home";
    }

    public Map<String, Object> checkParameters(Map<String, String[]> params, User user, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        boolean userSettingsChanged = false;

        SortOrder sortOrder = DEF_SORTORDER;
        if (params.get("sort") != null) {
            try {
                sortOrder = SortOrder.valueOf(params.get("sort")[0].toUpperCase());
            } catch (Exception e) {
            }
        } else if (user != null) {
            if (user.getSortOrder() != null) {
                sortOrder = user.getSortOrder();
            }
        }
        result.put("sortOrder", sortOrder);
        if (user != null && user.getSortOrder() != sortOrder) {
            user.setSortOrder(sortOrder);
            userSettingsChanged = true;
        }

        int page = 0;
        if (params.get("p") != null) {
            try {
                page = Integer.valueOf(params.get("p")[0]);
//                Because PageRequest counts from 0
                page--;
            } catch (Exception e) {
            }
        }
        result.put("page", page);

        int pageSize = DEF_PAGESIZE;
        if (params.get("s") != null) {
            try {
                pageSize = Integer.valueOf(params.get("s")[0]);                
            } catch (Exception e) {
            }
        } else if (user != null) {
            if (user.getPagesize() > 0) {
                pageSize = user.getPagesize();
            }
        }
        result.put("pageSize", pageSize);
        if (user != null && user.getPagesize() != pageSize) {
            user.setPagesize(pageSize);
            userSettingsChanged = true;
        }

        String sortProperty = null;
        switch (sortOrder) {
            case GENRE:
                sortProperty = "genres";
                break;
            case RATING:
                sortProperty = "imdbRating";
                break;
            case RELEASED:
                sortProperty = "released";
                break;
            case TITLE:
                sortProperty = "title";
            default:
                sortProperty = "title";
        }
        result.put("sortProperty", sortProperty);

        boolean favoritesFirst = true;
        if (params.get("favorites") != null) {
            if ("yes".equalsIgnoreCase(params.get("favorites")[0])) {
                favoritesFirst = true;
            } else if ("no".equalsIgnoreCase(params.get("favorites")[0])) {
                favoritesFirst = false;
            }
        } else if (user != null) {
            favoritesFirst = user.isFavoritesFirst();
        }        
        result.put("favoritesFirst", favoritesFirst);
        if (user != null && user.isFavoritesFirst() != favoritesFirst) {
            user.setFavoritesFirst(favoritesFirst);
            userSettingsChanged = true;
        }

        if (userSettingsChanged) {
            user = userRepo.saveAndFlush(user);
            request.getSession().setAttribute("user", user);
        }

        return result;
    }
    /*
     @RequestMapping(method = RequestMethod.GET, value = "/")
     public String home(Model model, HttpServletRequest request) {
     List<MovieFull> movies = movieRepo.findAll();        
     User user = (User) request.getSession().getAttribute("user");
     //        if (user != null && user.getId() == 0) {
     //            user = null;
     //            request.getSession().removeAttribute("user");            
     //        }

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
     if (user != null && user.getFavorites() != null) {
     movies.sort(new RatingComparator(false, user.getFavorites()));
     } else {
     movies.sort(new RatingComparator());
     }
     }
     if (order == SortOrder.RELEASED) {
     if (user != null && user.getFavorites() != null) {
     movies.sort(new ReleasedComparator(false, user.getFavorites()));
     } else {
     movies.sort(new ReleasedComparator());
     }
     }
     if (order == SortOrder.TITLE) {
     if (user != null && user.getFavorites() != null) {
     movies.sort(new TitleComparator(false, user.getFavorites()));
     } else {
     movies.sort(new TitleComparator());
     }
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
    
     */

    abstract class abstractComparator {

        boolean ascending = false;
        Collection favorites = null;

        public abstractComparator() {
        }

        public abstractComparator(boolean ascending, Collection favorites) {
            this.ascending = ascending;
            this.favorites = favorites;
        }

        protected int compareFavorites(Collection favorites, MovieFull o1, MovieFull o2) {
            boolean cont1 = favorites.contains(o1);
            boolean cont2 = favorites.contains(o2);
            if (cont1 & !cont2) {
                return -1;
            } else if (cont2 & !cont1) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    class RatingComparator extends abstractComparator implements Comparator<MovieFull> {

        public RatingComparator() {
        }

        public RatingComparator(boolean ascending, Collection favorites) {
            super(ascending, favorites);
        }

        @Override
        public int compare(MovieFull o1, MovieFull o2) {
            if (favorites != null) {
                return compareFavorites(favorites, o1, o2);
            }
            if (ascending) {
                return Double.compare(o1.getImdbRating(), o2.getImdbRating());
            } else {
                return Double.compare(o2.getImdbRating(), o1.getImdbRating());
            }
        }
    }

    class ReleasedComparator extends abstractComparator implements Comparator<MovieFull> {

        public ReleasedComparator() {
        }

        public ReleasedComparator(boolean ascending, Collection favorites) {
            super(ascending, favorites);
        }

        @Override
        public int compare(MovieFull o1, MovieFull o2) {
            if (favorites != null) {
                return compareFavorites(favorites, o1, o2);
            }
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

    class TitleComparator extends abstractComparator implements Comparator<MovieFull> {

        public TitleComparator() {
        }

        public TitleComparator(boolean ascending, Collection favorites) {
            super(ascending, favorites);
        }

        @Override
        public int compare(MovieFull o1, MovieFull o2) {
            if (favorites != null) {
                return compareFavorites(favorites, o1, o2);
            }
            if (ascending) {
                return o2.getTitle().compareToIgnoreCase(o1.getTitle());
            } else {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        }
    }

}
