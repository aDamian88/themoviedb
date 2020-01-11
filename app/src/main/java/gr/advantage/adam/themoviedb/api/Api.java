package gr.advantage.adam.themoviedb.api;

import com.google.gson.JsonObject;

import java.util.List;

import gr.advantage.adam.themoviedb.models.SearchObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String AUTH_KEY = "6b2e856adafcc7be98bdf0d8b076851c";
    String POSTER_URL = "http://image.tmdb.org/t/p/w185/";
    String BACKGROUND_URL = "http://image.tmdb.org/t/p/w500/";


    @GET
    Call<JsonObject> getSearchResult(@Url String url);

    @GET
    Call<JsonObject> getPopularMovies(@Url String url,@Query("page") int page);


    @GET
    Call<List<SearchObject>> getSearchResultObject(@Url String url);


    @GET("search/multi"+Api.AUTH_KEY)
    Call<List<SearchObject>> getSearchResults(@Field("query") String search);
}
