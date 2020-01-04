package gr.advantage.adam.themoviedb.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.util.List;

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
        Call<List<SearchObject>> call = api.getSearchResultObject(Api.BASE_URL + "search/multi?api_key=" + Api.AUTH_KEY + "&query=" + search + "&page=" + 1);
        call.enqueue(new Callback<List<SearchObject>>() {
            @Override
            public void onResponse(Call<List<SearchObject>> call, Response<List<SearchObject>> response) {
                searchList.postValue(response.body());
                Log.d(TAG, "onResponse: "+String.valueOf(response.body().get(0).getTitle()));
            }

            @Override
            public void onFailure(Call<List<SearchObject>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getMessage());
            }
        });

        return searchList;
    }

}
