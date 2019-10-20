package gr.advantage.adam.themoviedb;

import org.json.JSONException;
import org.json.JSONObject;

public class TvShow {

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

    public TvShow decodingTvShow(String response){
        TvShow tvShow = new TvShow();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            tvShow.setId(jsonResponse.getInt("id"));
            tvShow.setImage(jsonResponse.getString("poster_path"));
            tvShow.setTitle(jsonResponse.getString("original_name"));
            tvShow.setSummary(jsonResponse.getString("overview"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tvShow;
    }

}
