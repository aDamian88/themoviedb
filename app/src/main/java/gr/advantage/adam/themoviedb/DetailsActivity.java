package gr.advantage.adam.themoviedb;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private MyAppDatabase myAppDatabase;
    private CardView cardSave;
    private CardView cardTrailer;
    private Movie movie;
    private TvShow tvShow;
    private boolean saveInstance;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final TrailerHandler trailerHandler = new TrailerHandler(this);


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final Integer id = (Integer) intent.getSerializableExtra("id");
        final String type = (String) intent.getSerializableExtra("type");

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_movie_summary);
        tvGenre = findViewById(R.id.tv_movie_right);
        imPoster = findViewById(R.id.im_movie);

        String url;
        if(type.equals("movie")){
            url = "movie/"+id+"?api_key="+Api.AUTH_KEY;
            callToApi(url,type);
        }else{
            url = "tv/"+id+"?api_key="+Api.AUTH_KEY;
            callToApi(url,type);
        }

        final MyAppDatabase myAppDatabase = MyAppDatabase.getAppDatabaseFallBack(this);

        //// Handle save ///////////
        cardSave = findViewById(R.id.cardSave);
        final ImageView imSave = findViewById(R.id.im_save);

        saveInstance = myAppDatabase.MyDao().getObjectStatus(id,type);

        if(!saveInstance){
            imSave.setBackgroundResource(R.mipmap.favorite);
        }

        cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInstance = myAppDatabase.MyDao().getObjectStatus(id,type);
                if(!saveInstance) {
                    myAppDatabase.MyDao().updateTemporaryStatus(id,type,true);
                    imSave.setBackgroundResource(R.mipmap.favoriteblank);
                }else{
                    myAppDatabase.MyDao().updateTemporaryStatus(id,type,false);
                    imSave.setBackgroundResource(R.mipmap.favorite);

                }
            }
        });
        /////////////////////////////////////

        /////////// Handle trailer /////////
        cardTrailer = findViewById(R.id.cardTrailer);
        cardTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTrailer(id,type);
            }
        });

        bottomMenu.initBottomMenu(this);
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
                        movie = new Movie();
                        displayMovieData(movie.decodingMovie(response.body().toString()));
                    }else{
                        tvShow = new TvShow();
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
        tvGenre.setText(movie.getGenre());
        Glide.with(this).load(Api.POSTER_URL+movie.getImage()).into(imPoster);
    }

    private void displayTvShowData(TvShow tvShow){
        tvTitle.setText(tvShow.getTitle());
        tvSummary.setText(tvShow.getSummary());
        Glide.with(this).load(Api.POSTER_URL+tvShow.getImage()).into(imPoster);
    }

    private void callTrailer(Integer id, String type){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).client(okClient()).addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + type+"/"+id+"/"+"videos?api_key="+Api.AUTH_KEY);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (String.valueOf(response.body()).equals("[]")) {
                    Log.d("TAG", "onResponse: emptyResponse");
                    Toast.makeText(DetailsActivity.this,"Not available tailer",Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", "onResponse: trailer " + String.valueOf(response.body()));
                    trailerHandler.decodingVideoResponse(response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("TAG", "search response onFailure: " + t.toString());
            }
        });
    }



}
