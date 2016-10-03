package uk.org.willmott.mediasyncer.imdb.service;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import uk.org.willmott.mediasyncer.imdb.model.photos.Data;

/**
 * Created by tomwi on 02/10/2016.
 */

public class ImdbService {

    private static String API_URL = "http://imdb.wemakesites.net/api/";

    ImdbApiEndpoint imdbApiEndpoint;
    Retrofit retrofit;

    public ImdbService() {
        // Create the instance of retrofit using our http client.
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        imdbApiEndpoint = retrofit.create(ImdbApiEndpoint.class);
    }

    /**
     * This is way too slow. Don't use it.
     */
    public String getActorImage(String name) {

        String actorImdbId = null;

        try {
            String sUrl = API_URL + "search?q=" + name.replace(" ", "_");

            URL url = new URL(sUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert the response
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject();

            // Get all of the name results
            JsonArray array = rootobj.get("data").getAsJsonObject()
                    .get("results").getAsJsonObject()
                    .get("names").getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                JsonObject nameObject = array.get(i).getAsJsonObject();

                if (nameObject.get("title").getAsString().equals(name)) {
                    actorImdbId = nameObject.get("id").getAsString();
                    break;
                }
            }

            return imdbApiEndpoint.person(actorImdbId).execute().body().getData().getImage();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
