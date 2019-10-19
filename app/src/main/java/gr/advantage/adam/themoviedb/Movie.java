package gr.advantage.adam.themoviedb;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    Integer id;
    String image;
    String title;
    String summary;

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


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Movie decodingMovie(String response){
        Movie movie = new Movie();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            movie.setImage(jsonResponse.getString("poster_path"));
            movie.setTitle(jsonResponse.getString("title"));
            movie.setSummary(jsonResponse.getString("overview"));
            Log.d("TAG", "decodingMovie: genres " + jsonResponse.getString("genres"));
            //decode genre only first
            Log.d("TAG", "decodingMovie: video " + jsonResponse.getString("video"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }


}
