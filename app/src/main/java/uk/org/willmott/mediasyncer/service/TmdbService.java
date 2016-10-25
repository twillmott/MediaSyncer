package uk.org.willmott.mediasyncer.service;

import android.util.Log;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.FindResults;
import com.uwetrottmann.tmdb2.entities.TvEpisode;
import com.uwetrottmann.tmdb2.entities.TvSeason;
import com.uwetrottmann.tmdb2.entities.TvShowComplete;
import com.uwetrottmann.tmdb2.enumerations.ExternalSource;

import java.util.List;

import retrofit2.Response;
import uk.org.willmott.mediasyncer.model.Episode;
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
                    } else {
                        series.setTmdbId(findResults.tv_results.get(0).id);
                    }
                }

                tvShow = tmdb.tvService().tv(series.getTmdbId(), "en", null).execute().body();

                series.setOverview(tvShow.overview); // Override the overview.
                series.setThumbnailUrl("http://image.tmdb.org/t/p/w154" + tvShow.poster_path);
                series.setBannerUrl("http://image.tmdb.org/t/p/w600" + tvShow.backdrop_path);

                // Update all the series seasons
                series.setSeasons(updateSeasonInfo(series.getSeasons(), series));

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
                    // Get the TV Show from tmdb and then get the ID from the seasons
                    season.setTmdbId(tmdb.tvService().tv(series.getTmdbId(), "en", null).execute().body().seasons.get(season.getSeasonNumber() - 1).id);
                }

                Response<TvSeason> response = tmdb.tvSeasonsService().season(series.getTmdbId(), season.getSeasonNumber(), "en", null).execute();
                if (response.isSuccessful()) {
                    tvSeason = response.body();
                } else {
                    String responseError = response.errorBody().string();
                    if (responseError.contains("Your request count")) {
                        // We're only allowed 40 api calls every 10 seconds. So sleep for a bit.
                        Thread.sleep(10000);

                        Response<TvSeason> response = tmdb.tvSeasonsService().season(series.getTmdbId(), season.getSeasonNumber(), "en", null).execute();
                        if (response.isSuccessful()) {
                            tvSeason = response.body();
                        } else {
                            String responseError1 = response.errorBody().string();
                            if (responseError.contains("Your request count")) {

                            }
                        }

                    }
                }

                if (tvSeason != null) {
//                season.setOverview(tvSeason.overview); // Override the overview.
                    season.setThumbnailUrl(tvSeason.poster_path == null ? null : "http://image.tmdb.org/t/p/w154" + tvSeason.poster_path);
//                season.setBannerUrl(); // TMDB doesn't provide season banners.

                    // Now update all of the episodes
                } else {
                    Log.w(LOGTAG, "Unable to find any results for " + series.getTitle() + " Season " + Integer.toString(season.getSeasonNumber()));
                }


            } catch (Exception e) {
                Log.w(LOGTAG, "Unable to find any results for " + series.getTitle() + " Season " + Integer.toString(season.getSeasonNumber()));
            }
        }
        return seasons;
    }

    private void getTmdbSeason() {

    }

    /**
     * Updates a list of episodes with TMDB data. All episodes must belong to the same series and seson.
     */
    public List<Episode> updateEpisodeInfo(List<Episode> episodes, Series series, Season season) {

        if (series.getTmdbId() == null || series.getTmdbId() == 0) {
            Log.w(LOGTAG, "No tmdb ID for " + series.getTitle() + ", therefore episode information can't be attained.");
            return episodes;
        }

        for (Episode episode : episodes) {

            TvEpisode tvEpisode = null;

            // If tmdb id is blank. We need to find the season from another id.
            try {
                if (episode.getTmdbId() == null || episode.getTmdbId() == 0) {
                    // Get the TV Show from tmdb and then get the ID from the seasons
                    tvEpisode = tmdb.tvSeasonsService().season(series.getTmdbId(), season.getSeasonNumber(), "en", null).execute().body().episodes.get(episode.getEpisodeNumber() - 1);
                } else {
                    tvEpisode = tmdb.tvEpisodesService().episode(series.getTmdbId(), season.getSeasonNumber(), episode.getEpisodeNumber(), "en", null).execute().body();
                }
                episode.setOverview(tvEpisode.overview);
                episode.setThumbnailUrl("http://image.tmdb.org/t/p/w154" + tvEpisode.still_path);
                episode.setBannerUrl("http://image.tmdb.org/t/p/w600" + tvEpisode.still_path);
            } catch (Exception e) {
                Log.w(LOGTAG, "Unable to find any results for " + series.getTitle() + " Season " + Integer.toString(season.getSeasonNumber()) + " Episode " + Integer.toString(episode.getEpisodeNumber()));
            }
        }
        return episodes;
    }
}
