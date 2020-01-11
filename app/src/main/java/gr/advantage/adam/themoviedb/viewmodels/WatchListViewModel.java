package gr.advantage.adam.themoviedb.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import gr.advantage.adam.themoviedb.models.Movie;
import gr.advantage.adam.themoviedb.repositories.MovieRepository;

public class WatchListViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> localMovies;
    private MovieRepository movieRepository;

    public LiveData<List<Movie>> getLocalMovies(){
        return localMovies;
    }

    public void init(String searchQuery){
        movieRepository = MovieRepository.getInstance();
        localMovies = movieRepository.getMoviesFromDB(searchQuery);
    }


}
