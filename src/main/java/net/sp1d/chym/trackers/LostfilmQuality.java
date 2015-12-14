/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.trackers;

/**
 *
 * @author sp1d
 */

public enum LostfilmQuality {
    
    HD1080("1080p WEB-DLRip|1080p HDTVRip|1080p WEBRip"),HD720("720p WEB-DLRip|720p WEB-DL|720p HDTVRip|720p WEBRip"),SD("WEB-DLRip|HDTV-Rip|HDTVRip|WEBRip");
    private String pattern;

    private LostfilmQuality(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public static LostfilmQuality find(String s) {        
        for (LostfilmQuality value : LostfilmQuality.values()) {
            for (String split : value.getPattern().split("\\|")) {
                if (split.equalsIgnoreCase(s)) {
                    return value;
                }
            }
        }
        return null;
    }
    
}
