/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author sp1d
 */
public interface MovieRepoCustom {
    public Page<MovieFull> findFavoritesAndAll(User user, Pageable pageable);
}
