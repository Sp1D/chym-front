/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.trackers;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import net.sp1d.chym.abstractclasses.AbstractTracker;
import net.sp1d.chym.entities.Torrent;
import org.springframework.stereotype.Component;

/**
 *
 * @author che
 */
@Entity
@DiscriminatorValue(value = "lostfilm")
@Component
public class LostfilmTracker extends AbstractTracker {

    private static final long serialVersionUID = 4035459347143474056L;
    @Transient
    static private final String NAME = "Lostfilm";
    @Transient
    static private final String URL = "http://www.lostfilm.tv/serials.php";    
    @Transient
    static final int FULLSEASON_FAKE_EPISODE_NUMBER = 99;

    public LostfilmTracker() {
        super();        
        setName(NAME);
        setUrl(URL);
    }

    @Override
    public List<Torrent> parseAllMovies() {
        return getParser().parseAllTorrentsCovers();
    }

    @Override
    public List<Torrent> parseMovieByTitle(String title) {
        return getParser().parseTorrentsByTitle(title);        
    }

    @Override
    public void updateMovieLastSeasonByTitle(String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

        
    

}
