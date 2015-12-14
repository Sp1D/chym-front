/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.abstractclasses;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;

/**
 *
 * @author che
 */
@Entity
@Table(name = "tracker")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractTracker implements Serializable {

    private static final long serialVersionUID = -6717844331654797920L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String url;
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "tracker_id")
//    private List<TrackerFormatMovie> movies;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tracker_id")
    private List<Torrent> torrents;
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Quality> qualities;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AbstractParser parser;
    
    private String lastRssBuildDate;
  
    @Transient
    static final int FULLSEASON_FAKE_EPISODE_NUMBER = 0;
    
    public AbstractTracker() {
    }

    abstract public List<Torrent> parseAllMovies();

    abstract public List<Torrent> parseMovieByTitle(String title);

    public List<Torrent> getTorrentsCovers() {
        List<Torrent> all = getTorrents();
        List<Torrent> covers = new LinkedList<>();
        for (Torrent torrent : all) {
            if (torrent.getType() == Type.SERIES) {
                covers.add(torrent);
            }
        }
        return covers;
    }

    abstract public void updateMovieLastSeasonByTitle(String title);

    public void removeTorrentsByTitle(String title) {
        Iterator<Torrent> iter = getTorrents().iterator();
        while (iter.hasNext()) {
            Torrent torrent = iter.next();
            if (torrent.getTitle().equalsIgnoreCase(title)) {
                iter.remove();
            }
        }
    }
    
    public void removeEpisodesByLocalId(String localId) {
        Iterator<Torrent> iter = getTorrents().iterator();
        while (iter.hasNext()) {
            Torrent torrent = iter.next();
            if (torrent.getLocalId().equalsIgnoreCase(localId) && torrent.getType() == Type.EPISODE) {
                iter.remove();
            }
        }
    }
    
//    Get new torrents of this title, beginning from last episode presents in database
    public void updateMovieByTitle(String title) {    
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title can not be null or empty");
        }
        if (!isTitlePresents(title)) {
            return;
        }
        List<Torrent> updates = getParser().parseUpdatesByTitle(title);
        if (!updates.isEmpty()) {
            getTorrents().addAll(updates);
        }
        
    }
    
    public boolean isTitlePresents(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title can not be null or empty");
        }
        for (Torrent torrent : getTorrents()) {
            if (torrent.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }
    
//    Номер последнего сезона и эпизода можно будет хранить в cover
    public int getLastSeasonByLocalId(String localId) {
        int lastSeason = 0;
        for (Torrent torrent : getTorrents()) {
            if (torrent.getLocalId().equalsIgnoreCase(localId)) {
                if (torrent.getSeason() > lastSeason) lastSeason = torrent.getSeason();
            }
        }        
        return lastSeason;
    }
    
//    Номер последнего сезона и эпизода можно будет хранить в cover
    public int getLastEpisodeByLocalId(String localId) {
        int lastEpisode = 0;
        int lastSeason = getLastSeasonByLocalId(localId);
        if (lastSeason == 0) {
            return lastEpisode;
        }
        for (Torrent torrent : getTorrents()) {
            if (torrent.getLocalId().equalsIgnoreCase(localId) && torrent.getSeason() == lastSeason) {
                if (torrent.getEpisode() > lastEpisode) {
                    lastEpisode = torrent.getEpisode();                    
                }
            }            
        }        
        return lastEpisode;
    }
    
    public String getTorrentLocalIdByTitle(String title) {
        String localId = null;
        for (Torrent cover : getTorrentsCovers()) {
            if (cover.getTitle().equalsIgnoreCase(title)) {
                localId = cover.getLocalId();
                break;
            }
        }
        return localId;
    }
   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AbstractParser getParser() {
        return parser;
    }

    public void setParser(AbstractParser parser) {
        this.parser = parser;
    }


    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }   

    public String getLastRssBuildDate() {
        return lastRssBuildDate;
    }

    public void setLastRssBuildDate(String lastRssBuildDate) {
        this.lastRssBuildDate = lastRssBuildDate;
    }
    
    
    
    
}
