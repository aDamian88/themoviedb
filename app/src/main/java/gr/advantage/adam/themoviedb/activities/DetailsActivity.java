package gr.advantage.adam.themoviedb.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.Objects;
import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.helpers.BottomMenu;
import gr.advantage.adam.themoviedb.helpers.DateFormatter;
import gr.advantage.adam.themoviedb.helpers.GeneralHelper;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.helpers.PrefsHandler;
import gr.advantage.adam.themoviedb.helpers.TrailerHandler;
import gr.advantage.adam.themoviedb.models.Movie;
import gr.advantage.adam.themoviedb.repositories.MovieRepository;
import gr.advantage.adam.themoviedb.viewmodels.MovieViewModel;


public class DetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvSummary;
    private TextView tvGenre;
    private TextView tvSave;
    private TextView tvVoting;
    private TextView tvRelease;
    private ImageView imPoster;
    private ImageView imBackground;
    private Movie movie;
    private Integer saveInstance;
    private final BottomMenu bottomMenu = new BottomMenu(this);
    private final GeneralHelper generalHelper = new GeneralHelper();
    private final PrefsHandler prefsHandler = new PrefsHandler();
    ImageView imSave;
    CardView cardSave;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final Integer id = (Integer) intent.getSerializableExtra("id");

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_movie_summary);
        tvSave = findViewById(R.id.tv_save);
        imPoster = findViewById(R.id.movie_layout).findViewById(R.id.im_movie);
        imBackground = findViewById(R.id.movie_layout).findViewById(R.id.im_background);
        tvVoting = findViewById(R.id.movie_layout).findViewById(R.id.tv_voting_data);
        tvRelease = findViewById(R.id.movie_layout).findViewById(R.id.tv_release_data);
        tvGenre = findViewById(R.id.movie_layout).findViewById(R.id.tv_genre);

        String url = "movie/" + id + "?api_key=" + Api.AUTH_KEY;

        //// Handle save ///////////
        cardSave = findViewById(R.id.cardSave);
        imSave = findViewById(R.id.im_save);

        MovieViewModel movieViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(MovieViewModel.class);

        movieViewModel.init(url);
        movieViewModel.getMovieData().observe(this, movieResponse -> {
            if (movieResponse != null) {
                movie = movieResponse;
                getMovieInstance();
                displayMovieData(movieResponse);
            }
        });

        cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movie!=null && saveInstance!=null) {
                    MovieRepository movieRepository = MovieRepository.getInstance();
                    if (saveInstance > 0) {
                        movieRepository.deleteMovie(movie);
                        getMovieInstance();
                    } else {
                        movieRepository.saveMovie(movie);
                        getMovieInstance();
                    }
                }
            }
        });

        /////////// Handle trailer /////////
        CardView cardTrailer = findViewById(R.id.cardTrailer);
        cardTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!generalHelper.isOnline(DetailsActivity.this)) {
                    Toast.makeText(DetailsActivity.this, "Needs internet connection", Toast.LENGTH_LONG).show();
                } else {
                    TrailerHandler.showTrailer(id);
                }
            }
        });


        prefsHandler.putScreenToPrefs(this, "Details","lastScreen");
        bottomMenu.initBottomMenu();
    }

    private void displayMovieData(Movie movie) {
        tvTitle.setText(movie.getTitle());
        tvSummary.setText(movie.getSummary());
        tvGenre.setText(movie.getGenre());
        tvVoting.setText(movie.getVote());
        tvRelease.setText(DateFormatter.getYear(movie.getReleaseDate()));
        Glide.with(this).load(Api.POSTER_URL + movie.getImage()).into(imPoster);
        Glide.with(this).load(Api.BACKGROUND_URL + movie.getBackground()).into(imBackground);

    }

    private void getMovieInstance(){

        if(movie!=null) {
            MovieRepository movieRepository = MovieRepository.getInstance();
            saveInstance = movieRepository.movieExists(movie);
            if (saveInstance > 0) {
                imSave.setBackgroundResource(R.mipmap.favorite);
                tvSave.setText(getResources().getString(R.string.remove));
            } else {
                imSave.setBackgroundResource(R.mipmap.favoriteblank);
                tvSave.setText(getResources().getString(R.string.save));
            }
        }
    }
}
