package gr.advantage.adam.themoviedb;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Movie {

    Integer id;
    String image;
    String title;
    String summary;
    String genre;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Movie decodingMovie(String response){
        Movie movie = new Movie();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            movie.setId(jsonResponse.getInt("id"));
            movie.setImage(jsonResponse.getString("poster_path"));
            movie.setTitle(jsonResponse.getString("title"));
            movie.setSummary(jsonResponse.getString("overview"));
            String genre = getFirstGenre(jsonResponse.getString("genres"));
            movie.setGenre(genre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private String getFirstGenre(String genreList){
        String genre="";
        try {
            JSONArray genreArray = new JSONArray(genreList);
            JSONObject genreObject = genreArray.getJSONObject(0);
            genre = genreObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genre;
    }
}
