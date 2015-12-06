/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront;

import java.util.Collection;

/**
 *
 * @author sp1d
 */
public class ContainsFunctionJSP {

    public static Boolean collectionContains(Collection collection, Object item) {
        if (collection != null) {            
            return collection.contains(item);
        } else {            
            return false;
        }
    }

}
