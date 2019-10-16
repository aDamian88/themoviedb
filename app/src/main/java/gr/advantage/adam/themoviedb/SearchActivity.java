package gr.advantage.adam.themoviedb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

public class SearchActivity extends AppCompatActivity {

    Button btSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btSearch = findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSearchCall();
            }
        });
    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void makeSearchCall(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult( Api.BASE_URL + "3/search/multi?api_key=6b2e856adafcc7be98bdf0d8b076851c&query=harry potter&page=1");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d(TAG, "onResponse: emptyResponse");
                } else {
                    Log.d(TAG, "onResponse: successful response " + String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("TAG", "CL service response onFailure: " + t.toString());
            }
        });
    }
}
