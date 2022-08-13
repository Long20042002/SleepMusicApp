package com.prox.appsleep.database.dbCategoryIconModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;

import java.util.List;

@Dao
public interface CategoryIconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryIconModel categoryIconModel);

    @Update
    void update(CategoryIconModel categoryIconModel);

    @Delete
    void delete(CategoryIconModel categoryIconModel);

    @Query("SELECT * FROM category_icon_model_table WHERE isPlaying = 1")
    List<CategoryIconModel> getCategoryIconModelPlaying();

    @Query("SELECT * FROM category_icon_model_table")
    LiveData<List<CategoryIconModel>> getAllCategoryIconModel();

    @Query("SELECT * FROM category_icon_model_table WHERE isPlaying = 1")
    LiveData<List<CategoryIconModel>> getAllCategoryIconModelTrue();

    @Query("SELECT * FROM category_icon_model_table WHERE categoryMusic = 1")
    LiveData<List<CategoryIconModel>> getCategoryIconModelRain();

    @Query("SELECT * FROM category_icon_model_table WHERE categoryMusic = 2")
    LiveData<List<CategoryIconModel>> getCategoryIconModelForest();

    @Query("SELECT * FROM category_icon_model_table WHERE categoryMusic = 3")
    LiveData<List<CategoryIconModel>> getCategoryIconModelCity();

    @Query("SELECT * FROM category_icon_model_table WHERE categoryMusic = 4")
    LiveData<List<CategoryIconModel>> getCategoryIconModelRelax();

    @Query("SELECT * FROM category_icon_model_table WHERE categoryMusic = 5")
    LiveData<List<CategoryIconModel>> getCategoryIconModelASMR();

    @Query("UPDATE category_icon_model_table SET isPlaying = 'false'")
    void setIsPlayingFalse();
}
