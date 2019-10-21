package gr.advantage.adam.themoviedb.helpers;

import android.app.Activity;
import android.content.Context;

import androidx.cardview.widget.CardView;

import gr.advantage.adam.themoviedb.R;

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

        cardHome = this.activity.findViewById(R.id.cardHome);
        cardWatchlist = this.activity.findViewById(R.id.cardWatchlist);
        cardAbout = this.activity.findViewById(R.id.cardAbout);

        cardHome.setOnClickListener(new BottomMenuHelper(activity));
        cardWatchlist.setOnClickListener(new BottomMenuHelper(activity));
        cardAbout.setOnClickListener(new BottomMenuHelper(activity));

    }
}

