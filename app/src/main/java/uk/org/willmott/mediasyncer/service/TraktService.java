package uk.org.willmott.mediasyncer.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.AccessToken;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.Username;
import com.uwetrottmann.trakt5.enums.Extended;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

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


    public TraktService(Context context) {
        checkAuthentication(context, null);
    }

    public TraktService(String code)  {
        checkAuthentication(null, code);
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

            // TODO, save access tokens to database.

            // First we'll try refreshing the token that we may have.
            Response<AccessToken> accessToken = new RefreshTraktToken(trakt).execute().get();

            // If this doesn't work, we need to reauthorise.
            if (context != null && code == null) {
                // Start by getting a new code for this new authentication.
                OAuthClientRequest request = trakt.buildAuthorizationRequest("");

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(request.getLocationUri()));
                context.startActivity(intent);
            } else {
                // Then get a new token from the code, this will be if we're continuing the authentication.
                accessToken = new RetrieveTraktToken(code, trakt).execute().get();
                trakt.accessToken(accessToken.body().access_token);
                trakt.refreshToken(accessToken.body().refresh_token);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    /**
     * Get all the watched shows for the authorised user.
     */
    public List<BaseShow> getWatchedShows() {
        try {
            return new RetrieveShows(trakt).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get the watched progress for the input show(s).
     */
    public List<BaseShow> getShowWatchedProgress(List<BaseShow> shows) {

        List<String> showIds = new ArrayList<>();
        for (BaseShow show : shows) {
            showIds.add(show.show.ids.trakt.toString());
        }
        try {
            return new RetrieveWatchedProgress(trakt).execute(showIds.toArray(new String[shows.size()])).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private class RetrieveShows extends AsyncTask<Void, Void, List<BaseShow>> {

        TraktV2 trakt;

        RetrieveShows(TraktV2 trakt) {
            this.trakt = trakt;
        }

        @Override
        protected List<BaseShow> doInBackground(Void... voids) {
            try {
                Response<List<BaseShow>> response = trakt.users().watchedShows(Username.ME, Extended.FULL).execute();
                if (response.isSuccessful()) {
                    return response.body();
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private class RetrieveWatchedProgress extends AsyncTask<String, Void, List<BaseShow>> {

        TraktV2 trakt;

        RetrieveWatchedProgress(TraktV2 trakt) {
            this.trakt = trakt;
        }

        @Override
        protected List<BaseShow> doInBackground(String... showIds) {
            List<BaseShow> shows = new ArrayList<>();
            try {
                for (String showId : showIds) {
                    Response<BaseShow> response = trakt.shows().watchedProgress(showId, false, false, Extended.FULL).execute();
                    if (response.isSuccessful()) {
                        shows.add(response.body());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return shows;
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