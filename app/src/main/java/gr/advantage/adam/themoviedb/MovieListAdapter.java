package gr.advantage.adam.themoviedb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<Movie> cardList;
    private Context context;
    private String nextObject;

    public MovieListAdapter(List<Movie> cardList, Context context, String nextObject) {
        this.cardList = cardList;
        this.context = context;
        this.nextObject = nextObject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new gr.advantage.adam.themoviedb.MovieListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull gr.advantage.adam.themoviedb.MovieListAdapter.ViewHolder holder, final int position) {
        final Movie cardListItem = cardList.get(position);

        holder.objectName.setText(cardListItem.getTitle());
        holder.releaseDate.setText(cardListItem.getRelease());
        holder.ratings.setText(cardListItem.getRating() + "% ");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = cardListItem.getId();
                Intent myIntent = new Intent(context, DetailsActivity.class);
                myIntent.putExtra("id", id);
                String type = cardListItem.getType();
                myIntent.putExtra("type", type);
                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView objectName, releaseDate, ratings;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            objectName = itemView.findViewById(R.id.tv_title);
            releaseDate = itemView.findViewById(R.id.tv_movie1);
            ratings = itemView.findViewById(R.id.tv_movie_right);
            linearLayout = itemView.findViewById(R.id.object_linear_layout);

        }
    }
}
