package gr.advantage.adam.themoviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class PrefsHandler extends AppCompatActivity {

    public void putScreenToPrefs(Context context, String object,String objectName){
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(objectName, object);
            editor.apply();
        } catch (Exception e) {
            Log.d("String prefs error ", e.getMessage());
        }
    }

    public String getScreenFromPrefs(Context context,String objectName){
        String restoredActivity = "";
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            restoredActivity = prefs.getString(objectName,"");
        } catch (Exception e) {
            Log.d("String prefs error ", e.getMessage());
        }
        return restoredActivity;
    }

}

