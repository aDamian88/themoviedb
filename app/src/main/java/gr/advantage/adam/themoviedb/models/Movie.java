package gr.advantage.adam.themoviedb.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Movie {

    private Integer id;
    private String image;
    private String title;
    private String summary;
    private String genre;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    private void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    private void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenre() {
        return genre;
    }

    private void setGenre(String genre) {
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
