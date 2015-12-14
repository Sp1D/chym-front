/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author che
 */
@Entity
@DiscriminatorValue(value = "person")
public class Person extends ProtoBean<String>{
    private static final long serialVersionUID = 3120508204540690680L;
    
    
}
