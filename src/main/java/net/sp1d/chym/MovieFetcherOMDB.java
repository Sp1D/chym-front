/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import net.sp1d.chym.abstractclasses.AbstractFetcher;
import net.sp1d.chym.entities.MovieFull;
import net.sp1d.chym.entities.Language;
import net.sp1d.chym.entities.Country;
import net.sp1d.chym.entities.Person;
import net.sp1d.chym.entities.Genre;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sp1d.chym.entities.Episode;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author che
 */
public class MovieFetcherOMDB extends AbstractFetcher {

    String mainURL = "http://www.omdbapi.com/";
    final int FETCH_TRIES = 3;
    HttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new GsonFactory();
    HttpRequestFactory requestFactory
            = transport.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(jsonFactory));
                }
            });
    URIBuilder uriBuilder = null;

    public MovieFetcherOMDB() {
        super();
        try {
            this.uriBuilder = new URIBuilder(mainURL);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MovieFetcherOMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    private MovieOMDB fetchMovieByTorrent(Torrent torrent) {
        return fetchMovieByTorrent(torrent, true);
    }

    private MovieOMDB fetchMovieByTorrent(Torrent torrent, boolean byYear) {
        if (torrent == null) {
            throw new IllegalArgumentException();
        }
        if (torrent.getType() != Type.MOVIE && torrent.getType() != Type.SERIES) {
            throw new IllegalArgumentException("Wrong torrent type, details: " + torrent);
        }
        if (torrent.getTitle() == null || torrent.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Torrent must have a title");
        }
        if (torrent.getYearStart() == null) {
            torrent.setYearStart("");
        }
        String type = torrent.getType() == Type.MOVIE ? "movie" : "series";
        MovieOMDB movie = null;
        URI uri = null;
        uriBuilder.clearParameters();
        try {
            if (byYear) {
                uri = uriBuilder
                        .addParameter("t", checkField(torrent.getTitle(), checkedFields.TITLE))
                        .addParameter("y", torrent.getYearStart())
                        .addParameter("plot", "full")
                        .addParameter("r", "json")
                        .addParameter("type", type)
                        .build();
            } else {
                uri = uriBuilder
                        .addParameter("t", checkField(torrent.getTitle(), checkedFields.TITLE))
                        .addParameter("plot", "full")
                        .addParameter("r", "json")
                        .addParameter("type", type)
                        .build();
            }
            GenericUrl url = new GenericUrl(uri);

            HttpRequest req = requestFactory.buildGetRequest(url);
            req.setNumberOfRetries(FETCH_TRIES);

            movie = req.execute().parseAs(MovieOMDB.class);

            System.out.println("fetched movie: " + movie.getTitle()+", url: "+url);            

        } catch (URISyntaxException | IOException e) {
        }
        return movie;
    }

    private EpisodeOMDB fetchEpisodeByTorrent(Torrent torrent) {
        return fetchEpisodeByTorrent(torrent, true);
    }

    private EpisodeOMDB fetchEpisodeByTorrent(Torrent torrent, boolean byYear) {
        if (torrent == null) {
            throw new IllegalArgumentException();
        }
        if (torrent.getType() != Type.EPISODE) {
            throw new IllegalArgumentException("Wrong torrent type");
        }
        if (torrent.getTitle() == null || torrent.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Torrent must have a title");
        }
        if (torrent.getSeason() == 0 || torrent.getEpisode() == 0) {
            throw new IllegalArgumentException("Torrent must have a season and episode numbers");
        }
        if (torrent.getYearStart() == null) {
            torrent.setYearStart("");
        }
        EpisodeOMDB episode = null;
        URI uri = null;
        uriBuilder.clearParameters();
        try {
            if (byYear) {
                uri = uriBuilder
                        .addParameter("t", checkField(torrent.getTitle(), checkedFields.TITLE))
                        .addParameter("y", torrent.getYearStart())
                        .addParameter("plot", "full")
                        .addParameter("r", "json")
                        .addParameter("season", String.valueOf(torrent.getSeason()))
                        .addParameter("episode", String.valueOf(torrent.getEpisode()))
                        .addParameter("type", "episode")
                        .build();
            } else {
                uri = uriBuilder
                        .addParameter("t", checkField(torrent.getTitle(), checkedFields.TITLE))
                        .addParameter("plot", "full")
                        .addParameter("r", "json")
                        .addParameter("season", String.valueOf(torrent.getSeason()))
                        .addParameter("episode", String.valueOf(torrent.getEpisode()))
                        .addParameter("type", "episode")
                        .build();
            }
            GenericUrl url = new GenericUrl(uri);
            HttpRequest req = requestFactory.buildGetRequest(url);
            req.setNumberOfRetries(FETCH_TRIES);

            episode = req.execute().parseAs(EpisodeOMDB.class);

            System.out.println("fetched episode: " + episode.getTitle()+", url: "+url);            

        } catch (URISyntaxException | IOException e) {
        }

        return episode;
    }

    private MovieFull convertMovie(MovieOMDB m) {
        if (m == null || m.getTitle() == null || m.getTitle().isEmpty()) {
            return null;
        }
        try {
            MovieFull mf = new MovieFull();
            mf.setTitle(m.getTitle());
            mf.setActors(convertStringToStringBeans(m.getActors(), Person.class));
//            mf.setAwards(m.getAwards());
            mf.setCountrys(convertStringToStringBeans(m.getCountry(), Country.class));
//            mf.setDirectors(convertStringToStringBeans(m.getDirector(), Person.class));
            mf.setGenres(convertStringToStringBeans(m.getGenre(), Genre.class));
            mf.setImdbID(m.getImdbID());

            double rating = 0;
            try {
                rating = Double.valueOf(m.getImdbRating());
            } catch (Exception e) {
            }
            mf.setImdbRating(rating);

            String vs = m.getImdbVotes();
            vs = vs.replace(",", "");
            int votes = 0;
            try {
                votes = Integer.valueOf(vs);
            } catch (Exception e) {
            }
            mf.setImdbVotes(votes);
//            mf.setLanguages(convertStringToStringBeans(m.getLanguage(), Language.class));
//            mf.setMetascore(m.getMetascore());
            mf.setPlot(m.getPlot());
            mf.setPoster(m.getPoster());
            mf.setRated(m.getRated());
            mf.setReleased(m.getReleased());
//            mf.setResponce(m.getResponce());
            mf.setRuntime(m.getRuntime());

//            MovieType mt = new MovieType();
//            mt.setValue(m.getType());
//            mf.setType(mt);
//            mf.setWriters(convertStringToStringBeans(m.getWriter(), Person.class));
            String ys = m.getYear();
            int beg = 0, end = 0;
            try {
                ys = ys.trim().replaceAll("[A-Za-z\\s]", "");
                String[] ar = ys.split("[–-]");
                beg = Integer.valueOf(ar[0]);
                end = Integer.valueOf(ar[1]);
            } catch (Exception e) {
            }
            mf.setYearEnd(end);
            mf.setYearStart(beg);

            System.out.println("converted " + mf.getTitle());
            return mf;
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.getLogger(MovieFetcherOMDB.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    private Episode convertEpisode(EpisodeOMDB m) {
        if (m == null || m.getTitle() == null || m.getTitle().isEmpty()) {
            return null;
        }
        try {
            Episode mf = new Episode();
            mf.setTitle(m.getTitle());
            mf.setActors(convertStringToStringBeans(m.getActors(), Person.class));
//            mf.setAwards(m.getAwards());
            mf.setCountrys(convertStringToStringBeans(m.getCountry(), Country.class));
//            mf.setDirectors(convertStringToStringBeans(m.getDirector(), Person.class));
            mf.setGenres(convertStringToStringBeans(m.getGenre(), Genre.class));
            mf.setImdbID(m.getImdbID());

            float rating = 0f;
            try {
                rating = Float.valueOf(m.getImdbRating());
            } catch (Exception e) {
            }
            mf.setImdbRating(rating);

            String vs = m.getImdbVotes();
            vs = vs.replace(",", "");
            int votes = 0;
            try {
                votes = Integer.valueOf(vs);
            } catch (Exception e) {
            }
            mf.setImdbVotes(votes);
//            mf.setLanguages(convertStringToStringBeans(m.getLanguage(), Language.class));
//            mf.setMetascore(m.getMetascore());
            mf.setPlot(m.getPlot());
            mf.setPoster(m.getPoster());
            mf.setRated(m.getRated());
            mf.setReleased(m.getReleased());
//            mf.setResponce(m.getResponce());
            mf.setRuntime(m.getRuntime());

//            MovieType mt = new MovieType();
//            mt.setValue(m.getType());
//            mf.setType(convertStringToStringBeans(m.getType(), MovieType.class).get(0));
//            mf.setWriters(convertStringToStringBeans(m.getWriter(), Person.class));
            String ys = m.getYear();
            int beg = 0, end = 0;
            try {
                ys = ys.trim().replaceAll("[A-Za-z\\s]", "");
                String[] ar = ys.split("[–-]");
                beg = Integer.valueOf(ar[0]);
                end = Integer.valueOf(ar[1]);
            } catch (Exception e) {
            }
            mf.setYearEnd(end);
            mf.setYearStart(beg);
            try {
                mf.setSeason(Integer.valueOf(m.getSeason()));
            } catch (NumberFormatException e) {
//                mf.setSeason(0);
                return null;
            }
            try {
                mf.setEpisode(Integer.valueOf(m.getEpisode()));
            } catch (NumberFormatException e) {
//                mf.setEpisode(0);
                return null;
            }
            mf.setSeriesID(m.getSeriesID());

            System.out.println("Converted " + mf.getTitle());
            return mf;
        } catch (InstantiationException | IllegalAccessException | NumberFormatException e) {
            Logger.getLogger(MovieFetcherOMDB.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public MovieFull getMovieByTorrent(Torrent torrent) {

        return convertMovie(fetchMovieByTorrent(torrent));

    }

    public MovieFull getMovieByTorrent(Torrent torrent, boolean byYear) {

        return convertMovie(fetchMovieByTorrent(torrent, byYear));

    }

    @Override
    public Episode getEpisodeByTorrent(Torrent torrent) {

        return convertEpisode(fetchEpisodeByTorrent(torrent));

    }
    @Override
    public Episode getEpisodeByTorrent(Torrent torrent, boolean byYear) {

        return convertEpisode(fetchEpisodeByTorrent(torrent, byYear));

    }

    @Override
    public List<MovieFull> getMoviesListByTorrents(List<Torrent> torrents) {
        List<MovieFull> movies = new LinkedList<>();
        MovieFull movie;
        for (Torrent torrent : torrents) {
            movie = getMovieByTorrent(torrent);
            if (movie != null) {
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public List<Episode> getEpisodesListByTorrents(List<Torrent> torrents) {
        List<Episode> episodes = new LinkedList<>();
        Episode episode;
        for (Torrent torrent : torrents) {
            episode = getEpisodeByTorrent(torrent);
            if (episode != null) {
                episodes.add(episode);
            }
        }
        return episodes;
    }

}
