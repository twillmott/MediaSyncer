package uk.org.willmott.mediasyncer.service;

import android.util.Log;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.TmdbHelper;
import com.uwetrottmann.tmdb2.entities.FindResults;
import com.uwetrottmann.tmdb2.entities.TvShow;
import com.uwetrottmann.tmdb2.entities.TvShowComplete;
import com.uwetrottmann.tmdb2.enumerations.ExternalSource;

import java.io.IOException;
import java.util.List;

import uk.org.willmott.mediasyncer.model.Series;

/**
 * Service for interacting with the movie databse api
 * Created by tomwi on 18/10/2016.
 */
public class TmdbService {

    private static String LOGTAG = TmdbService.class.getSimpleName();

    private Tmdb tmdb = new Tmdb("5475520bdcb65a7d746b9a582cad6ccf");

    /**
     * Updates a list of series information with TMDB data.
     */
    public List<Series> updateSeriesInfo(List<Series> serieses) {

        for (Series series : serieses) {

            TvShowComplete tvShow;

            // If tmdb id is blank. We need to find the show from another id.
            try {
                if (series.getTmdbId() == null || series.getTmdbId() == 0) {
                    FindResults findResults = null;
                    if (series.getImdbId() != null && series.getImdbId().equals("")) {
                        findResults = tmdb.findService().find(series.getImdbId(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (series.getTvrageId() != null && series.getTvrageId() != 0 && findResults.tv_results.isEmpty()) {
                        findResults = tmdb.findService().find(series.getTvrageId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (series.getTvdbId() != null && series.getTvdbId() != 0 && findResults.tv_results.isEmpty()) {
                        findResults = tmdb.findService().find(series.getTvdbId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (findResults.tv_results.isEmpty()) {
                        throw new UnsupportedOperationException();
                    }

                    series.setTmdbId(findResults.tv_results.iterator().next().id);
                }

                tvShow = tmdb.tvService().tv(series.getTmdbId(), "en", null).execute().body();

                series.setOverview(tvShow.overview); // Override the overview.
                series.setThumbnailUrl("http://image.tmdb.org/t/p/w154" + tvShow.images.posters.iterator().next().file_path);
                series.setBannerUrl("http://image.tmdb.org/t/p/w600" + tvShow.images.backdrops.iterator().next().file_path);

            } catch (Exception e) {
                Log.w(LOGTAG, "Unable to find any results for " + series.getTitle());
            }
        }
        return serieses;
    }
}
