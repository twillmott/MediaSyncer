package uk.org.willmott.mediasyncer.tvdb.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import uk.org.willmott.mediasyncer.tvdb.model.AuthRequest;
import uk.org.willmott.mediasyncer.tvdb.model.Episode;
import uk.org.willmott.mediasyncer.tvdb.model.ShowActors;
import uk.org.willmott.mediasyncer.tvdb.model.Token;

/**
 * When this service is instantiated, it creates a new authitencation.
 * <p>
 * All other methods in this service must be called in an AsyncTask.
 * <p>
 * Created by tomwi on 01/10/2016.
 */
public class TheTvdbService {

    private static String API_KEY = "95EB3E3F850B7DF4";
    public static final String API_HOST = "api.thetvdb.com";
    public static final String API_URL = "https://" + API_HOST + "/";
    public static final String API_VERSION = "2.1.0";

    private TheTvdbApiEndpoints theTvdbApi;
    private Token token;
    private Episode episode;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private
    Retrofit retrofit;

    public TheTvdbService() {

        // Set the token to nothing for now, until we actually get it.
        token = new Token();
        token.setToken("");

        // Add the token to the header.
        httpClient.addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Add the custom header that will always add in our authorisation token
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + token.getToken())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        // Create the instance of retrofit using our http client.
        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        theTvdbApi = retrofit.create(TheTvdbApiEndpoints.class);

        // Get the authorisation token.
        try {
            token = new Authorise().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the episode information from the TVDB.
     */
    public Episode getEpisode(String id) {
        try {
            Response<Episode> call = theTvdbApi.getEpisode(id).execute();
            return call.body();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    public ShowActors getShowActors(String id) {
        try {
            Response<ShowActors> response = theTvdbApi.getShowActors(id).execute();
            return response.body();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    // Async task to get the authorisation for the tvdb.
    private class Authorise extends AsyncTask<Void, Void, Token> {

        @Override
        protected Token doInBackground(Void... params) {
            try {
                return theTvdbApi.getToken(new AuthRequest(API_KEY, null, null)).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
