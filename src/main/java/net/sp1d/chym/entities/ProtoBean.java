/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author che
 */
@Entity
@Table(name = "fields", uniqueConstraints = @UniqueConstraint(columnNames = {"DTYPE","content"}))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ProtoBean <T> implements Serializable{
    private static final long serialVersionUID = 6897158591432784454L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bean_id;
    
    @Column(unique = true)
    String content;
    
    
    public String getContent() {
        return content;
    }
    
    public  void setContent(String content){
        this.content = content;
    }

    public int getBean_id() {
        return bean_id;
    }

    public void setBean_id(int bean_id) {
        this.bean_id = bean_id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProtoBean<?> other = (ProtoBean<?>) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+"{" + "value=" + content + '}';
    }
    
    
    
}
