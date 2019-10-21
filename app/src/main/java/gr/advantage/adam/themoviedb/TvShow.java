package gr.advantage.adam.themoviedb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TvShow {

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

    public TvShow decodingTvShow(String response){
        TvShow tvShow = new TvShow();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            tvShow.setId(jsonResponse.getInt("id"));
            tvShow.setImage(jsonResponse.getString("poster_path"));
            tvShow.setTitle(jsonResponse.getString("original_name"));
            tvShow.setSummary(jsonResponse.getString("overview"));
            String genre = getFirstGenre(jsonResponse.getString("genres"));
            tvShow.setGenre(genre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tvShow;
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
