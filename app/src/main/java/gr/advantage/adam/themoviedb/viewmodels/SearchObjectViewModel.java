package gr.advantage.adam.themoviedb.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import gr.advantage.adam.themoviedb.models.SearchObject;
import gr.advantage.adam.themoviedb.repositories.SearchObjectRepository;

public class SearchObjectViewModel extends ViewModel {

    private MutableLiveData<List<SearchObject>> searchObjectList;
    private SearchObjectRepository searchObjectRepository;


    public LiveData<List<SearchObject>> getSearchObjectList(){
        return searchObjectList;
    }

    public void init(String search){
        searchObjectRepository = SearchObjectRepository.getInstance();
        if(!search.isEmpty()) {
            searchObjectList = searchObjectRepository.getSearchList(search);
        }else{
            searchObjectList = searchObjectRepository.getPopularList();
        }
    }


}
