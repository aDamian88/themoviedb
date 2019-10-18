package gr.advantage.adam.themoviedb;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String AUTH_KEY = "6b2e856adafcc7be98bdf0d8b076851c";

    @GET
    Call<JsonObject> getSearchResult(@Url String url);
}
