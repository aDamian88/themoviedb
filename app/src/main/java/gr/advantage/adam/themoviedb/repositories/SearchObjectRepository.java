package gr.advantage.adam.themoviedb.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.api.RetrofitService;
import gr.advantage.adam.themoviedb.models.SearchObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchObjectRepository {

    private static SearchObjectRepository instance;
    private static final String TAG = "SearchObjectRepository";
    private final MutableLiveData<List<SearchObject>> searchList;


    public static SearchObjectRepository getInstance() {
        if (instance == null) {
            instance = new SearchObjectRepository();
        }
        return instance;
    }

    private final Api api;

    private SearchObjectRepository(){
        api = RetrofitService.createService();
        searchList = new MutableLiveData<>();
    }



    public MutableLiveData<List<SearchObject>> getSearchList(String search){
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "search/movie?api_key=" + Api.AUTH_KEY + "&query=" + search + "&page=" + 1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                searchList.postValue(getSearchObjectFromResponse(Objects.requireNonNull(response.body()).toString()));
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getMessage());
            }
        });

        return searchList;
    }

    public MutableLiveData<List<SearchObject>> getPopularList(){
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "movie/popular?api_key=" + Api.AUTH_KEY);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                searchList.postValue(getSearchObjectFromResponse(Objects.requireNonNull(response.body()).toString()));
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getMessage());
            }
        });

        return searchList;
    }




    private List<SearchObject> getSearchObjectFromResponse(String response) {
        ArrayList<SearchObject> searchObjects = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray arrayResults = jsonResponse.getJSONArray("results");
            for (int i = 0; i < arrayResults.length(); i++) {
                JSONObject jsonResults = arrayResults.getJSONObject(i);
                searchObjects.add(setDataFromJson(jsonResults));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchObjects;
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
