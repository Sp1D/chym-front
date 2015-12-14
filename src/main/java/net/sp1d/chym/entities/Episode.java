/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author sp1d
 */
@Entity
@Table(name = "episodes", uniqueConstraints = @UniqueConstraint(columnNames = {"imdbID","episode","season"}))
public class Episode implements Serializable {

    private static final long serialVersionUID = -7199805354101598413L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private int yearStart;
    private int yearEnd;
    private String rated;
    private String released;
    private int season;
    private int episode;
    private String runtime;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "episodes_genres")
    private List<Genre> genres;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "episodes_directors")
    private List<Person> directors;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "episodes_writers")
    private List<Person> writers;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "episodes_actors")
    private List<Person> actors;
    @Column (length = 5000)
    private String plot;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "episodes_languages")
    private List<Language> languages;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "episodes_countrys")
    private List<Country> countrys;
    private String awards;
    private String poster;
    private String metascore;
    private Float imdbRating;
    private int imdbVotes;
    private String imdbID;
    private String seriesID;
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "type_id")
//    private MovieType type;
    private String responce;
    @OneToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "episodeId", fetch = FetchType.EAGER)    
    private List<Torrent> torrents;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
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

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public List<Person> getWriters() {
        return writers;
    }

    public void setWriters(List<Person> writers) {
        this.writers = writers;
    }

    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Country> getCountrys() {
        return countrys;
    }

    public void setCountrys(List<Country> countrys) {
        this.countrys = countrys;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

//    public MovieType getType() {
//        return type;
//    }
//
//    public void setType(MovieType type) {
//        this.type = type;
//    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.title);
        hash = 31 * hash + this.season;
        hash = 31 * hash + this.episode;
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
        final Episode other = (Episode) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (this.season != other.season) {
            return false;
        }
        if (this.episode != other.episode) {
            return false;
        }
        return true;
    }

}
