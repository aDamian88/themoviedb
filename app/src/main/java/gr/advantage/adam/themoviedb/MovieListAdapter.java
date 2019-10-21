package gr.advantage.adam.themoviedb;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import gr.advantage.adam.themoviedb.models.SearchObject;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<SearchObject> cardList;
    private Context context;
    private GeneralHelper generalHelper = new GeneralHelper();

    public MovieListAdapter(List<SearchObject> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new gr.advantage.adam.themoviedb.MovieListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull gr.advantage.adam.themoviedb.MovieListAdapter.ViewHolder holder, final int position) {
        final SearchObject cardListItem = cardList.get(position);

        holder.objectName.setText(cardListItem.getTitle());
        holder.releaseDate.setText(cardListItem.getRelease());
        holder.ratings.setText(cardListItem.getRating() + "% ");
        Glide.with(context).load(Api.POSTER_URL+cardListItem.getImage()).into(holder.poster);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!generalHelper.isOnline(context)) {
                    Toast.makeText(context,"Needs internet connection",Toast.LENGTH_LONG).show();
                }else {
                    saveItemTemporary(cardListItem);
                    Integer id = cardListItem.getId();
                    Intent myIntent = new Intent(context, DetailsActivity.class);
                    myIntent.putExtra("id", id);
                    String type = cardListItem.getType();
                    myIntent.putExtra("type", type);
                    context.startActivity(myIntent);
                }
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
        public ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.im_movie);
            objectName = itemView.findViewById(R.id.tv_title);
            releaseDate = itemView.findViewById(R.id.tv_movie1);
            ratings = itemView.findViewById(R.id.tv_movie_right);
            linearLayout = itemView.findViewById(R.id.object_linear_layout);

        }
    }

    private void saveItemTemporary(SearchObject searchObject){
        MyAppDatabase myAppDatabase = MyAppDatabase.getAppDatabase(context);
        Log.d(TAG, "saveItemTemporary: "+ searchObject.getTitle()+" " + String.valueOf(myAppDatabase.MyDao().checkIfObjectIsStored(searchObject.getId(),searchObject.getType(),false)));
        if(myAppDatabase.MyDao().checkIfObjectIsStored(searchObject.getId(),searchObject.getType(),false)==0) {
            searchObject.setTemporary(true);
            searchObject.saveSearchObject(context);
        }
    }


}
