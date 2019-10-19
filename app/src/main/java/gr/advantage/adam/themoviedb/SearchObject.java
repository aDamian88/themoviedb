package gr.advantage.adam.themoviedb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class SearchObject {

    Integer id;
    String image;
    String title;
    String release;
    String rating;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public ArrayList<SearchObject> getSearchObjectFromResponse(String response) {
        ArrayList<SearchObject> SearchObjects = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray arrayResults = jsonResponse.getJSONArray("results");
            for (int i = 0; i < arrayResults.length(); i++) {
                JSONObject jsonResults = arrayResults.getJSONObject(i);

                // Checking if media_type is person to not decode it, we need only Movies and Tv Shows.
                if (!jsonResults.getString("media_type").equals("person")) {
                    SearchObjects.add(setDataFromJson(jsonResults));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return SearchObjects;
    }

    private SearchObject setDataFromJson(JSONObject jsonResults) {
        SearchObject SearchObject = new SearchObject();
        try {
            SearchObject.setId(jsonResults.getInt("id"));
            SearchObject.setImage(jsonResults.getString("poster_path"));
            if (jsonResults.has("title"))
                SearchObject.setTitle(jsonResults.getString("title"));// Different decoding
            if (jsonResults.has("name"))
                SearchObject.setTitle(jsonResults.getString("name")); // between Movie and TV Show.
            if (jsonResults.has("release_date"))
                SearchObject.setRelease(jsonResults.getString("release_date"));
            if (jsonResults.has("first_air_date"))
                SearchObject.setRelease(jsonResults.getString("first_air_date"));
            SearchObject.setRating(jsonResults.getString("vote_average"));
            SearchObject.setType(jsonResults.getString("media_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return SearchObject;
    }
}
