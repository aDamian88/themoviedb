package gr.advantage.adam.themoviedb.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
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
    private final PrefsHandler prefsHandler = new PrefsHandler();
    private RecyclerView.Adapter adapter;
    private static final String TAG = "SearchActivity";
    private String queryValue="";
    private SearchObjectViewModel searchObjectViewModel;

    @Override
    protected void onResume() {
        super.onResume();
        searchObserve(searchObjectViewModel, queryValue);
        prefsHandler.putScreenToPrefs(this, "Search","lastScreen");
        bottomMenu.initBottomMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.themoviedb_search));

        SearchView searchView = findViewById(R.id.search_view_movie);
        searchView.setQueryHint(getResources().getString(R.string.web_search));

        searchObjectViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(SearchObjectViewModel.class);

        RxSearchView.queryTextChanges(searchView)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->
                {
                    queryValue = String.valueOf(charSequence);
                    searchObserve(searchObjectViewModel, queryValue);

                });

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
