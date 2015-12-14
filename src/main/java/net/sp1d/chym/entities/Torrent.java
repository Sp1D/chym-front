/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.sp1d.chym.abstractclasses.AbstractTracker;
import javax.persistence.*;
import net.sp1d.chym.trackers.LostfilmQuality;

/**
 *
 * @author sp1d
 */
@Entity
@Table(name = "torrents", uniqueConstraints = @UniqueConstraint(columnNames = {"tracker_id", "title", "type", "torrent"}))
public class Torrent implements Serializable {

    private static final long serialVersionUID = 7458853222603049861L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String localId;
    @ManyToOne    
    private AbstractTracker tracker;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String title;
    @Enumerated(EnumType.STRING)
    private LostfilmQuality quality;
    private String yearStart;
    private int season;
    private int episode;
    private String torrent;
    private String magnet;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private MovieFull movieId;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "episode_id")
    private Episode episodeId;
    private boolean partially = true;
    
    public void updateEpisodes() {        
        tracker.getTorrents().remove(this);
        
/*        Iterator<Torrent> iter = allTorrents.iterator();
        while (iter.hasNext()) {
            if (iter.next().equals(this)) {
                iter.remove();
            }
        }
*/
                
        List<Torrent> newTorrents = tracker.getParser().parseTorrentsByCoverTorrent(this);
        tracker.getTorrents().addAll(newTorrents);
        
        setPartially(false);        
    }

    public Torrent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String local_id) {
        this.localId = local_id;
    }

    public AbstractTracker getTracker() {
        return tracker;
    }

    public void setTracker(AbstractTracker tracker) {
        this.tracker = tracker;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearStart() {
        return yearStart;
    }

    public void setYearStart(String yearStart) {
        this.yearStart = yearStart;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getTorrent() {
        return torrent;
    }

    public void setTorrent(String torrent) {
        this.torrent = torrent;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public LostfilmQuality getQuality() {
        return quality;
    }

    public void setQuality(LostfilmQuality quality) {
        this.quality = quality;
    }

    public boolean isPartially() {
        return partially;
    }

    public void setPartially(boolean partially) {
        this.partially = partially;
    }

    public MovieFull getMovieId() {
        return movieId;
    }

    public void setMovieId(MovieFull movieId) {
        this.movieId = movieId;
    }

    public Episode getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Episode episodeId) {
        this.episodeId = episodeId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.localId);
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Objects.hashCode(this.title);
        hash = 53 * hash + Objects.hashCode(this.quality);
        hash = 53 * hash + Objects.hashCode(this.torrent);
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
        final Torrent other = (Torrent) obj;
        if (!Objects.equals(this.localId, other.localId)) {
            return false;
        }
        if (!Objects.equals(this.tracker, other.tracker)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.torrent, other.torrent)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Torrent{" + "id=" + id + ", localId=" + localId + ", tracker=" + tracker + 
                ", type=" + type + ", title=" + title + ", quality=" + quality + 
                ", yearStart=" + yearStart + ", season=" + season + ", episode=" + episode + 
                ", torrent=" + torrent + ", magnet=" + magnet + ", partially=" + partially + '}';
    }
    
    

}
