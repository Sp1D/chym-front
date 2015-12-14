/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import net.sp1d.chym.entities.MovieFull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sp1d
 */

public interface MovieRepo extends JpaRepository<MovieFull, Integer>, MovieRepoCustom{
    public MovieFull findByTitle(String title);
    public MovieFull findByTitleAndYearStart(String title, int year);
    public MovieFull findByimdbID(String imdbID); 
//
//    @Query("SELECT f FROM User u JOIN u.favorites f WHERE u.id = :user ORDER BY f.:property DESC")
//    Page<MovieFull> findAllll(@Param(value = "user") long id, Pageable pr, @Param("property") String property);    
}
