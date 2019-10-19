package gr.advantage.adam.themoviedb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

public class SearchActivity extends AppCompatActivity {

    private CardView cardSearch;
    private EditText edtSearch;
    private final ArrayList<Movie> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    private String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.search);

        cardSearch = findViewById(R.id.cardSearch);
        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = edtSearch.getText().toString();
                Log.d(TAG, "onClick: search " + String.valueOf(search));
                makeSearchCall(search);
            }
        });

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(false);

        edtSearch.clearFocus();

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void makeSearchCall(String search) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "search/multi?api_key="+Api.AUTH_KEY+"&query="+search+"&page=1");
        Log.d(TAG, "makeSearchCall: " + String.valueOf(call.request()));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d(TAG, "onResponse: emptyResponse");
                } else {
                    Log.d(TAG, "onResponse: successful response " + String.valueOf(response.body()));
                    Movie movie = new Movie();
                    movies.addAll(movie.getMovieFromResponse(String.valueOf(response.body())));
                    initMovieList();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("TAG", "CL service response onFailure: " + t.toString());
            }
        });
    }

    private void initMovieList() {
        RecyclerView.Adapter adapter = new MovieListAdapter(movies, this, "Movie");
        recyclerView.setAdapter(adapter);
    }
}
