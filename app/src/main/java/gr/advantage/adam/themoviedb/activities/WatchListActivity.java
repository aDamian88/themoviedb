package gr.advantage.adam.themoviedb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.jakewharton.rxbinding3.widget.RxSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import gr.advantage.adam.themoviedb.helpers.BottomMenu;
import gr.advantage.adam.themoviedb.adapters.MovieListAdapter;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.helpers.PrefsHandler;
import gr.advantage.adam.themoviedb.models.Movie;
import gr.advantage.adam.themoviedb.models.SearchObject;
import gr.advantage.adam.themoviedb.viewmodels.SearchObjectViewModel;
import gr.advantage.adam.themoviedb.viewmodels.WatchListViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WatchListActivity extends AppCompatActivity {

    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final PrefsHandler prefsHandler = new PrefsHandler();
    private static final String TAG = "WatchListActivity";
    RecyclerView.Adapter adapter;
    private ImageView imTheMovieDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);

        ImageView imTheMovieDB = findViewById(R.id.iv_the_movie_db);

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(false);

        SearchObject searchObject = new SearchObject();
        searchObjects.addAll(searchObject.getSearchObjectFromDatabase(this));
        if(searchObjects.size()>0) imTheMovieDB.setVisibility(View.GONE);
        initMovieList();

        prefsHandler.putScreenToPrefs(this, "Watchlist","lastScreen");
        bottomMenu.initBottomMenu();

        WatchListViewModel watchListViewModel= ViewModelProviders.of(Objects.requireNonNull(this)).get(WatchListViewModel.class);
        SearchView searchView = findViewById(R.id.search_view_movie);
        RxSearchView.queryTextChanges(searchView)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->
                {
                    String queryValue = String.valueOf(charSequence);
//                    searchObserve(searchObjectViewModel, queryValue);
                    watchListObserve(watchListViewModel,queryValue);

                });
    }

    private void initMovieList() {
        RecyclerView.Adapter adapter = new MovieListAdapter(searchObjects, this);
        recyclerView.setAdapter(adapter);
    }


    private void watchListObserve(WatchListViewModel watchListViewModel, String queryValue){

        watchListViewModel.init(queryValue);
        watchListViewModel.getLocalMovies().observe(this, objectResponse -> {

            if (objectResponse != null && !objectResponse.isEmpty()) {
                imTheMovieDb.setVisibility(View.GONE);
                if (searchObjects.size() > 0) clear();
                Log.d(TAG, "onCreate: " + String.valueOf(objectResponse));
                searchObjects.addAll(searchObjectConverter(objectResponse));
                adapter = new MovieListAdapter(searchObjects, this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private List<SearchObject> searchObjectConverter(List<Movie> movies){
        ArrayList<SearchObject> searchObjects = new ArrayList();

        for(Movie movie: movies){
            SearchObject searchObject = new SearchObject();
            searchObject.setId(movie.getId());
            searchObject.setImage(movie.getImage());
            searchObject.setTitle(movie.getTitle());
            searchObject.setRelease(movie.getReleaseDate());
            searchObject.setRating(movie.getVote());

            searchObjects.add(searchObject);
        }
        return searchObjects;
    }

    private void clear() {
        int size = searchObjects.size();
        searchObjects.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

}
