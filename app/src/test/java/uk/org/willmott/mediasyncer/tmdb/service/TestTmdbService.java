package uk.org.willmott.mediasyncer.tmdb.service;

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
        series.setTitle("Police Interceptors");
        serieses.add(series);
        tmdbService.updateSeriesInfo(serieses);
    }

    @Test
    public void testSeasonUpdate() {
        Series series = new Series();
        series.setTmdbId(1396); // Breaking bad
        ArrayList<Season> seasons = new ArrayList<>();
        Season season = new Season();
        season.setTmdbId(3573);
        season.setSeasonNumber(3);
        seasons.add(season);
        tmdbService.updateSeasonInfo(seasons, series);
    }

    @Test
    public void testEpisodeUpdate() {
        Series series = new Series();
        series.setTmdbId(1396); // Breaking bad
        Season season = new Season();
        season.setSeasonNumber(3);
        ArrayList<Episode> episodes = new ArrayList<>();
        Episode episode = new Episode();
        episode.setEpisodeNumber(4);
        episodes.add(episode);
        tmdbService.updateEpisodeInfo(episodes, series, season);

    }
}
