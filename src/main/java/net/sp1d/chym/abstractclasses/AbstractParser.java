/*
 * 
 */
package net.sp1d.chym.abstractclasses;

import java.io.Serializable;
import net.sp1d.chym.entities.Torrent;
import java.util.List;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import net.sp1d.chym.entities.Torrent;


/**
 *
 * @author Eugene L Subbotin
 */
@Entity
@Table(name = "parsers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractParser implements Serializable{
    private static final long serialVersionUID = -3025108647700256717L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "parser")    
    private AbstractTracker tracker;

    public AbstractParser() {
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

    public AbstractTracker getTracker() {
        return tracker;
    }

    public void setTracker(AbstractTracker tracker) {
        this.tracker = tracker;
    }
    
    

    public abstract List<Torrent> parseTorrentsByTitle(String name);

    public abstract List<Torrent> parseAllTorrentsCovers();
    
    public abstract Set<String> parseRssUpdates();
    
    public abstract String parseRssLastBuildDate();
    
    public abstract List<Torrent> parseUpdatesByTitle(String title);
    
    public abstract List<Torrent> parseTorrentsByCoverTorrent(Torrent cover);

    public String parseStringBetweenMarkers(String line, String start, String end) {
        int start_index = line.indexOf(start);
        int end_index = line.indexOf(end);
        if (start_index > 0 && end_index > 0) {
            return line.substring(start_index + start.length(), end_index);
        } else {
            return "";
        }
    }
}
