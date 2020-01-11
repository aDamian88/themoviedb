package gr.advantage.adam.themoviedb.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gr.advantage.adam.themoviedb.models.Movie;
import gr.advantage.adam.themoviedb.models.SearchObject;

@Dao
public interface MyDao {

    // Search Model
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSearchObject(SearchObject searchObject);

    @Query("select count() from Favorites where id=:objectId and type=:objectType and is_temporary=:temporary")
    Integer checkIfObjectIsStored(Integer objectId,String objectType,boolean temporary);

    @Query("update Favorites set is_temporary=:temporary where id=:objectId and type=:objectType")
    void updateTemporaryStatus(Integer objectId,String objectType,boolean temporary);

    @Query("delete from Favorites where is_temporary=:temporary")
    void deleteTemporarySavedObjects(boolean temporary);

    @Query("select is_temporary from Favorites where id=:objectId and type=:objectType")
    boolean getObjectStatus(Integer objectId,String objectType);

    @Query("select * from Favorites")
    List<SearchObject> getSearchObjects();

    ///// Movies

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Query("Select * from Movies")
    List<Movie> getLocalMovies();

    @Query("select count() from Movies where id=:movieId")
    Integer movieExists(Integer movieId);

    @Query("select * from Movies where id=:movieId")
    Movie getMovieFromId(Integer movieId);

    @Query("select * from Movies where title like :search")
    List<Movie> getMoviesFromSearch(String search);



}
