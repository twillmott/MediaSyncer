package uk.org.willmott.mediasyncer.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.FindResults;
import com.uwetrottmann.tmdb2.entities.TvEpisode;
import com.uwetrottmann.tmdb2.entities.TvResultsPage;
import com.uwetrottmann.tmdb2.entities.TvSeason;
import com.uwetrottmann.tmdb2.entities.TvShowComplete;
import com.uwetrottmann.tmdb2.enumerations.ExternalSource;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import uk.org.willmott.mediasyncer.data.access.SeriesAccessor;
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
     * Updates a single series with TMDB data.
     */
    public Series updateSeriesInfo(Series series) {

        TvShowComplete tvShow;

        // If tmdb id is blank. We need to find the show from another id.
        try {
            if (series.getTmdbId() == null || series.getTmdbId() == 0) {
                TvResultsPage tvResultsPage = (TvResultsPage) (callTMDBAPI(tmdb.searchService().tv(series.getTitle(), 1, "en", null, null), new TvResultsPage()));
                if (tvResultsPage.results.isEmpty()) {
                    FindResults findResults = null;
                    if (series.getTvdbId() != null && series.getTvdbId() != 0 && (findResults == null || findResults.tv_results.isEmpty())) {
                        findResults = (FindResults) callTMDBAPI(tmdb.findService().find(series.getTvdbId().toString(), ExternalSource.IMDB_ID, "en"), findResults);
                    }
                    if (series.getImdbId() != null && !series.getImdbId().equals("")) {
                        findResults = (FindResults) callTMDBAPI(tmdb.findService().find(series.getImdbId(), ExternalSource.IMDB_ID, "en"), findResults);
                    }
                    if (series.getTvrageId() != null && series.getTvrageId() != 0 && (findResults == null || findResults.tv_results.isEmpty())) {
                        findResults = (FindResults) callTMDBAPI(tmdb.findService().find(series.getTvrageId().toString(), ExternalSource.IMDB_ID, "en"), findResults);
                    }
                    if ((findResults != null || !findResults.tv_results.isEmpty())) {
                        series.setTmdbId(findResults.tv_results.get(0).id);
                    }
                } else {
                    series.setTmdbId(tvResultsPage.results.get(0).id); // We want a NPE to be thrown
                }
            }

            // Get the complete show details.
            tvShow = (TvShowComplete) callTMDBAPI(tmdb.tvService().tv(series.getTmdbId(), "en", null), new TvShowComplete());

            // Populate the model
            series.setOverview(tvShow.overview);
            series.setThumbnailUrl("http://image.tmdb.org/t/p/w500" + tvShow.poster_path);
            series.setBannerUrl("http://image.tmdb.org/t/p/w600" + tvShow.backdrop_path);
            series.setLastTmdbUpdate(System.currentTimeMillis());

            // Update all the series seasons
            series.setSeasons(updateSeasonInfo(series.getSeasons(), tvShow));

        } catch (Exception e) {
            Log.w(LOGTAG, "Unable to find any results for " + series.getTitle() + " " + e.getMessage());
        }
        return series;
    }


    /**
     * Updates a list of seasons with TMDB data. All seasons must belong to the same series.
     */
    private List<Season> updateSeasonInfo(List<Season> seasons, TvShowComplete series) {

        // Get a map of the supported seasons and season numbers (java8replace)
        Map<Integer, TvSeason> supportedSeasons = Maps.uniqueIndex(series.seasons, new Function<TvSeason, Integer>() {
            @Override
            public Integer apply(TvSeason input) {
                return input.season_number;
            }
        });

        for (Season season : seasons) {

            // Check that TMDB actually has this season (for some reason it only has certain seasons for some TV shows.
            if (supportedSeasons.get(season.getSeasonNumber()) == null) {
                Log.i(LOGTAG, "TMDB does not include information for " + series.name + " Season " + season.getSeasonNumber() + ". Skipping this entry.");
                continue;
            }

            season.setTmdbId(supportedSeasons.get(season.getSeasonNumber()).id);

            // Unfortunately the season that we got from the tv show doesn't contain all info. So get
            // it again from the API.
            TvSeason tvSeason = (TvSeason) callTMDBAPI(tmdb.tvSeasonsService().season(series.id, season.getSeasonNumber(), "en", null), new TvSeason());
            ;

            if (tvSeason == null) {
                Log.w(LOGTAG, "Unable to find any results for " + series.id + " Season " + Integer.toString(season.getSeasonNumber()));
            } else {
//                season.setOverview(tvSeason.overview); // Override the overview.
                season.setThumbnailUrl(tvSeason.poster_path == null ? null : "http://image.tmdb.org/t/p/w500" + tvSeason.poster_path);
//                season.setBannerUrl(); // TMDB doesn't provide season banners.
                season.setLastTmdbUpdate(System.currentTimeMillis());

                // Now update all of the episodes
                season.setEpisodes(updateEpisodeInfo(season.getEpisodes(), tvSeason, series.name));
            }
        }
        return seasons;
    }


    /**
     * Updates a list of episodes with TMDB data. All episodes must belong to the same supplied season.
     */
    private List<Episode> updateEpisodeInfo(List<Episode> episodes, TvSeason season, String seriesName) {

        // Get a map of the supported seasons and season numbers (java8replace)
        Map<Integer, TvEpisode> supportedEpisodes = Maps.uniqueIndex(season.episodes, new Function<TvEpisode, Integer>() {
            @Override
            public Integer apply(TvEpisode input) {
                return input.episode_number;
            }
        });

        for (Episode episode : episodes) {

            // Check that TMDB actually has this season (for some reason it only has certain seasons for some TV shows.
            if (supportedEpisodes.get(episode.getEpisodeNumber()) == null) {
                Log.i(LOGTAG, "TMDB does not include information for " + seriesName
                        + " Season " + season.season_number
                        + " Episode " + episode.getEpisodeNumber() + ". Skipping this entry.");
                continue;
            }

            TvEpisode tvEpisode = supportedEpisodes.get(episode.getEpisodeNumber());

            // Fortunately the episode that we got from the tmdb season DOES contain all info. So
            // there is no need to call the API again.
            episode.setTmdbId(tvEpisode.id);
            episode.setOverview(tvEpisode.overview); // Override the overview.
            episode.setThumbnailUrl(tvEpisode.still_path == null ? null : "http://image.tmdb.org/t/p/original" + tvEpisode.still_path);
            episode.setBannerUrl(tvEpisode.still_path == null ? null : "http://image.tmdb.org/t/p/original" + tvEpisode.still_path);
            episode.setLastTmdbUpdate(System.currentTimeMillis());
            episode.setAiredOn(tvEpisode.air_date.getTime());
        }
        return episodes;
    }


    /**
     * Pass in the call so that the 40 calls per second limit is handled.
     */
    private Object callTMDBAPI(Call call, Object returnObject) {
        Response response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            Log.e(LOGTAG, "Error calling TMDB database with exception: " + e.getMessage());
            return returnObject;
        }

        if (response.isSuccessful()) {
            return response.body();
        } else {
            if (response.code() == 429) { // Error code for more than 40 calls per second.
                try {
                    Log.i(LOGTAG, "TMDB api limit reached, waiting " + response.headers().get("Retry-After") + " seconds.");
                    Thread.sleep(Integer.parseInt(response.headers().get("Retry-After")) * 1050);
                } catch (InterruptedException e) {
                    Log.e(LOGTAG, "Error waiting for TMDB service");
                    return returnObject;
                }
                return callTMDBAPI(call.clone(), returnObject);
            } else {
                Log.e(LOGTAG, "Error calling the TMDB service with error code:" + response.code());
                return returnObject;
            }
        }
    }

    public void populateBlankShows(Context context, RefreshCompleteListener refreshCompleteListener) {
        try {
            new UpdateAllSeriesInDB(context, refreshCompleteListener).execute();
        } catch (Exception e) {
            Log.e(LOGTAG, "Error" + e.getMessage());
        }
    }

    private class UpdateAllSeriesInDB extends AsyncTask<Void, Void, String> {

        RefreshCompleteListener refreshCompleteListener;
        Context context;

        public UpdateAllSeriesInDB(Context context, RefreshCompleteListener refreshCompleteListener) {
            this.refreshCompleteListener = refreshCompleteListener;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            SeriesAccessor accessor = new SeriesAccessor(context);
            int i = 0;
            for (Series series : accessor.getAllUnfilledTMDBSeries()) {
                accessor.writeAllSeriesToDatabase(Lists.newArrayList(updateSeriesInfo(series)));
                i++;
            }
            return "tmdbupdateall,TMDB service successfully updated " + i + "series";
        }

        @Override
        protected void onPostExecute(String string) {
            refreshCompleteListener.refreshComplete(string);
        }
    }
}
