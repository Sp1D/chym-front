/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import net.sp1d.chym.abstractclasses.AbstractFetcher;
import net.sp1d.chym.entities.Episode;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;
import net.sp1d.chym.repos.EpisodeRepo;
import net.sp1d.chym.repos.MovieRepo;
import net.sp1d.chym.repos.TorrentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sp1d
 */
public class FSaver implements FetchNSave {

    static final int nThreads = 5;
    static ExecutorService executor = Executors.newFixedThreadPool(nThreads);

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private EpisodeRepo episodeRepo;

    @Autowired
    private TorrentRepo torrentRepo;

    @Autowired
    private AbstractFetcher fetcher;

    Logger log = LoggerFactory.getLogger(FSaver.class);

    @Override
    public void go() {

    }

    @Override
    public void finish() {
        executor.shutdown();
    }

    @Override
    public void add(Torrent torrent) {
        executor.execute(new Worker(torrent));
    }

    private volatile Set<Integer> blockset = new HashSet<>();

//    Torrents will have equal hash if they are for equal episodes (from internet movie db point of view)
    public Integer episodeHash(Torrent torrent) {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(torrent.getTitle());
        hash = 53 * hash + Objects.hashCode(torrent.getSeason());
        hash = 53 * hash + Objects.hashCode(torrent.getEpisode());
        return hash;
    }

    private synchronized void block(Torrent torrent) {
        blockset.add(episodeHash(torrent));
    }

    private synchronized void unblock(Torrent torrent) {
        blockset.remove(episodeHash(torrent));
    }

    private boolean isBlocked(Torrent torrent) {
        return blockset.contains(episodeHash(torrent));
    }

    private final class Worker implements Runnable {

        Torrent torrent;

        public Worker(Torrent torrent) {
            this.torrent = torrent;
        }

        private MovieFull getMovieFromDB(Torrent torrent) {
            if (torrent.getYearStart() == null || torrent.getYearStart().isEmpty()) {
                return movieRepo.findByTitle(torrent.getTitle());
            } else {
                return movieRepo.findByTitleAndYearStart(torrent.getTitle(), Integer.valueOf(torrent.getYearStart()));
            }
        }

        @Override
        public void run() {
            try {

                if (torrent != null && torrent.getType() == Type.EPISODE && torrent.getEpisode() != 99) {

                    MovieFull series;
                    
                    if (torrent.getYearStart() == null || torrent.getYearStart().isEmpty()) {
                        series = movieRepo.findByTitle(torrent.getTitle());
                    } else {
                        series = movieRepo.findByTitleAndYearStart(torrent.getTitle(), Integer.valueOf(torrent.getYearStart()));
                    }

//                  Episodes should be searched only after series
                    if (series != null) {
                        if (isBlocked(torrent)) {
                            while (isBlocked(torrent)) {
                                yield();
                            }
                        } else {
                            block(torrent);
                        }
//                        Episode episode = episodeRepo.findBySeriesIDAndSeasonAndEpisode(series.getImdbID(),Integer.valueOf(torrent.getSeason()), Integer.valueOf(torrent.getEpisode()));
                        Episode episode = episodeRepo.findBySeriesIDAndSeasonAndEpisode(
                                series.getImdbID(),torrent.getSeason(), torrent.getEpisode());
                        if (episode == null) {
//                            this call also use torrent's startyear field for more relevant search
                            episode = fetcher.getEpisodeByTorrent(torrent);
                            if (episode == null) {
//                            Sometimes internet movie database returns null when year from torrent field is wrong
//                            for those cases when tracker's year isnt correct and maybe other fetching erros in future
                                episode = fetcher.getEpisodeByTorrent(torrent, false);
                            }
                        } else {
                            unblock(torrent);
                        }
//                        Internet database give wrong data sometimes
                        if (episode != null && episode.getSeason() == torrent.getSeason()
                                && episode.getEpisode() == torrent.getEpisode()) {
                            if (episode.getTorrents() == null) {
                                episode.setTorrents(Arrays.asList(torrent));
                            } else {
                                episode.getTorrents().add(torrent);
                            }
                            torrent.setEpisodeId(episode);
                            System.out.println("Episode fetched, title: " + episode.getTitle() + ", torrent quality: " + torrent.getQuality());
                            torrentRepo.saveAndFlush(torrent);

                        }
                        unblock(torrent);
                    }

                }
                if (torrent != null && (torrent.getType() == Type.SERIES || torrent.getType() == Type.MOVIE)) {
                    MovieFull movie;
                    
                    if (isBlocked(torrent)) {
                        while (isBlocked(torrent)) {
                            yield();
                        }
                    } else {
                        block(torrent);
                    }

                    if (torrent.getYearStart() == null || torrent.getYearStart().isEmpty()) {
                        movie = movieRepo.findByTitle(torrent.getTitle());
                    } else {
                        movie = movieRepo.findByTitleAndYearStart(torrent.getTitle(), Integer.valueOf(torrent.getYearStart()));
                    }
                    
                    if (movie == null) {
                        movie = fetcher.getMovieByTorrent(torrent);
                        if (movie == null) {
//                              for those cases when tracker's year isnt correct
                            movie = fetcher.getMovieByTorrent(torrent, false);
                        }
                    } else {
                        unblock(torrent);
                    }
                    if (movie != null) {
                        if (movie.getTorrents() == null) {
                            movie.setTorrents(Arrays.asList(torrent));
                        } else {
                            movie.getTorrents().add(torrent);
                        }
                        torrent.setMovieId(movie);

                        System.out.println("Movie fetched, title: " + movie.getTitle());
//                        movieRepo.save(movie);
                        torrentRepo.save(torrent);
                    }
                    unblock(torrent);
                }

            } catch (Throwable t) {
                System.out.println(t.getMessage());
            }
//            catch (NumberFormatException e) {
//                log.error("Cant convert variable", e);
//            }
        }

    }
}
