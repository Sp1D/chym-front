/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import com.google.api.client.util.Key;

/**
 *
 * @author sp1d
 */
public class EpisodeOMDB extends MovieOMDB {

    @Key("Season")
    private String season;
    @Key("Episode")
    private String episode;
    @Key("seriesID")
    private String seriesID;

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    
}
