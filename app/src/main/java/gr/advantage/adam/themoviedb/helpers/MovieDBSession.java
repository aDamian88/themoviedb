package gr.advantage.adam.themoviedb.helpers;

import android.app.Application;
import android.content.Context;

public class MovieDBSession extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MovieDBSession.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MovieDBSession.context;
    }

}
