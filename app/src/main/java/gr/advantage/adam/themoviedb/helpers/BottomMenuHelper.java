package gr.advantage.adam.themoviedb.helpers;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import gr.advantage.adam.themoviedb.R;
import gr.advantage.adam.themoviedb.activities.SearchActivity;
import gr.advantage.adam.themoviedb.activities.WatchListActivity;


class BottomMenuHelper implements View.OnClickListener {

    private final Activity activity;
    private final PrefsHandler prefsHandler = new PrefsHandler();
    private final GeneralHelper generalHelper = new GeneralHelper();

    public BottomMenuHelper(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View bottomClick) {


        String currentScreen = prefsHandler.getScreenFromPrefs(activity, "lastScreen");
        switch (bottomClick.getId()) {
            case R.id.cardHome:
                if (!currentScreen.equals("Search")) {
                    Intent mainIntent = new Intent(activity, SearchActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivityIfNeeded(mainIntent,0);
                }
                break;
            case R.id.cardWatchlist:
                if (!currentScreen.equals("Watchlist")) {
                    generalHelper.deleteTemporaryObjects(activity);
                    Intent cityIntent = new Intent(activity, WatchListActivity.class);
                    cityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivityIfNeeded(cityIntent,0);
                }
                break;
            case R.id.cardAbout:
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.createAlertDialog(activity,activity.getResources().getString(R.string.about),activity.getResources().getString(R.string.about_message),"OK","",null);
                break;
            default:
                break;
        }
    }
}
