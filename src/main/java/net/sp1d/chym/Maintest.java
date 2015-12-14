/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author che
 */
public class Maintest {
    
//    static Logger log = LogManager.getLogger(Maintest.class);
    static Logger log = LoggerFactory.getLogger(Maintest.class);
    
    @Autowired Service service;


    public static void main(String[] args) throws Exception {


//        Service service = Chym.getSpring().getBean("trackerService", Service.class);
//        service.persistNewTrackerParser(LostfilmTracker.class, LostfilmParser.class);
//        AbstractTrackerRepo trackerRepo = Chym.getSpring().getBean("abstractTrackerRepo",AbstractTrackerRepo.class);
//        AbstractFetcher fetcher = Chym.getSpring().getBean(AbstractFetcher.class, "omdbFetcher");
//        AbstractTracker tracker = trackerRepo.findByNameIgnoreCase("lostfilm");
//        Iterator<Torrent> iter = tracker.getTorrents().iterator();
//        while (iter.hasNext()) {
//            Torrent torrent = iter.next();
//            if (torrent.getTitle().equals("It's Always Sunny in Philadelphia")) {
//                iter.remove();
//            }
//        }
//        trackerRepo.save(tracker);
//        tracker.updateMovieByTitle("It's Always Sunny in Philadelphia");
//        tracker.updateMovieByTitle("12 Monkeys");
//        service.save(tracker);
//        service.init();
//        service.checkNewTitlesAllTrackers();
//        service.fetchAll();
//        service.checkMovieByTitle("Narcos");
//        service.fetchByTitle("Narcos");
//        service.stop();
        
       
       

    }

    


}
