package gr.advantage.adam.themoviedb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Integer id = (Integer) intent.getSerializableExtra("id");
        String type = (String) intent.getSerializableExtra("type");

        Log.d(TAG, "onCreate: id " + id + " type " + type);

        if(type.equals("movie")){
            //call movies
            callMovie(id,type);
        }else{
            //call tv shows
        }

    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    private void callMovie(Integer id,String type){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + type+"/"+id+"?api_key="+Api.AUTH_KEY);
        Log.d(TAG, "callMovie: " + String.valueOf(call.request()));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                // show image, title, summary, genre (only first), trailer if applicable.

                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d(TAG, "onResponse: emptyResponse");
                } else {
                    Log.d(TAG, "onResponse: successful response " + String.valueOf(response.body()));
                    decodingMovie(response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("TAG", "CL service response onFailure: " + t.toString());
            }
        });

    }

    private void decodingMovie(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            Log.d(TAG, "decodingMovie: poster " + jsonResponse.getString("poster_path"));
            Log.d(TAG, "decodingMovie: title " + jsonResponse.getString("original_title"));
            Log.d(TAG, "decodingMovie: summary " + jsonResponse.getString("overview"));
            Log.d(TAG, "decodingMovie: genres " + jsonResponse.getString("genres"));
            //decode genre only first
            Log.d(TAG, "decodingMovie: video " + jsonResponse.getString("video"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callTvShow(Integer id){

    }

}
