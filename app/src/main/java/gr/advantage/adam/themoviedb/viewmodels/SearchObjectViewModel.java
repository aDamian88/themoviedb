package gr.advantage.adam.themoviedb.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import gr.advantage.adam.themoviedb.models.SearchObject;
import gr.advantage.adam.themoviedb.repositories.SearchObjectRepository;

public class SearchObjectViewModel extends ViewModel {

    private MutableLiveData<List<SearchObject>> searchObjectList;
    private SearchObjectRepository searchObjectRepository;
    private static final String TAG = "SearchObjectViewModel";


    public LiveData<List<SearchObject>> getSearchObjectList(){
        return searchObjectList;
    }

    public void init(String search){
        Log.d(TAG, "init: search " + String.valueOf(search));
        searchObjectRepository = searchObjectRepository.getInstance();
        if(!search.isEmpty()) {
            searchObjectList = searchObjectRepository.getSearchList(search);
        }else{
            searchObjectList = searchObjectRepository.getPopularList();
        }
        Log.d(TAG, "init: search " + String.valueOf(searchObjectList));

    }


}
