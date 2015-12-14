/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SearchType;
import java.util.List;
import net.sp1d.chym.abstractclasses.AbstractFetcher;
import net.sp1d.chym.entities.Episode;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sp1d
 */
public class MovieFetcherTMDB extends AbstractFetcher {

    TheMovieDbApi tmdbapi;
    Logger log = LoggerFactory.getLogger(MovieFetcherTMDB.class);

    public MovieFetcherTMDB(String apikey) {
        try {
            tmdbapi = new TheMovieDbApi(apikey);
        } catch (MovieDbException e) {
            log.error("cant init tmdb api", e);
        }
    }

    @Override
    public MovieFull getMovieByTorrent(Torrent torrent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MovieFull getMovieByTorrent(Torrent torrent, boolean byYear) {
        if (torrent == null) {
            throw new IllegalArgumentException();
        }
        if (torrent.getType() != Type.MOVIE && torrent.getType() != Type.SERIES) {
            throw new IllegalArgumentException("Wrong torrent type, details: " + torrent);
        }
        if (torrent.getTitle() == null || torrent.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Torrent must have a title");
        }
        Integer yearStart = null;
        if (torrent.getYearStart() != null) {
            try {
                yearStart = Integer.valueOf(torrent.getYearStart());
            } catch (NumberFormatException e) {                
            }
        }
        try {
            tmdbapi.searchTV(checkField(torrent.getTitle(), checkedFields.TITLE),
                    Integer.SIZE,
                    null,
                    yearStart,
                    SearchType.PHRASE);
        } catch (MovieDbException ex) {
            log.error("some error in TMDB api", ex);            
        }
        
        return new MovieFull();
    }

    @Override
    public Episode getEpisodeByTorrent(Torrent torrent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Episode getEpisodeByTorrent(Torrent torrent, boolean byYear) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MovieFull> getMoviesListByTorrents(List<Torrent> torrents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Episode> getEpisodesListByTorrents(List<Torrent> torrents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
