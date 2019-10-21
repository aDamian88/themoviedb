package gr.advantage.adam.themoviedb.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import gr.advantage.adam.themoviedb.database.MyAppDatabase;

public class GeneralHelper {

    public void deleteTemporaryObjects(Context context) {
        MyAppDatabase myAppDatabase = MyAppDatabase.getAppDatabaseFallBack(context);
        myAppDatabase.MyDao().deleteTemporarySavedObjects(true);
    }

    public boolean isOnline(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (Network net : connectivityManager.getAllNetworks()) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(net);
                if (networkInfo.isConnected()) return true;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (Network net : connectivityManager.getAllNetworks()) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(net);
                if (networkInfo.isConnected()) return true;
            }
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
