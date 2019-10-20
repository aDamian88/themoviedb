package gr.advantage.adam.themoviedb;

import android.content.Context;

public class GeneralHelper {

    public void deleteTemporaryObjects(Context context){
        MyAppDatabase myAppDatabase = MyAppDatabase.getAppDatabaseFallBack(context);
        myAppDatabase.MyDao().deleteTemporarySavedObjects(true);
    }

}
