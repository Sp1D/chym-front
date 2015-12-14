/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import net.sp1d.chym.entities.ProtoBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author sp1d
 */
public interface ProtoBeanRepo extends JpaRepository<ProtoBean<String>, Integer>{
    public ProtoBean<String> findByContentIgnoreCase(String content);
    
    @Query("select pb from ProtoBean pb where dtype = :dtype and content = :content")
    public ProtoBean<String> findByDtypeAndContent(@Param("dtype") String dtype,@Param("content") String content);
    
}
