/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.repos;

import java.util.List;
import net.sp1d.chym.abstractclasses.AbstractTracker;
import net.sp1d.chym.entities.Torrent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sp1d
 */
public interface TorrentRepo extends JpaRepository<Torrent, Long>{
    public List<Torrent> findAllByLocalIdAndEpisodeAndSeasonAndTracker(String localId, int episide, int season, AbstractTracker tracker);
}
