package gr.advantage.adam.themoviedb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

public class GeneralHelper {

    public void deleteTemporaryObjects(Context context){
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
}
