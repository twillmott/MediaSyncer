package uk.org.willmott.mediasyncer.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.collect.Lists;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.AccessToken;
import com.uwetrottmann.trakt5.entities.BaseEpisode;
import com.uwetrottmann.trakt5.entities.BaseSeason;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Episode;
import com.uwetrottmann.trakt5.entities.HistoryEntry;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.entities.Username;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.HistoryType;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import uk.org.willmott.mediasyncer.R;
import uk.org.willmott.mediasyncer.data.access.EpisodeAccessor;
import uk.org.willmott.mediasyncer.data.access.SeriesAccessor;
import uk.org.willmott.mediasyncer.model.Season;
import uk.org.willmott.mediasyncer.model.Series;

/**
 * This is my version of an service for interfacing with Trakt. It is very heavily dependent on
 * UweTrottmann's helper.
 *
 * In order to instanciate this service, you either need to supply a context, to allow the service
 * to authenticate, or supply the code to continue to authenticate. Either method checks for
 * authentication first.
 */
public class TraktService {

    private static String CLIENT_ID = "0866b3b94e7873b57c5fb201291adb4ba601ab4a403bd3546ff6f38cf0a8fb84";
    private static String CLIENT_SECRET = "767159b9d53fe00d40b8e16e5172a785806b7e909570bf0360eade075d4d0a35";
    private static String LOG_TAG = TraktService.class.getSimpleName();
    public static String REDIRECT_URL = "mediasyncer://oauthresponse";

    private TraktV2 trakt = new TraktV2(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
    private String authenticationState;


    /**
     * Contructor for this class that checks the authentication. Supply a null code to check authentication,
     * supply a valid code to continue authentication. Context is required. This is used when we
     * don't know the access token.
     */
    public TraktService(Context context, String code) {
        checkAuthentication(context, code);
    }

    /**
     * Constructor for creating a trakt service when you know the access token.
     */
    public TraktService(String accessToken) { trakt.accessToken(accessToken); }

    private TraktV2 getTrakt() {
        return trakt;
    }

    public String getAccessToken() { return trakt.accessToken(); }

    /**
     * This service checks that we have authentication. If we don't have it, we get it and save it.
     * This method must always be called on app start up, and is called in the constructor of this
     * service.
     * @param context If the context is passed in, it means we're looking to create a new authorization.
     */
    private void checkAuthentication(Context context, String code) {
        try {

            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            if (code == null) {
                // Load the access and refresh tokens from shared preferences.
                String accessToken = sharedPref.getString(context.getString(R.string.trakt_access_token_preference), null);
                String refreshToken = sharedPref.getString(context.getString(R.string.trakt_refresh_token_preference), null);

                // If we have an access token, then we don't need any more authorisation.
                if (accessToken != null && refreshToken != null) {
                    trakt.accessToken(accessToken);
                    trakt.refreshToken(refreshToken);

                    // Refresh the token for ease of use and resave it.
                    Response<AccessToken> accessTokenResponse = new RefreshTraktToken(trakt).execute().get();
                    trakt.accessToken(accessTokenResponse.body().access_token);
                    trakt.refreshToken(accessTokenResponse.body().refresh_token);
                    sharedPref.edit().putString(context.getString(R.string.trakt_access_token_preference), accessTokenResponse.body().access_token).apply();
                    sharedPref.edit().putString(context.getString(R.string.trakt_refresh_token_preference), accessTokenResponse.body().refresh_token).apply();

                    Log.i(LOG_TAG, "Media Syncer successfully authorised with trakt.");
                    return;
                }

                // If we get to this stage, then we don't have a token, so need to re-authorise.

                // Start by getting a new code for this new authentication.
                OAuthClientRequest request = trakt.buildAuthorizationRequest("");

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(request.getLocationUri()));
                context.startActivity(intent);
            } else {
                // Then get a new token from the code, this will be if we're continuing the authentication.
                Response<AccessToken> accessTokenResponse = new RetrieveTraktToken(code, trakt).execute().get();
                trakt.accessToken(accessTokenResponse.body().access_token);
                trakt.refreshToken(accessTokenResponse.body().refresh_token);
                sharedPref.edit().putString(context.getString(R.string.trakt_access_token_preference), accessTokenResponse.body().access_token).apply();
                sharedPref.edit().putString(context.getString(R.string.trakt_refresh_token_preference), accessTokenResponse.body().refresh_token).apply();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Reauthorise trakt from fresh. This includes opening the browser.
     */
    public void reauthorise(Context context) {

        // Delete the auth keys from the shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().remove(context.getString(R.string.trakt_access_token_preference)).apply();
        sharedPref.edit().remove(context.getString(R.string.trakt_refresh_token_preference)).apply();

        checkAuthentication(context, null);
    }


    /**
     * Get all the watched shows for the authorised user.
     */
    public List<BaseShow> getShowWatchlist() {
        try {
            Response<List<BaseShow>> response = trakt.users().watchlistShows(Username.ME, Extended.DEFAULT_MIN).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            Log.e(LOG_TAG, response.errorBody().string());
            return new ArrayList<>();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Get all watched shows for the user.
     */
    public List<BaseShow> getShowWatched() {
        try {
            Response<List<BaseShow>> response = trakt.users().watchedShows(Username.ME, Extended.DEFAULT_MIN).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            Log.e(LOG_TAG, response.errorBody().string());
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all watched shows for the user.
     */
    public List<BaseShow> getShowCollection() {
        try {
            Response<List<BaseShow>> response = trakt.users().collectionShows(Username.ME, Extended.DEFAULT_MIN).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            Log.e(LOG_TAG, response.errorBody().string());
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all watched shows for the user.
     */
    public BaseShow getShowWatchedProgress(String showId) {
        try {
            Response<BaseShow> response = trakt.shows().watchedProgress(showId, false, false, Extended.FULLIMAGES).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get basic information about a show
     */
    public Show getShow(String showId) {
        try {
            Response<Show> response = trakt.shows().summary(showId, Extended.FULLIMAGES).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class RetrieveTraktToken extends AsyncTask<Void, Void, Response<AccessToken>> {

        private String code;
        private TraktV2 traktV2;

        private RetrieveTraktToken(String code, TraktV2 traktV2) {
            this.code = code;
            this.traktV2 = traktV2;
        }

        @Override
        protected Response<AccessToken> doInBackground(Void... voids) {
            try {
                return traktV2.exchangeCodeForAccessToken(code);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Given lists of shows, merge them to get a list of all shows in each list with no
     * duplicates sorted alphabetically if sorting is true.
     */
    public List<BaseShow> mergeShows(boolean sorting, final List<BaseShow>... shows) {
        final Map<String, BaseShow> showMap = new LinkedHashMap<>();

        // Go through all the lists and add them to the hash map in order to get only one instance.
        for (List<BaseShow> showList : shows) {
            for (BaseShow show : showList) {
                showMap.put(show.show.ids.trakt.toString().toLowerCase(), show);
            }
        }

        if (sorting) {
            // Return a sorted list
            return sortShowsAlphabetically( new ArrayList<>(showMap.values()));
        } else {
            // Return the unsorted list
            return new ArrayList<>(showMap.values());
        }
    }

    /**
     * Combine two base shows, if any of show A's properties are null, we will use
     * show B's property.
     */
    private BaseShow combineBaseShows(BaseShow showA, BaseShow showB) {
        BaseShow show = new BaseShow();

        show.next_episode = showA.next_episode == null ? showB.next_episode : showA.next_episode;
        show.show = showA.show == null ? showB.show : showA.show;
        show.aired = showA.aired == null ? showB.aired : showA.aired;
        show.completed = showA.completed == null ? showB.completed : showA.completed;
        show.hidden_seasons = showA.hidden_seasons == null ? showB.hidden_seasons : showA.hidden_seasons;
        show.last_collected_at = showA.last_collected_at == null ? showB.last_collected_at : showA.last_collected_at;
        show.last_watched_at = showA.last_watched_at == null ? showB.last_watched_at : showA.last_watched_at;
        show.listed_at = showA.listed_at == null ? showB.listed_at : showA.listed_at;
        show.next_episode = showA.next_episode == null ? showB.next_episode : showA.next_episode;
        show.plays = showA.plays == null ? showB.plays : showA.plays;
        show.completed = showA.completed == null ? showB.completed : showA.completed;

        return show;
    }

    /**
     * Sort the list of shows alphabetically.
     */
    private List<BaseShow> sortShowsAlphabetically(List<BaseShow> shows) {
        Collections.sort(shows, new Comparator<BaseShow>() {
            public int compare(BaseShow v1, BaseShow v2) {
                return v1.show.title.compareTo(v2.show.title);
            }
        });
        return shows;
    }


    private Series baseShowToSeries(BaseShow baseShow, List<Season> seasons) {
        return new Series(
                null,
                baseShow.show.title,
                baseShow.show.ids.trakt,
                baseShow.show.ids.tmdb,
                baseShow.show.ids.tvdb,
                baseShow.show.ids.tvrage,
                baseShow.show.ids.imdb,
                null,
                null,
                episodeMapper(baseShow.next_episode),
                seasons,
                null);
    }


    private Season seasonMapper(com.uwetrottmann.trakt5.entities.Season season, List<uk.org.willmott.mediasyncer.model.Episode> episodes) {
        Season seasonModel = new Season(
                null,
                season.ids.tmdb,
                season.ids.trakt,
                season.ids.tvdb,
                season.ids.tvrage,
                null,
                season.number,
                null,
                null,
                episodes);
        seasonModel.setEpisodeCount(episodes.size());

        return seasonModel;
    }


    private uk.org.willmott.mediasyncer.model.Episode episodeMapper(Episode episode) {

        // Episode can be null when the series mapper is trying to load the next episode.
        if (episode == null) {
            return null;
        }

        return new uk.org.willmott.mediasyncer.model.Episode(
                null,
                episode.ids.tmdb,
                episode.ids.trakt,
                episode.ids.tvdb,
                episode.ids.tvrage,
                episode.ids.imdb,
                episode.title,
                episode.number,
                null,
                null,
                null);
    }

    public void getAllShows(Context context, RefreshCompleteListener refreshCompleteListener) {
        try {
            new RefreshFullTvDatabase(context, refreshCompleteListener).execute();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading all shows from trakt" + e.getMessage());
        }
    }

    public void refreshAllEpisodeWatchedStatus(Context context, RefreshCompleteListener refreshCompleteListener) {
        try {
            new RefreshWatchedStatus(context, refreshCompleteListener).execute();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading all shows from trakt" + e.getMessage());
        }
    }

    public void refreshAllEpisodeCollectedStatus(Context context, RefreshCompleteListener refreshCompleteListener) {
        try {
            new RefreshCollectedStatus(context, refreshCompleteListener).execute();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error loading all shows from trakt" + e.getMessage());
        }
    }

    private class RefreshTraktToken extends AsyncTask<Void, Void, Response<AccessToken>> {

        TraktV2 trakt;

        RefreshTraktToken(TraktV2 trakt) {
            this.trakt = trakt;
        }


        @Override
        protected Response<AccessToken> doInBackground(Void... voids) {
            try {
                return trakt.refreshAccessToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * Collect all watched, collected and watchlist shows from Trakt.
     */
    private class RefreshFullTvDatabase extends AsyncTask<Void, Void, String> {

        RefreshCompleteListener refreshCompleteListener;
        Context context;

        public RefreshFullTvDatabase(Context context, RefreshCompleteListener refreshCompleteListener) {
            this.refreshCompleteListener = refreshCompleteListener;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            // Get all of the users shows, with the minimum information.
            // Get the users watchlist
            List<BaseShow> watchlistShows = getShowWatchlist();
            // Get the users watched shows.
            List<BaseShow> watchedShows = getShowWatched();
            // Get the users collection.
            List<BaseShow> collectedShows = getShowCollection();

            // Merge all the shows in to one list.
            List<BaseShow> mergedShows = mergeShows(true, watchlistShows, watchedShows, collectedShows);

            List<Series> showModels = new ArrayList<>();

            List<Series> shows = new ArrayList<>();
            for (BaseShow traktShow : mergedShows) {

                // Get the extra details of the show. Trakt doesn't give all details in the one api call.
                BaseShow fullShow = combineBaseShows(traktShow, getShowWatchedProgress(traktShow.show.ids.trakt.toString()));

                // Get all of the seasons for the show
                List<com.uwetrottmann.trakt5.entities.Season> traktSeasons = new ArrayList<>();
                try {
                    traktSeasons = getTrakt().seasons().summary(traktShow.show.ids.trakt.toString(), Extended.DEFAULT_MIN).execute().body();
                } catch (Exception e) {
                    Log.e("Trakt", e.getMessage());
                }

                List<Season> seasonModels = new ArrayList<>();
                // Go through each season and get its episodes
                for (com.uwetrottmann.trakt5.entities.Season traktSeason : traktSeasons) {
                    if (traktSeason.number != 0) {

                        List<Episode> traktEpisodes = new ArrayList<>();
                        try {
                            traktEpisodes = getTrakt().seasons().season(traktShow.show.ids.trakt.toString(), traktSeason.number, Extended.DEFAULT_MIN).execute().body();
                        } catch (Exception e) {
                            Log.e("Trakt", e.getMessage());
                        }

                        List<uk.org.willmott.mediasyncer.model.Episode> episodeModels = new ArrayList<>();
                        for (Episode traktEpisode : traktEpisodes) {
                            episodeModels.add(episodeMapper(traktEpisode));
                        }

                        seasonModels.add(seasonMapper(traktSeason, episodeModels));
                    }
                }

                showModels.add(baseShowToSeries(fullShow, seasonModels));
            }

            SeriesAccessor accessor = new SeriesAccessor(context);
            accessor.writeAllSeriesToDatabase(showModels);

            return "traktrefresh,Trakt service successfully grabbed " + showModels.size() + " series.";
        }

        @Override
        protected void onPostExecute(String string) {
            refreshCompleteListener.refreshComplete(string);
        }
    }

    /**
     * Refresh the watched status for all unwatched episodes
     */
    public class RefreshWatchedStatus extends AsyncTask<Void, Void, String> {

        RefreshCompleteListener refreshCompleteListener;
        Context context;

        public RefreshWatchedStatus(Context context, RefreshCompleteListener refreshCompleteListener) {
            this.refreshCompleteListener = refreshCompleteListener;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            // Get the last watched episode so that we know when to start our search for the next watched episodes.
            EpisodeAccessor episodeAccessor = new EpisodeAccessor(context);
            Long lastWatched = episodeAccessor.getLastWatchedEpisode();
            DateTime lastWatchedDateTime = new DateTime(0L);
            if (lastWatched != null) {
                lastWatchedDateTime = new DateTime(lastWatched);
            }

            List<HistoryEntry> traktEpisodes = null;
            try {
                Response<List<HistoryEntry>> response = trakt.users().history(Username.ME, HistoryType.EPISODES, 1, 99999, Extended.DEFAULT_MIN, lastWatchedDateTime, null).execute();

                if (response.code() == 503) {
                    return "trakterror,Unable to communicate with Trakt (error 503). Trakt may be down.";
                }
                traktEpisodes = response.body();

                if (traktEpisodes == null) {
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }

            List<uk.org.willmott.mediasyncer.model.Episode> episodes = Lists.newArrayList();
            for (HistoryEntry historyEntry : traktEpisodes) {
                uk.org.willmott.mediasyncer.model.Episode episode = new uk.org.willmott.mediasyncer.model.Episode();
                episode.setTraktId(historyEntry.episode.ids.trakt);
                episode.setLastWatched(historyEntry.watched_at.getMillis());
                episodeAccessor.updateTraktWatchedEpisode(episode);
            }

            return "traktwatchedprogress,Trakt watched progress updated :)";
        }

        @Override
        protected void onPostExecute(String string) {
            refreshCompleteListener.refreshComplete(string);
        }
    }


    /**
     * Refresh the collected status for all episodes
     */
    public class RefreshCollectedStatus extends AsyncTask<Void, Void, String> {

        RefreshCompleteListener refreshCompleteListener;
        Context context;

        public RefreshCollectedStatus(Context context, RefreshCompleteListener refreshCompleteListener) {
            this.refreshCompleteListener = refreshCompleteListener;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            // Get the last watched episode so that we know when to start our search for the next watched episodes.
            EpisodeAccessor episodeAccessor = new EpisodeAccessor(context);
            try {
                Response<List<BaseShow>> response = trakt.users().collectionShows(Username.ME, Extended.DEFAULT_MIN).execute();

                if (response.isSuccessful()) {
                    for (BaseShow baseShow : response.body()) {
                        if (baseShow.seasons == null) {
                            continue;
                        }
                        for (BaseSeason season : baseShow.seasons) {
                            if (season.episodes == null) {
                                continue;
                            }
                            for (BaseEpisode episode : season.episodes) {
                                episodeAccessor.markEpisodeAsCollected(baseShow.show.ids.trakt, season.number, episode.number, episode.collected_at.getMillis());
                            }
                        }

                    }
                } else if (response.code() == 503) {
                    return "trakterror,Unable to communicate with Trakt (error 503). Trakt may be down.";
                } else {
                    return "trakterror,Unable to get collected shows :(";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }

            return "traktcollectedprogress,Trakt collected progress updated :)";
        }

        @Override
        protected void onPostExecute(String string) {
            refreshCompleteListener.refreshComplete(string);
        }
    }
}
