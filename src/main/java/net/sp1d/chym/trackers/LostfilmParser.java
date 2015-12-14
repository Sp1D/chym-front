/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym.trackers;

import com.google.api.client.http.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import net.sp1d.chym.abstractclasses.AbstractParser;
import net.sp1d.chym.entities.Torrent;
import net.sp1d.chym.entities.Type;
import org.slf4j.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author che
 */
@Entity
@DiscriminatorValue(value = "lostfilm")
@Component
public class LostfilmParser extends AbstractParser {

    private static final long serialVersionUID = 3536886052049669125L;
    @Transient
    HttpRequestFactory reqF = new NetHttpTransport().createRequestFactory();

    @Transient
    static final String NAME = "LostfilmParserN1";
    private final String BASE_URL = "http://www.lostfilm.tv";
    private final String SHOWS_PAGE = "http://www.lostfilm.tv/serials.php";
    private final String RSS_URL = "http://www.lostfilm.tv/rssdd.xml";
    private final String SHOW_BASE_URL = "http://www.lostfilm.tv/browse.php?cat=";
    private final String UID_COOKIE = "1542955";
    private final String PASS_COOKIE = "83cc1f785c24263d300d03ca9545b4cc";

    public LostfilmParser() {
        super();
        setName(NAME);
    }

    private List<String> getPageContent(String stringUrl) {
        List<String> strings = new LinkedList<String>();
        GenericUrl gUrl;
        try {
            gUrl = new GenericUrl(new URL(stringUrl));

            HttpRequest req = reqF.buildGetRequest(gUrl);
            HttpResponse responce = req.execute();

            if (responce.getStatusCode() >= 300) {
                throw new IOException("HTTP error, status: " + responce.getStatusCode()
                        + ", " + responce.getStatusMessage());
            }
            Charset charset = responce.getContentCharset();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(responce.getContent(), charset))) {
                String line;
                do {
                    line = br.readLine();
                    if (line != null) {
                        strings.add(line);
                    }
                } while (line != null);
            }

        } catch (IOException e) {

        }
        return strings;
    }

    private String parseYearByLocalId(String localId) {
        String year = null;
        Pattern yearPattern = Pattern.compile("\\u0413\\u043E\\u0434\\s+\\u0432\\u044B\\u0445\\u043E\\u0434\\u0430:\\s*<span>(\\d{4})<\\/span><br\\s+\\/>");
        for (String line : getPageContent(SHOW_BASE_URL + localId)) {
            Matcher matcher = yearPattern.matcher(line);
            if (matcher.find()) {
                year = matcher.group(1);
                break;
            }
        }
        return year;
    }

    private boolean parseTorrentsOfLocalIdByEpisode(List<Torrent> torrents, String id, Integer season, Integer episode, String title) throws IOException {

        System.out.println("trying " + season + ":" + episode);

        Pattern redirectToLinkPattern = Pattern.compile("location\\.replace\\(\""
                + "(?<link>.+)\"\\);");

        StringBuffer q = new StringBuffer("");
        for (LostfilmQuality quality : LostfilmQuality.values()) {
            q = q.append(quality.getPattern());
            q = q.append("|");
        }

        q = q.deleteCharAt(q.length() - 1);
        Pattern linkAndQualityPattern = Pattern.compile("<a\\s+href=\""
                + "(?<link>.+)\"\\sstyle=\"font-size:18px;font-weight:bold;\">.*?(?<quality>" + q + ")<\\/a><br\\s*\\/>");

        String transitLink = String.format("http://www.lostfilm.tv/nrdr2.php?c=%s&s=%s&e=%s", id, season, episode);

        GenericUrl transitUrl = new GenericUrl(transitLink);
        HttpRequest transitRequest = reqF.buildGetRequest(transitUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setCookie("uid=" + UID_COOKIE + "; pass=" + PASS_COOKIE);
        transitRequest.setHeaders(headers);
        transitRequest.setFollowRedirects(true);
        HttpResponse transitResponce = transitRequest.execute();

        if (transitResponce.getStatusCode() >= 300) {
            throw new IOException("HTTP error, status: " + transitResponce.getStatusCode()
                    + ", " + transitResponce.getStatusMessage());
        }

        boolean wasFound = false;
        String retreLink = "";
        String respString = "";
        Matcher redirectLink = redirectToLinkPattern.matcher(transitResponce.parseAsString());
        transitResponce.disconnect();
        if (redirectLink.find()) {
            retreLink = redirectLink.group("link");
            GenericUrl retreUrl = new GenericUrl(retreLink);
            HttpRequest retreRequest = reqF.buildGetRequest(retreUrl);
            retreRequest.setHeaders(headers);

            HttpResponse retreResponse = retreRequest.execute();
            respString = retreResponse.parseAsString();
            retreResponse.disconnect();

            if (!respString.isEmpty()) {
                Matcher linkAndQ = linkAndQualityPattern.matcher(respString);
                if (!linkAndQ.find()) {
                    /*
                     if (episode < fullSeason) {
                     episode = fullSeason - 1;
                     return finished;
                     } else if (episode == fullSeason) {                        
                     finished = true;
                     return finished;
                     }
                     */
                    wasFound = false;
                    return wasFound;
                }
                linkAndQ.reset();
                while (linkAndQ.find()) {
                    Torrent t = new Torrent();
                    t.setTorrent(linkAndQ.group("link"));
                    t.setLocalId(id);
                    LostfilmQuality quality = LostfilmQuality.find(linkAndQ.group("quality"));
                    t.setQuality(quality);
                    t.setTracker(getTracker());
                    t.setEpisode(episode);
                    t.setSeason(season);
                    t.setType(Type.EPISODE);
                    t.setPartially(false);
                    t.setTitle(title);
                    torrents.add(t);
                    wasFound = true;
                }
            }
        } else {
            throw new IOException("Link not found at " + transitLink);
        }
        return wasFound;
    }

    private List<Torrent> parseTorrentsOfLocalId(String id, String title) {
        return parseTorrentsOfLocalId(id, 1, 1, title);
    }

    private List<Torrent> parseTorrentsOfLocalId(String localId, int initialSeason, int initialEpisode, String title) {
        List<Torrent> torrents = new LinkedList<>();
        int fullSeason = 99;
        int scanMax = fullSeason + 1;

        boolean wasFound = false;
        boolean finished = false;

        try {
            for (int season = initialSeason; season < scanMax && !finished; season++) {
                for (int episode = initialEpisode; episode < scanMax; episode++) {
                    wasFound = parseTorrentsOfLocalIdByEpisode(torrents, localId, season, episode, title);
                    if (wasFound) {
//                        torrents.addAll(torrents);
                    } else {
                        if (episode < fullSeason) {
                            episode = fullSeason - 1;
                            continue;
                        } else if (episode == fullSeason) {
                            finished = true;
                        }

                    }
                }
            }
        } catch (IOException iOException) {

        }

        return torrents;

    }

    @Override
    public List<Torrent> parseTorrentsByTitle(String name) {
        System.out.println("Trying to get torrents of " + name);
        List<Torrent> torrents = null;
        List<String> mainPage = getPageContent(SHOWS_PAGE);
        String nameUpperCase = name.toUpperCase();

        Pattern idAndNamePattern = Pattern.compile("<a href=\""
                + "(?<link>\\/browse\\.php\\?cat="
                + "(?<id>\\d+)).+<br><span>\\("
                + "(?<title>.+)\\)<\\/span><\\/a>");

        String local_id, title, link;
        for (String line : mainPage) {
            Matcher m = idAndNamePattern.matcher(line);
            if (m.find() && nameUpperCase.equals(m.group("title").toUpperCase())) {
                local_id = m.group("id");
                title = m.group("title");
//                link = m.group("link");
                torrents = parseTorrentsOfLocalId(local_id, title);
                if (torrents.isEmpty()) {
                    return Collections.EMPTY_LIST;
                }
                for (Torrent torrent : torrents) {
//                    torrent.setTitle(title);
                    System.out.println("parsed " + torrent.getLocalId() + ", " + torrent.getTitle() + " "
                            + torrent.getSeason() + ":" + torrent.getEpisode() + " " + torrent.getQuality() + " " + torrent.getTorrent());
                }
                break;
            }
        }

        return torrents;
    }

    @Override
    public List<Torrent> parseTorrentsByCoverTorrent(Torrent cover) {
        System.out.println("Trying to get torrents of " + cover.getTitle());
        List<Torrent> torrents = null;

        torrents = parseTorrentsOfLocalId(cover.getLocalId(), cover.getTitle());
        if (torrents.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        for (Torrent torrent : torrents) {
            torrent.setYearStart(cover.getYearStart());
            System.out.println("parsed " + torrent.getLocalId() + ", " + torrent.getTitle() + " "
                    + torrent.getSeason() + ":" + torrent.getEpisode() + " " + torrent.getQuality() + " " + torrent.getTorrent());
        }

        return torrents;
    }

    @Override
    public List<Torrent> parseAllTorrentsCovers() {

        List<Torrent> movies = new LinkedList<>();
        List<String> page = new LinkedList<>();
        GenericUrl gUrl;
        try {
            gUrl = new GenericUrl(new URL(SHOWS_PAGE));

            HttpRequest req = reqF.buildGetRequest(gUrl);
            HttpResponse responce = req.execute();

            if (responce.getStatusCode() >= 300) {
                throw new IOException("HTTP error, status: " + responce.getStatusCode()
                        + ", " + responce.getStatusMessage());
            }
            Charset charset = responce.getContentCharset();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(responce.getContent(), charset))) {
                String line;
                Boolean copy = false;
                do {
                    line = br.readLine();
                    if (!copy && line.contains("<div class=\"bb\">")) {
                        copy = true;
                    }
                    if (copy && line.contains("</div>")) {
                        break;
                    }
                    if (copy) {
                        page.add(line);
                    }
                } while (line != null || "".equals(line));
            } catch (IOException ex) {

            }

            responce.disconnect();
            Pattern idAndTitlePattern = Pattern.compile("<a\\shref=\"\\/browse\\.php\\?cat="
                    + "(?<id>\\d+)\".+<br><span>\\("
                    + "(?<title>.+)\\)<\\/span><\\/a>");

            String title;
            for (String s : page) {
                Matcher m = idAndTitlePattern.matcher(s);

                if (m.find()) {
                    title = new String(m.group("title").getBytes("utf-8"));
                    Torrent t = new Torrent();
                    t.setTitle(title);
                    t.setLocalId(m.group("id"));
                    t.setTracker(getTracker());
                    t.setType(Type.SERIES);
                    t.setYearStart(parseYearByLocalId(m.group("id")));

                    movies.add(t);

                }

            }

        } catch (IOException e) {

        }

        return movies;
    }

//    public Torrent parseTorrentCoverByTitle(String queryTitle) {
//        Torrent cover = null;
//        List<String> page = getPageContent(SHOWS_PAGE);
//
//        Pattern idAndTitlePattern = Pattern.compile("<a\\shref=\"\\/browse\\.php\\?cat="
//                + "(?<id>\\d+)\".+<br><span>\\("
//                + "(?<title>.+)\\)<\\/span><\\/a>");
//
//        String title;
//        for (String s : page) {
//            Matcher m = idAndTitlePattern.matcher(s);
//
//            if (m.find()) {
//                title = new String(m.group("title").getBytes("utf-8"));
//                cover = new Torrent();
//                cover.setTitle(title);
//                cover.setLocalId(m.group("id"));
//                cover.setTracker(getTracker());
//                cover.setType(Type.SERIES);
//                cover.setYearStart(parseYearByLocalId(m.group("id")));
//
//                break;
//            }
//        }
//
//        return cover;
//    }

    private String getRssContent() {
        GenericUrl url = new GenericUrl(RSS_URL);
        HttpRequest request = null;
        HttpResponse responce = null;
        String feedContent = null;
        try {
            request = reqF.buildGetRequest(url);
            responce = request.execute();
            feedContent = responce.parseAsString();
            responce.disconnect();
        } catch (IOException ex) {

        }
        return feedContent;
    }

    @Override
    public Set<String> parseRssUpdates() {
        Set<String> titles = new HashSet<>();
        String feedContent = getRssContent();
        if (!feedContent.isEmpty()) {
            Pattern titlePattern = Pattern.compile("<title>.*\\((.+)\\).*\\(.*\\).*\\(.*\\).*<\\/title>");
            Matcher titleMatcher = titlePattern.matcher(feedContent);
            while (titleMatcher.find()) {
                titles.add(titleMatcher.group(1));
            }
        }
        return titles;
    }

    @Override
    public String parseRssLastBuildDate() {
        String lastBuildDate = null;
        Pattern lastBuildDatePattern = Pattern.compile("<lastBuildDate>(.*)<\\/lastBuildDate>");

        String feedContent = getRssContent();
        Matcher matcher = lastBuildDatePattern.matcher(feedContent);
        if (matcher.find()) {
            lastBuildDate = matcher.group(1);
        }
        return lastBuildDate;
    }

    @Override
    public List<Torrent> parseUpdatesByTitle(String title) {
        List<Torrent> torrents = new LinkedList<>();

        if (title == null || title.isEmpty()) {
            return new LinkedList<>();
        }

        String localId = getTracker().getTorrentLocalIdByTitle(title);
        int lastSeason = getTracker().getLastSeasonByLocalId(localId);
        int lastEpisode = getTracker().getLastEpisodeByLocalId(localId);
        if ((localId == null || localId.isEmpty()) || lastEpisode == 0 || lastSeason == 0) {
            return new LinkedList<>();
        }

        torrents = parseTorrentsOfLocalId(localId, lastSeason, lastEpisode + 1, title);

        return torrents;
    }

}
