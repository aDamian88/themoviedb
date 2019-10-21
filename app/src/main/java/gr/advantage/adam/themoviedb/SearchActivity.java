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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ImageView imTheMovieDb;
    private final ArrayList<SearchObject> searchObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private String search;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final GeneralHelper generalHelper = new GeneralHelper();
    private int page =1;
    private LinearLayoutManager layoutManager;

    /// Variables for pagination
    private boolean responseIsEmpty =false;

    private boolean isLoading = true;
    private int pastVisibleItems,visibleItemCount,totalItemCount,previousTotal=0;
    private int viewThreshold = 10;

    @Override
    protected void onResume() {
        super.onResume();
        generalHelper.deleteTemporaryObjects(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imTheMovieDb = findViewById(R.id.iv_the_movie_db);

        edtSearch = findViewById(R.id.search);

        cardSearch = findViewById(R.id.cardSearch);
        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imTheMovieDb.setVisibility(View.GONE);
                searchObjects.clear();
                responseIsEmpty =false;
                pastVisibleItems=0;
                visibleItemCount=0;
                totalItemCount=0;
                previousTotal=0;
                search = edtSearch.getText().toString();
                Log.d("TAG", "onClick: search " + String.valueOf(search));
                makeSearchCall(search);

            }
        });

        recyclerView = findViewById(R.id.recycler_movie_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.isClickable();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.stopScroll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setFocusable(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCount>previousTotal){
                            isLoading =false;
                            previousTotal = totalItemCount;
                        }
                    }
                }

                if(!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+viewThreshold) ){
                    isLoading = true;
                    page++;
                    makeSearchCall(search);
                }else {
                    if (!responseIsEmpty && (totalItemCount - visibleItemCount) == (pastVisibleItems)) {
                        if (!responseIsEmpty) {
                            isLoading = true;
                            page++;
                            makeSearchCall(search);
                        }
                    }
                }
            }
        });

        generalHelper.deleteTemporaryObjects(this);
        bottomMenu.initBottomMenu(this);

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void makeSearchCall(final String search) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "search/multi?api_key="+Api.AUTH_KEY+"&query="+search+"&page="+page);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                String results="";
                try {
                    JSONObject checkResponse = new JSONObject(response.body().toString());
                    results = checkResponse.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (results.equals("[]") || results.isEmpty()) {
                    Log.d("TAG", "onResponse: emptyResponse");
                    Toast.makeText(SearchActivity.this,"Empty response from API",Toast.LENGTH_LONG).show();
                    responseIsEmpty=true;
                    page = 1;
                } else {
                    Log.d("TAG", "onResponse: successful response " + String.valueOf(response.body()));
                    SearchObject searchObject = new SearchObject();
                    searchObjects.addAll(searchObject.getSearchObjectFromResponse(String.valueOf(response.body())));
                    initMovieList();
                    Toast.makeText(SearchActivity.this,"Page: "+page,Toast.LENGTH_LONG).show();
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
