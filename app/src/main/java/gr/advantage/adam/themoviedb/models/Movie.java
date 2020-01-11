package gr.advantage.adam.themoviedb.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Movies")
public class Movie {

    @PrimaryKey
    private Integer id;
    private String image;
    private String title;
    private String summary;
    private String genre;
    private String background;
    private String releaseDate;
    private String popularity;
    private String vote;

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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
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
