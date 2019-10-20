package gr.advantage.adam.themoviedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchActivity extends AppCompatActivity {

    private CardView cardSearch;
    private EditText edtSearch;
    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private String search;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final GeneralHelper generalHelper = new GeneralHelper();

    @Override
    protected void onResume() {
        super.onResume();
        generalHelper.deleteTemporaryObjects(this);
    }

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
                Log.d("TAG", "onClick: search " + String.valueOf(search));
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

        generalHelper.deleteTemporaryObjects(this);
        bottomMenu.initBottomMenu(this);

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void makeSearchCall(final String search) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "search/multi?api_key="+Api.AUTH_KEY+"&query="+search+"&page=1");
        Log.d("TAG", "makeSearchCall: " + String.valueOf(call.request()));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d("TAG", "onResponse: emptyResponse");
                    Toast.makeText(SearchActivity.this,"No results",Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", "onResponse: successful response " + String.valueOf(response.body()));
                    SearchObject searchObject = new SearchObject();
                    searchObjects.addAll(searchObject.getSearchObjectFromResponse(String.valueOf(response.body())));
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
        RecyclerView.Adapter adapter = new MovieListAdapter(searchObjects, this);
        recyclerView.setAdapter(adapter);
    }

}
