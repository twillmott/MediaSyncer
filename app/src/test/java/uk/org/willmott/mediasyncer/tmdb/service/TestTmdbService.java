package uk.org.willmott.mediasyncer.tmdb.service;

import org.junit.Test;

import java.util.ArrayList;

import uk.org.willmott.mediasyncer.model.Series;
import uk.org.willmott.mediasyncer.service.TmdbService;

/**
 * Created by tomwi on 18/10/2016.
 */

public class TestTmdbService {

    TmdbService tmdbService = new TmdbService();

    @Test
    public void test() {
        ArrayList<Series> serieses = new ArrayList();
        Series series = new Series();
        series.setImdbId("tt0182576");
        serieses.add(series);
        tmdbService.updateSeriesInfo(serieses);
    }
}
