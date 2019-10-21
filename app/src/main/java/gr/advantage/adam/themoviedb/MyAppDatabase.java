package gr.advantage.adam.themoviedb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import gr.advantage.adam.themoviedb.models.SearchObject;


@Database(entities = SearchObject.class, exportSchema = false, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    private static MyAppDatabase INSTANCE;

    public abstract MyDao MyDao();

    public static MyAppDatabase getAppDatabaseFallBack(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyAppDatabase.class, "movie_db").fallbackToDestructiveMigration().allowMainThreadQueries().setJournalMode(JournalMode.TRUNCATE).build();
        }
        return INSTANCE;
    }

    public static MyAppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyAppDatabase.class, "movie_db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }



    public static void destroyInstance() {
        INSTANCE = null;
    }

/*    public static MyAppDatabase getAssetDatabase(Context context){

        INSTANCE =   Room.databaseBuilder(context.getApplicationContext(),
                MyAppDatabase.class,
                "userdb")
                .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                .allowMainThreadQueries()
                .build();

        return INSTANCE;
    }*/
}
