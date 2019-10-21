package gr.advantage.adam.themoviedb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;
import gr.advantage.adam.themoviedb.helpers.BottomMenu;
import gr.advantage.adam.themoviedb.adapters.MovieListAdapter;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.helpers.PrefsHandler;
import gr.advantage.adam.themoviedb.models.SearchObject;

public class WatchListActivity extends AppCompatActivity {

    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final PrefsHandler prefsHandler = new PrefsHandler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView imTheMovieDB = findViewById(R.id.iv_the_movie_db);
        fitToolbarComponents();

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
    }

    private void initMovieList() {
        RecyclerView.Adapter adapter = new MovieListAdapter(searchObjects, this);
        recyclerView.setAdapter(adapter);
    }

    private void fitToolbarComponents(){
        EditText edtSearch = findViewById(R.id.search);
        edtSearch.setVisibility(View.GONE);
        CardView cardSearch = findViewById(R.id.cardSearch);
        cardSearch.setVisibility(View.GONE);
    }
}
