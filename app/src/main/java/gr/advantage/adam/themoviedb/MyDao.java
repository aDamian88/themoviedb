package gr.advantage.adam.themoviedb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MyDao {

    // search Model
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSearchObject(SearchObject searchObject);

    @Query("select count() from Favorites where id=:objectId and type=:objectType and is_temporary=:temporary")
    Integer checkIfObjectIsStored(Integer objectId,String objectType,boolean temporary);

    @Query("update Favorites set is_temporary=:temporary where id=:objectId and type=:objectType")
    void updateTemporaryStatus(Integer objectId,String objectType,boolean temporary);

}
