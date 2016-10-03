package uk.org.willmott.mediasyncer.tvdb.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.org.willmott.mediasyncer.tvdb.model.AuthRequest;
import uk.org.willmott.mediasyncer.tvdb.model.Episode;
import uk.org.willmott.mediasyncer.tvdb.model.ShowActors;
import uk.org.willmott.mediasyncer.tvdb.model.Token;

/**
 * Created by tomwi on 01/10/2016.
 */

public interface TheTvdbApiEndpoints {

    @GET("episodes/{id}")
    Call<Episode> getEpisode(@Path("id") String id);

    @POST("login")
    Call<Token> getToken(@Body AuthRequest authRequest);

    @GET("series/{id}/actors")
    Call<ShowActors> getShowActors(@Path("id") String id);
}
