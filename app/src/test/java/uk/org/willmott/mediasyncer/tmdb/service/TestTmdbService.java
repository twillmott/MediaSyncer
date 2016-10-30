package uk.org.willmott.mediasyncer.tmdb.service;

import com.google.common.collect.Lists;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.TvSeason;

import org.junit.Test;

import java.util.ArrayList;

import uk.org.willmott.mediasyncer.model.Episode;
import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.TmdbService;

/**
 * Created by tomwi on 18/10/2016.
 */

public class TestTmdbService {

    TmdbService tmdbService = new TmdbService();

    @Test
    public void testSeriesUpdate() {
        ArrayList<Series> serieses = new ArrayList();
        Series series = new Series();
        series.setTitle("First Dates");
        series.setTvdbId(270907);
//        series.setTmdbId(66001);

        ArrayList<Season> seasons = new ArrayList<>();
        Season season = new Season();
//        season.setTmdbId(3573);
        season.setSeasonNumber(3);
        seasons.add(season);

        Episode episode = new Episode();
        episode.setEpisodeNumber(4);
        season.setEpisodes(Lists.newArrayList(episode));

        series.setSeasons(seasons);

        tmdbService.updateSeriesInfo(series);
    }

//    @Test
//    public void testSeasonUpdate() {
//        Series series = new Series();
//        series.setTmdbId(1396); // Breaking bad
//        ArrayList<Season> seasons = new ArrayList<>();
//        Season season = new Season();
//        season.setTmdbId(3573);
//        season.setSeasonNumber(3);
//        seasons.add(season);
//        tmdbService.updateSeasonInfo(seasons, series);
//    }

//    @Test
//    public void testEpisodeUpdate() {
//        Series series = new Series();
//        series.setTmdbId(1396); // Breaking bad
//        Season season = new Season();
//        season.setSeasonNumber(3);
//        ArrayList<Episode> episodes = new ArrayList<>();
//        Episode episode = new Episode();
//        episode.setEpisodeNumber(4);
//        episodes.add(episode);
//        tmdbService.updateEpisodeInfo(episodes, series, season);
//
//    }

//    @Test
//    public void testCallTMDBAPI() {
//        Tmdb tmdb = new Tmdb("5475520bdcb65a7d746b9a582cad6ccf");
//        Series series = new Series();
//        series.setTmdbId(1396); // Breaking bad
//        Season season = new Season();
//        season.setSeasonNumber(3);
//
//        TvSeason tvSeason = new TvSeason();
//
//        for (int i = 0; i < 50; i++) {
//            tmdbService.callTMDBAPI(tmdb.tvSeasonsService().season(series.getTmdbId(), season.getSeasonNumber(), "en", null), tvSeason);
//        }
//    }
}
