package uk.org.willmott.mediasyncer.service;

import android.util.Log;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.TmdbHelper;
import com.uwetrottmann.tmdb2.entities.FindResults;
import com.uwetrottmann.tmdb2.entities.TvSeason;
import com.uwetrottmann.tmdb2.entities.TvShow;
import com.uwetrottmann.tmdb2.entities.TvShowComplete;
import com.uwetrottmann.tmdb2.enumerations.ExternalSource;

import java.io.IOException;
import java.util.List;

import uk.org.willmott.mediasyncer.model.Season;
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
                    if (series.getImdbId() != null && !series.getImdbId().equals("")) {
                        findResults = tmdb.findService().find(series.getImdbId(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (series.getTvrageId() != null && series.getTvrageId() != 0 && (findResults == null || findResults.tv_results.isEmpty())) {
                        findResults = tmdb.findService().find(series.getTvrageId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (series.getTvdbId() != null && series.getTvdbId() != 0 && (findResults == null || findResults.tv_results.isEmpty())) {
                        findResults = tmdb.findService().find(series.getTvdbId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if ((findResults == null || findResults.tv_results.isEmpty())) {
                        series.setTmdbId(tmdb.searchService().tv(series.getTitle(), 1, "en", null, null).execute().body().results.get(0).id);
                    }

                    series.setTmdbId(findResults.tv_results.get(0).id);
                }

                tvShow = tmdb.tvService().tv(series.getTmdbId(), "en", null).execute().body();

                series.setOverview(tvShow.overview); // Override the overview.
                series.setThumbnailUrl("http://image.tmdb.org/t/p/w154" + tvShow.poster_path);
                series.setBannerUrl("http://image.tmdb.org/t/p/w600" + tvShow.backdrop_path);

            } catch (Exception e) {
                Log.w(LOGTAG, "Unable to find any results for " + series.getTitle());
            }
        }
        return serieses;
    }

    /**
     * Updates a list of seasons with TMDB data. All seasons must belong to the same series.
     */
    public List<Season> updateSeasonInfo(List<Season> seasons, Series series) {

        if (series.getTmdbId() == null || series.getTmdbId() == 0) {
            Log.w(LOGTAG, "No tmdb ID for " + series.getTitle() + ", therefore season information can't be attained.");
            return seasons;
        }

        for (Season season : seasons) {

            TvSeason tvSeason = null;

            // If tmdb id is blank. We need to find the season from another id.
            try {
                if (season.getTmdbId() == null || season.getTmdbId() == 0) {
                    FindResults findResults = null;
                    if (season.getImdbId() != null && !season.getImdbId().equals("")) {
                        findResults = tmdb.findService().find(season.getImdbId(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (season.getTvrageId() != null && season.getTvrageId() != 0 && (findResults == null || findResults.tv_season_results.isEmpty())) {
                        findResults = tmdb.findService().find(season.getTvrageId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (season.getTvdbId() != null && season.getTvdbId() != 0 && (findResults == null || findResults.tv_season_results.isEmpty())) {
                        findResults = tmdb.findService().find(season.getTvdbId().toString(), ExternalSource.IMDB_ID, "en").execute().body();
                    }
                    if (findResults.tv_results.isEmpty()) {
                        throw new UnsupportedOperationException();
                    }

                    tvSeason = findResults.tv_season_results.iterator().next();
                } else {
                    tvSeason = tmdb.tvSeasonsService().season(series.getTvdbId(), season.getSeasonNumber(), "en", null).execute().body();

                }

//                season.setOverview(tvSeason.overview); // Override the overview.
                season.setThumbnailUrl("http://image.tmdb.org/t/p/w154" + tvSeason.poster_path);
                season.setBannerUrl("http://image.tmdb.org/t/p/w600" + tmdb.tvSeasonsService().images(series.getTmdbId(), season.getSeasonNumber(), "en").execute().body().backdrops.iterator().next());

            } catch (Exception e) {
                Log.w(LOGTAG, "Unable to find any results for " + series.getTitle() + " Season " + Integer.toString(season.getSeasonNumber()));
            }
        }
        return seasons;
    }
}
