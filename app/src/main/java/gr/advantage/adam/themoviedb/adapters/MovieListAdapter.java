package gr.advantage.adam.themoviedb.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.List;
import gr.advantage.adam.themoviedb.helpers.GeneralHelper;
import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.activities.DetailsActivity;
import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.models.SearchObject;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final List<SearchObject> cardList;
    private final Context context;
    private final GeneralHelper generalHelper = new GeneralHelper();

    public MovieListAdapter(List<SearchObject> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, final int position) {
        final SearchObject cardListItem = cardList.get(position);

        holder.objectName.setText(cardListItem.getTitle());
        holder.releaseDate.setText(cardListItem.getRelease());
        holder.ratings.setText(cardListItem.getRating());
        Glide.with(context).load(Api.POSTER_URL+cardListItem.getImage()).into(holder.poster);

        holder.linearLayout.setOnClickListener(view -> {
            if (!generalHelper.isOnline(context)) {
                Toast.makeText(context,"Needs internet connection",Toast.LENGTH_LONG).show();
            }else {
                Integer id = cardListItem.getId();
                Intent myIntent = new Intent(context, DetailsActivity.class);
                myIntent.putExtra("id", id);
                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView objectName;
        final TextView releaseDate;
        final TextView ratings;
        final LinearLayout linearLayout;
        final ImageView poster;

        ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.im_movie);
            objectName = itemView.findViewById(R.id.tv_title);
            releaseDate = itemView.findViewById(R.id.tv_movie1);
            ratings = itemView.findViewById(R.id.tv_movie_right);
            linearLayout = itemView.findViewById(R.id.object_linear_layout);

        }
    }

}
