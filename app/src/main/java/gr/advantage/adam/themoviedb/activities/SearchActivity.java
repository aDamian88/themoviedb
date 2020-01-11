package gr.advantage.adam.themoviedb.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import gr.advantage.adam.themoviedb.helpers.BottomMenu;
import gr.advantage.adam.themoviedb.adapters.MovieListAdapter;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.helpers.PrefsHandler;
import gr.advantage.adam.themoviedb.models.SearchObject;
import gr.advantage.adam.themoviedb.viewmodels.SearchObjectViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;

import androidx.lifecycle.ViewModelProviders;

import com.jakewharton.rxbinding3.widget.RxSearchView;


public class SearchActivity extends AppCompatActivity {

    private ImageView imTheMovieDb;
    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private LinearLayoutManager layoutManager;
    private final PrefsHandler prefsHandler = new PrefsHandler();
    RecyclerView.Adapter adapter;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);

        SearchView searchView = findViewById(R.id.search_view_movie);

        SearchObjectViewModel searchObjectViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(SearchObjectViewModel.class);


        RxSearchView.queryTextChanges(searchView)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->
                {
                    String queryValue = String.valueOf(charSequence);
                    searchObserve(searchObjectViewModel, queryValue);

                });

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(true);


        prefsHandler.putScreenToPrefs(this, "Search","lastScreen");
        bottomMenu.initBottomMenu();

    }

    private void searchObserve(SearchObjectViewModel searchObjectViewModel,String queryValue){

        searchObjectViewModel.init(queryValue);
        searchObjectViewModel.getSearchObjectList().observe(this, objectResponse -> {

            if (objectResponse != null && !objectResponse.isEmpty()) {
                imTheMovieDb.setVisibility(View.GONE);
                if (searchObjects.size() > 0) clear();
                Log.d(TAG, "onCreate: " + String.valueOf(objectResponse));
                searchObjects.addAll(objectResponse);
                adapter = new MovieListAdapter(searchObjects, this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void clear() {
        int size = searchObjects.size();
        searchObjects.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

}
