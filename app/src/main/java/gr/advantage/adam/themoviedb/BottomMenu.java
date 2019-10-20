package gr.advantage.adam.themoviedb;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class BottomMenu {

    protected PrefsHandler prefsHandler = new PrefsHandler();
    protected Context context;
    protected final Activity activity;

    public BottomMenu(Activity activity) {
        this.activity = activity;
    }

    public void initBottomMenu(final Context context) {

        this.context = context;
        CardView cardHome, cardWatchlist, cardAbout;

//        ImageView imHunting = this.activity.findViewById(R.id.im_start);
        cardHome = this.activity.findViewById(R.id.cardHome);
        cardWatchlist = this.activity.findViewById(R.id.cardWatchlist);
        cardAbout = this.activity.findViewById(R.id.cardAbout);

        cardHome.setOnClickListener(new BottomMenuHelper(activity));
        cardWatchlist.setOnClickListener(new BottomMenuHelper(activity));
        cardAbout.setOnClickListener(new BottomMenuHelper(activity));


/*        cardCommunity.setOnClickListener(new HuntBottomMenu(activity));
        cardProfile.setOnClickListener(new HuntBottomMenu(activity));

        StreethuntSession testSession = (StreethuntSession) context.getApplicationContext();
        TourSession checkTourSession = null;
        try {
            checkTourSession = testSession.getTourSession();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (checkTourSession != null){
            imHunting.setImageResource(0);
            imHunting.setBackgroundResource(R.mipmap.compasswhite);
            TextView tvHunting = this.activity.findViewById(R.id.tv_start_tour);
            tvHunting.setText(this.activity.getResources().getString(R.string.hunt));
        }*/
    }
}

