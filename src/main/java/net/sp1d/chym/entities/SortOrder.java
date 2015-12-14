/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.entities;

/**
 *
 * @author sp1d
 */
public enum SortOrder {
    RATING,RELEASED,GENRE,TITLE;
    
    
    public static String stringValues() {
        StringBuilder sb = new StringBuilder();
        for (SortOrder value : SortOrder.values()) {
            sb.append(value.toString().toLowerCase());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        return new String(sb);
    }
}
