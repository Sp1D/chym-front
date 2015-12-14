/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import java.util.List;
import net.sp1d.chym.entities.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sp1d
 */
public interface EpisodeRepo extends JpaRepository<Episode, Long>{
    public Episode findByTitleAndSeasonAndEpisode(String title, int season, int episode);
    public Episode findBySeriesIDAndSeasonAndEpisode(String seriesId, int season, int episode);
    public List<Episode> findBySeriesID(String seriesId);
}
