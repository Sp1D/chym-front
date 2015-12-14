/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import net.sp1d.chym.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sp1d
 */
public interface UserRepo extends JpaRepository<User, Long>{
    public User findByEmail(String email);
}
