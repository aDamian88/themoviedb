package gr.advantage.adam.themoviedb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.helpers.BottomMenu;
import gr.advantage.adam.themoviedb.helpers.GeneralHelper;
import gr.advantage.adam.themoviedb.adapters.MovieListAdapter;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.helpers.PrefsHandler;
import gr.advantage.adam.themoviedb.models.SearchObject;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ImageView imTheMovieDb;
    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private String search;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private int page = 1;
    private LinearLayoutManager layoutManager;
    private final GeneralHelper generalHelper = new GeneralHelper();
    private final PrefsHandler prefsHandler = new PrefsHandler();
    RecyclerView.Adapter adapter;

    /// Variables for pagination

    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private final int viewThreshold = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);
        TextView watchlistTitle = findViewById(R.id.tv_title);
        watchlistTitle.setVisibility(View.GONE);

        edtSearch = findViewById(R.id.search);
        CardView cardSearch = findViewById(R.id.cardSearch);

        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: clickSearch");
                performSearch();
            }
        });
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        performSearch();
                        return true;
                    }
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                }

                if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + viewThreshold)) {
                    isLoading = true;
                    page++;
                    makeSearchCall(search);
                }
            }
        });

        prefsHandler.putScreenToPrefs(this, "Search","lastScreen");
        bottomMenu.initBottomMenu();

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void makeSearchCall(final String search) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "search/multi?api_key=" + Api.AUTH_KEY + "&query=" + search + "&page=" + page);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                String results = "";
                try {
                    if (response.body() != null) {
                        JSONObject checkResponse = new JSONObject(response.body().toString());
                        results = checkResponse.getString("results");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (results.equals("[]") || results.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Empty response from API", Toast.LENGTH_LONG).show();
                } else {
                    SearchObject searchObject = new SearchObject();
                    searchObjects.addAll(searchObject.getSearchObjectFromResponse(String.valueOf(response.body())));
                    initMovieList();
                    Toast.makeText(SearchActivity.this, "Page: " + page, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("error", "CL service response onFailure: " + t.toString());
            }
        });
    }

    private void initMovieList() {
        adapter = new MovieListAdapter(searchObjects, this);
        recyclerView.setAdapter(adapter);
    }

    private void performSearch() {
        if (!generalHelper.isOnline(SearchActivity.this)) {
            Toast.makeText(SearchActivity.this, "Needs internet connection", Toast.LENGTH_LONG).show();
        } else {
            page = 1;
            imTheMovieDb.setVisibility(View.GONE);
            if (searchObjects.size() > 0) searchObjects.clear();
            search = edtSearch.getText().toString();
            makeSearchCall(search);
            GeneralHelper.hideKeyboard(this);
        }
    }
}
