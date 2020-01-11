package gr.advantage.adam.themoviedb.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import java.util.List;

import gr.advantage.adam.themoviedb.database.MyAppDatabase;

@Entity(tableName = "Favorites",primaryKeys = {"id","type"})
public class SearchObject {

    @NonNull
    Integer id;
    @NonNull
    String type;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="release")
    private String release;
    @ColumnInfo(name="rating")
    private String rating;
    @ColumnInfo(name="is_temporary")
    boolean isTemporary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }

    public List<SearchObject> getSearchObjectFromDatabase(Context context){
        MyAppDatabase myAppDatabase = MyAppDatabase.getAppDatabaseFallBack(context);
        return myAppDatabase.MyDao().getSearchObjects();
    }

}
