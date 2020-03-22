package gr.advantage.adam.themoviedb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
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
import gr.advantage.adam.themoviedb.viewmodels.WatchListViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WatchListActivity extends AppCompatActivity {

    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final PrefsHandler prefsHandler = new PrefsHandler();
    private static final String TAG = "WatchListActivity";
    private RecyclerView.Adapter adapter;
    private ImageView imTheMovieDb;
    private String queryValue="";
    private WatchListViewModel watchListViewModel;

    @Override
    protected void onResume() {
        super.onResume();
        watchListObserve(watchListViewModel, queryValue);
        prefsHandler.putScreenToPrefs(this, "Watchlist","lastScreen");
        bottomMenu.initBottomMenu();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(false);

        SearchObject searchObject = new SearchObject();
        searchObjects.addAll(searchObject.getSearchObjectFromDatabase(this));
        if(searchObjects.size()>0) imTheMovieDb.setVisibility(View.GONE);
        initMovieList();

        prefsHandler.putScreenToPrefs(this, "Watchlist","lastScreen");
        bottomMenu.initBottomMenu();

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.watchlist));

        SearchView searchView = findViewById(R.id.search_view_movie);
        searchView.setQueryHint(getResources().getString(R.string.watchlist_search));

        watchListViewModel= ViewModelProviders.of(Objects.requireNonNull(this)).get(WatchListViewModel.class);
        RxSearchView.queryTextChanges(searchView)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->
                {
                    queryValue = String.valueOf(charSequence);
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
