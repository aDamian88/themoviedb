package gr.advantage.adam.themoviedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {

    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    GeneralHelper generalHelper = new GeneralHelper();
    ImageView imTheMovieDB;

    @Override
    protected void onResume() {
        super.onResume();
        generalHelper.deleteTemporaryObjects(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDB = findViewById(R.id.iv_the_movie_db);

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(false);

        SearchObject searchObject = new SearchObject();
        searchObjects.addAll(searchObject.getSearchObjectFromDatabase(this));
        Log.d("tag", "onCreate: watchlist " + String.valueOf(searchObjects));
        if(searchObjects.size()>0)imTheMovieDB.setVisibility(View.GONE);
        initMovieList();

        bottomMenu.initBottomMenu(this);
        generalHelper.deleteTemporaryObjects(this);
    }

    private void initMovieList() {
        RecyclerView.Adapter adapter = new MovieListAdapter(searchObjects, this);
        recyclerView.setAdapter(adapter);
    }
}
