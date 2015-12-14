/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.abstractclasses;

import net.sp1d.chym.repos.ProtoBeanRepo;
import net.sp1d.chym.entities.MovieFull;
import java.util.LinkedList;
import java.util.List;
import net.sp1d.chym.entities.Episode;
import net.sp1d.chym.entities.ProtoBean;
import net.sp1d.chym.entities.Torrent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author che
 */
@Component
public abstract class AbstractFetcher {

    @Autowired
    ProtoBeanRepo protoBeanRepo;

    public AbstractFetcher() {
    }

    abstract public MovieFull getMovieByTorrent(Torrent torrent);
    abstract public MovieFull getMovieByTorrent(Torrent torrent, boolean byYear);

    abstract public Episode getEpisodeByTorrent(Torrent torrent);
    abstract public Episode getEpisodeByTorrent(Torrent torrent, boolean byYear);

    abstract public List<MovieFull> getMoviesListByTorrents(List<Torrent> torrents);
    abstract public List<Episode> getEpisodesListByTorrents(List<Torrent> torrents);

//    <E extends ProtoBean> List<E> convertStringToStringBeans(String s, Class<E> cl) throws InstantiationException, IllegalAccessException {
//        List<E> result = new LinkedList<>();
//        
//        for (String str : s.split(",")) {
//            E bean = cl.newInstance();
//            bean.setValue(str.trim());
//            result.add(bean);            
//        }
//        
//        
//        return result;
//    }
    
    protected enum checkedFields{
        TITLE
    }
    
    protected String checkField(String content, checkedFields field) {
        if (field == checkedFields.TITLE) {
            int i = content.indexOf(':');
            if (i != -1) {
                return new String(content.substring(0, i));
            }
        }
        return content;
    }
    
    protected <E extends ProtoBean> List<E> convertStringToStringBeans(String s, Class<E> cl) throws InstantiationException, IllegalAccessException {
        List<E> result = new LinkedList<E>();

        for (String string : s.split(",")) {
            String value = string.trim();
            E bean = (E) protoBeanRepo.findByDtypeAndContent(cl.getSimpleName(), value);
            if (bean == null) {
                bean = cl.newInstance();
                bean.setContent(value);
            }
            result.add(bean);
        }

        return result;
    }

}
