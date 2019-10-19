package gr.advantage.adam.themoviedb;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvSummary;
    private TextView tvGenre;
    private ImageView imPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Integer id = (Integer) intent.getSerializableExtra("id");
        String type = (String) intent.getSerializableExtra("type");

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_movie_summary);
        imPoster = findViewById(R.id.im_movie);

        String url;
        if(type.equals("movie")){
            url = "movie/"+id+"?api_key="+Api.AUTH_KEY;
            callToApi(url,type);
        }else{
            url = "tv/"+id+"?api_key="+Api.AUTH_KEY;
            callToApi(url,type);
        }
    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder().connectTimeout(60, TimeUnit.MINUTES).writeTimeout(60, TimeUnit.MINUTES).readTimeout(60, TimeUnit.MINUTES).build();
    }

    public void callToApi(final String url,final String type) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + url);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d("TAG", "onResponse: emptyResponse");
                } else {
                    Log.d("TAG", "onResponse: " + String.valueOf(response.body()));
                    if(type.equals("movie")){
                        Movie movie = new Movie();
                        displayMovieData(movie.decodingMovie(response.body().toString()));
                    }else{
                        TvShow tvShow = new TvShow();
                        displayTvShowData(tvShow.decodingTvShow(response.body().toString()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("TAG", "search response onFailure: " + t.toString());
            }
        });
    }

    private void displayMovieData(Movie movie){
        tvTitle.setText(movie.getTitle());
        tvSummary.setText(movie.getSummary());
        Glide.with(this).load(Api.POSTER_URL+movie.getImage()).into(imPoster);
    }

    private void displayTvShowData(TvShow tvShow){
        tvTitle.setText(tvShow.getTitle());
        tvSummary.setText(tvShow.getSummary());
        Glide.with(this).load(Api.POSTER_URL+tvShow.getImage()).into(imPoster);
    }


}
