package gr.advantage.adam.themoviedb;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Movie {

    Integer id;
    String image;
    String title;
    String release;
    String rating;
    String votes;
    String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Movie> getMovieFromResponse(String response) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray arrayResults = jsonResponse.getJSONArray("results");
            for (int i = 0; i < arrayResults.length(); i++) {
                JSONObject jsonResults = arrayResults.getJSONObject(i);
                movies.add(setDataFromJson(jsonResults));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Movie setDataFromJson(JSONObject jsonResults){
        Movie movie = new Movie();
        try {
            movie.setId(jsonResults.getInt("id"));
            movie.setImage(jsonResults.getString("poster_path"));
            movie.setTitle(jsonResults.getString("title"));
            movie.setRelease(jsonResults.getString("release_date"));
            movie.setRating(jsonResults.getString("vote_average"));
            movie.setType(jsonResults.getString("media_type"));
            setVotes(jsonResults.getString("vote_count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }
}
