package uk.org.willmott.mediasyncer.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.AccessToken;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.entities.Username;
import com.uwetrottmann.trakt5.enums.Extended;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import uk.org.willmott.mediasyncer.R;

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
     * supply a valid code to continue authentication. Context is required.
     */
    public TraktService(Context context, String code) {
        checkAuthentication(context, code);
    }

    public TraktV2 getTrakt() {
        return trakt;
    }

    /**
     * This service checks that we have authentication. If we don't have it, we get it and save it.
     * This method must always be called on app start up, and is called in the constructor of this
     * service.
     * @param context If the context is passed in, it means we're looking to create a new authorization.
     */
    public void checkAuthentication(Context context, String code) {
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
     * Get all the watched shows for the authorised user.
     */
    public List<BaseShow> getShowWatchlist() {
        try {
            Response<List<BaseShow>> response = trakt.users().watchlistShows(Username.ME, Extended.IMAGES).execute();
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
     * Get all watched shows for the user.
     */
    public List<BaseShow> getShowWatched() {
        try {
            Response<List<BaseShow>> response = trakt.users().watchedShows(Username.ME, Extended.IMAGES).execute();
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
     * Get all watched shows for the user.
     */
    public List<BaseShow> getShowCollection() {
        try {
            Response<List<BaseShow>> response = trakt.users().collectionShows(Username.ME, Extended.IMAGES).execute();
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

        public RetrieveTraktToken(String code, TraktV2 traktV2) {
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
    public BaseShow combineBaseShows(BaseShow showA, BaseShow showB) {
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
}