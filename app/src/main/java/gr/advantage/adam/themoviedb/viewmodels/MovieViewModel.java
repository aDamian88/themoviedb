package gr.advantage.adam.themoviedb.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import gr.advantage.adam.themoviedb.models.Movie;
import gr.advantage.adam.themoviedb.repositories.MovieRepository;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<Movie> movieData;
    private MovieRepository movieRepository;

    public LiveData<Movie> getMovieData(){
        return movieData;
    }

    public void init(String url){
        movieRepository = movieRepository.getInstance();
        movieData = movieRepository.getMovieData(url);
    }

}
