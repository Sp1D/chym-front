/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import net.sp1d.chym.abstractclasses.AbstractTracker;
import net.sp1d.chym.abstractclasses.AbstractParser;
import net.sp1d.chym.repos.AbstractTrackerRepo;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;
import net.sp1d.chym.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sp1d
 */
@Repository
public class Service {

//    @PersistenceContext
//    EntityManager em;
    @Autowired
    AbstractTrackerRepo abstractTrackerRepo;

    @Autowired
    FetchNSave fetchSaver;

    @Autowired
    MovieRepo movieRepo;

    public void init() {
//        fetchSaver.go();
    }

    public void stop() {
//        fetchSaver.finish();
    }

    @Transactional
    public void persistNewTrackerParser(Class tracker, Class parser) throws InstantiationException, IllegalAccessException, PersistenceException {
//        AbstractTrackerRepo trackerRepo = spring.getBean("abstractTrackerRepo", AbstractTrackerRepo.class);
        AbstractTracker t = (AbstractTracker) tracker.newInstance();
        AbstractParser p = (AbstractParser) parser.newInstance();
        p.setTracker(t);
        t.setParser(p);

//        em.persist(t);        
        abstractTrackerRepo.save(t);
    }

//    fetches all titles from trackers, and gets all torrents for those titles which is not presents in database
    @Transactional
    public void checkNewTitlesAllTrackers() {
        System.out.println("TEST OUTPUT");
        
        
//        List<AbstractTracker> trackers = abstractTrackerRepo.findAll();
//        for (AbstractTracker tracker : trackers) {
//            List<Torrent> actualCoversList = tracker.getParser().parseAllTorrentsCovers();
//            List<Torrent> savedCoversList = tracker.getTorrentsCovers();
//
//            Iterator<Torrent> iter = savedCoversList.iterator();
//            while (iter.hasNext()) {
//                if (iter.next().isPartially()) {
//                    iter.remove();
//                }
//            }
//            actualCoversList.removeAll(savedCoversList);
//
//            if (!actualCoversList.isEmpty()) {
//                List<Torrent> newCoversList = actualCoversList;
//
////                for (Torrent torrentCover : newCoversList) {
////                    TEMPORARY CODE FOR TESTING PURPOSE
//                for (int i = 0; i < 2; i++) {
//                    Torrent torrentCover = newCoversList.get(i);
//                    
//                    torrentCover.setPartially(true);
//                    tracker.getTorrents().remove(torrentCover);
//                    if (torrentCover.getType() == Type.SERIES) {
//                        tracker.removeEpisodesByLocalId(torrentCover.getLocalId());
//                        tracker = abstractTrackerRepo.save(tracker);
//                    }
//                    tracker.getTorrents().addAll(tracker.getParser().parseTorrentsByCoverTorrent(torrentCover));
//                    torrentCover.setPartially(false);
//                    tracker.getTorrents().add(torrentCover);
//                }
//            }
//            abstractTrackerRepo.save(tracker);
//        }

    }

    public void checkMovieByTitle(String title) {
        for (AbstractTracker tracker : abstractTrackerRepo.findAll()) {
            List<Torrent> torrents = tracker.parseMovieByTitle(title);
            tracker.getTorrents().addAll(torrents);
            abstractTrackerRepo.save(tracker);
        }

    }

    public void updateTorrentsFromRss() {

        for (AbstractTracker tracker : abstractTrackerRepo.findAll()) {
            String savedRssDate = tracker.getLastRssBuildDate();
            String lastRssDate = tracker.getParser().parseRssLastBuildDate();
            if (lastRssDate == null || lastRssDate.isEmpty()) {
                throw new NullPointerException("Can not parse last RSS build date from tracker's feed");
            }
            if (savedRssDate != null
                    && lastRssDate.equalsIgnoreCase(savedRssDate)) {
                return;
            }

            Set<String> titles = tracker.getParser().parseRssUpdates();
            for (String title : titles) {
                System.out.println(title);
                tracker.updateMovieByTitle(title);
            }
            tracker.setLastRssBuildDate(lastRssDate);
            abstractTrackerRepo.save(tracker);
        }
    }

    public void save(AbstractTracker tracker) {
        abstractTrackerRepo.save(tracker);
    }
    @Transactional
    public void fetchAll() {

//        FetcherSaver fetcherSaver = Chym.getSpring().getBean("fetcherSaver", FetcherSaver.class);
        for (AbstractTracker tracker : abstractTrackerRepo.findAll()) {
            for (Torrent torrent : tracker.getTorrents()) {
                if (torrent.getType() == Type.SERIES && torrent.getEpisodeId() == null && torrent.getMovieId() == null) {
                    fetchSaver.add(torrent);
                }
            }
            for (Torrent torrent : tracker.getTorrents()) {
                if (torrent.getType() != Type.SERIES && torrent.getEpisodeId() == null && torrent.getMovieId() == null && torrent.getEpisode() != 99) {
                    fetchSaver.add(torrent);
                }
            }
        }

    }

    public void fetchByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return;
        }
        for (AbstractTracker tracker : abstractTrackerRepo.findAll()) {
            for (Torrent torrent : tracker.getTorrents()) {
                if (torrent.getType() == Type.SERIES && torrent.getEpisodeId() == null
                        && torrent.getMovieId() == null && torrent.getTitle().equalsIgnoreCase(title)) {
                    fetchSaver.add(torrent);
                }
            }
            for (Torrent torrent : tracker.getTorrents()) {
                if (torrent.getType() != Type.SERIES && torrent.getEpisodeId() == null
                        && torrent.getMovieId() == null && torrent.getTitle().equalsIgnoreCase(title)) {
                    fetchSaver.add(torrent);
                }
            }
        }

    }

    public AbstractTrackerRepo getTrackerRepo() {
        return abstractTrackerRepo;
    }

    public void somethingStrange() {
        long q = 1;
        for (int i = 0; i < 10_000_000; i++) {
            q *= 2;
            if (i % 100_000 == 0) {
                System.out.print(".");
            }
        }
    }
    
}
