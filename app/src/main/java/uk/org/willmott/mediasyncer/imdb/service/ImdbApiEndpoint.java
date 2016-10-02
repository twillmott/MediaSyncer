package uk.org.willmott.mediasyncer.imdb.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uk.org.willmott.mediasyncer.imdb.model.photos.PersonData;

/**
 * Created by tomwi on 01/10/2016.
 */

public interface ImdbApiEndpoint {

    @GET("{id}")
    Call<PersonData> person(@Path("id") String id);
}
