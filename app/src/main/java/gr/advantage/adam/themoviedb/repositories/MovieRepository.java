package gr.advantage.adam.themoviedb.repositories;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.api.RetrofitService;
import gr.advantage.adam.themoviedb.database.MyAppDatabase;
import gr.advantage.adam.themoviedb.helpers.MovieDBSession;
import gr.advantage.adam.themoviedb.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static MovieRepository instance;
    private static final String TAG = "SearchObjectRepository";
    private final MutableLiveData<Movie> movieData;
    private final MutableLiveData<List<Movie>> localMovies;
    MyAppDatabase myAppDatabase;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private final Api api;

    private MovieRepository() {
        api = RetrofitService.createService();
        movieData = new MutableLiveData<>();
        localMovies = new MutableLiveData<>();
        myAppDatabase = MyAppDatabase.getAppDatabaseFallBack(MovieDBSession.getAppContext());
    }


    public MutableLiveData<Movie> getMovieData(String url) {
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + url);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Movie movie = decodingMovie(response.body().toString());
                if (movieExists(movie) > 0) {
                    updateMovie(movie);
                    movieData.postValue(myAppDatabase.MyDao().getMovieFromId(movie.getId()));
                } else {
                    movieData.postValue(decodingMovie(response.body().toString()));
                }
                Log.d(TAG, "onResponse: " + String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return movieData;
    }

    public MutableLiveData<List<Movie>> getMoviesFromDB(String searchQuery) {
        String helpQuery = "%"+searchQuery+"%";
        List<Movie> movieList = myAppDatabase.MyDao().getMoviesFromSearch(helpQuery);
        localMovies.postValue(movieList);
        return localMovies;
    }

    public Movie decodingMovie(String response) {
        Movie movie = new Movie();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            movie.setId(jsonResponse.getInt("id"));
            movie.setImage(jsonResponse.getString("poster_path"));
            movie.setTitle(jsonResponse.getString("title"));
            movie.setSummary(jsonResponse.getString("overview"));
            String genre = getFirstGenre(jsonResponse.getString("genres"));
            movie.setGenre(genre);
            movie.setBackground(jsonResponse.getString("backdrop_path"));
            movie.setReleaseDate(jsonResponse.getString("release_date"));
            movie.setPopularity(jsonResponse.getString("popularity"));
            movie.setVote(jsonResponse.getString("vote_average"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private String getFirstGenre(String genreList) {
        String genre = "";
        try {
            JSONArray genreArray = new JSONArray(genreList);
            JSONObject genreObject = genreArray.getJSONObject(0);
            genre = genreObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genre;
    }


    public void saveMovie(Movie movie) {
        myAppDatabase.MyDao().addMovie(movie);
    }

    public void deleteMovie(Movie movie) {
        myAppDatabase.MyDao().deleteMovie(movie);
    }

    public void updateMovie(Movie movie) {
        myAppDatabase.MyDao().updateMovie(movie);
    }

    public Integer movieExists(Movie movie) {
        return myAppDatabase.MyDao().movieExists(movie.getId());
    }


}
