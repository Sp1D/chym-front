/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sp1d.chym.abstractclasses.AbstractTracker;
import net.sp1d.chym.entities.Episode;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.repos.EpisodeRepo;
import net.sp1d.chym.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 *
 * @author sp1d
 */
@Controller
public class EpisodesController {
    
    @Autowired
    EpisodeRepo episodeRepo;
    
    @Autowired
    MovieRepo movieRepo;
        
    @RequestMapping("/episodes/{seriesId}")
    public String getEpisodes(@PathVariable(value = "seriesId") String seriesId, Model model) {     
        List<Episode> sortedEpisodes = episodeRepo.findBySeriesID(seriesId);
        sortedEpisodes.sort(new Comparator<Episode>(){
            @Override
            public int compare(Episode o1, Episode o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                int o1num = o1.getSeason() * 100 + o1.getEpisode();
                int o2num = o2.getSeason() * 100 + o2.getEpisode();
                return o1num - o2num;
            }
            
        });
        MovieFull movie = movieRepo.findByimdbID(seriesId);
        AbstractTracker tracker = movie.getTorrents().get(0).getTracker();
        String localId = movie.getTorrents().get(0).getLocalId();
        
        int lastSeason = tracker.getLastSeasonByLocalId(localId);
        
        Map<Integer,Integer> episodesInSeasons = new HashMap<>();
        for (Episode episode : sortedEpisodes) {
            if (episode.getEpisode() > episodesInSeasons.getOrDefault(episode.getSeason(), 0)) {
                episodesInSeasons.put(episode.getSeason(), episode.getEpisode());
            }
            
        }
        
        model.addAttribute("episodesList", sortedEpisodes);
        model.addAttribute("seriesTitle", movie.getTitle());
        model.addAttribute("lastSeason", lastSeason);
        model.addAttribute("episodesInSeasonsMap", episodesInSeasons);
        return "episodes";
    }
}
