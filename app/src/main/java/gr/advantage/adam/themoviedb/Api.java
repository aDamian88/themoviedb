package gr.advantage.adam.themoviedb;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    String BASE_URL = "https://api.themoviedb.org/";

    @GET
    Call<JsonObject> getSearchResult(@Url String url);
}
