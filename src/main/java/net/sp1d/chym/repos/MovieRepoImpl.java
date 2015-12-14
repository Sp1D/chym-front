/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

/**
 *
 * @author sp1d
 */
public class MovieRepoImpl implements MovieRepoCustom {

    @PersistenceContext
    EntityManager em;

    final String MOVIES_TABLENAME = "movies";
    final String FIND_FAVORITES_AND_ALL_TEMPLATE = "SELECT m FROM MovieFull m WHERE m NOT IN(SELECT mf FROM User u JOIN u.favorites mf WHERE u.id = :uid) ORDER BY ";

    @Override
    public Page<MovieFull> findFavoritesAndAll(User user, Pageable pageable) {
        Query q = em.createNativeQuery("SELECT count(*) FROM " + MOVIES_TABLENAME);        

        int favoritesSize = user.getFavorites().size();             
        int actualSize = favoritesSize + ((BigInteger)q.getSingleResult()).intValue();                
        int offset = pageable.getOffset();        
        int size = pageable.getPageSize();
        List<MovieFull> resultlist = new LinkedList<>();
        
        if ((offset + size) <= favoritesSize) {
            resultlist.addAll(user.getFavorites().subList(offset, offset+size));            
            return new PageImpl<>(resultlist, pageable, actualSize);
        } else {
            if (offset < favoritesSize) {
                resultlist.addAll(user.getFavorites().subList(offset, favoritesSize));
            }
            StringBuilder sb = new StringBuilder();
            Iterator<Order> iter = pageable.getSort().iterator();
            while (iter.hasNext()) {
                Order order = iter.next(); 
                sb.append("m.");
                sb.append(order.getProperty());
                sb.append(' ');
                sb.append(order.getDirection().toString().intern());
                sb.append(',');
            }
            sb.deleteCharAt(sb.length()-1);
            int moviesOffset = offset <= favoritesSize ? 0 : offset-favoritesSize;
            int moviesSize = offset >= favoritesSize ? size : size-(favoritesSize - offset);
//            q = em.createQuery("SELECT m FROM MovieFull m ORDER BY "+sb.toString()+" LIMIT "+moviesOffset+","+moviesSize);
            q = em.createQuery(FIND_FAVORITES_AND_ALL_TEMPLATE+sb.toString());
            q.setParameter("uid", user.getId());            
            q.setFirstResult(moviesOffset);
            q.setMaxResults(moviesSize);
            resultlist.addAll(q.getResultList());
            return new PageImpl<>(resultlist, pageable, actualSize);
        }
    }

}
