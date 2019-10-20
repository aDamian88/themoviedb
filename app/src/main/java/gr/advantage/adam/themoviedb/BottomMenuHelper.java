package gr.advantage.adam.themoviedb;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


class BottomMenuHelper implements View.OnClickListener {

    private final Context context;
    private final PrefsHandler prefsHandler = new PrefsHandler();

    public BottomMenuHelper(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View bottomClick) {

        String currentScreen = prefsHandler.getScreenFromPrefs(context, "lastScreen");
        switch (bottomClick.getId()) {
            case R.id.cardHome:
                if (!currentScreen.equals("Search")) {
                    Intent mainIntent = new Intent(context, SearchActivity.class);
                    context.startActivity(mainIntent);
                }
                break;
            case R.id.cardWatchlist:
                if (!currentScreen.equals("Watchlist")) {
                    Intent cityIntent = new Intent(context, WatchListActivity.class);
                    context.startActivity(cityIntent);
                }
                break;
            case R.id.cardAbout:
                Toast.makeText(context, "About Dialog", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
