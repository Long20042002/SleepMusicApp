package com.prox.appsleep.database.dbFavoriteMusic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.prox.appsleep.model.FavoriteMusic;

import java.util.List;

@Dao
public interface FavoriteMusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMusic favoriteMusic);

    @Update
    void update(FavoriteMusic favoriteMusic);

    @Delete
    void delete(FavoriteMusic favoriteMusic);

    @Query("SELECT * FROM favorite_song_table")
    LiveData<List<FavoriteMusic>> getAllFavoriteMusic();

    @Query("UPDATE favorite_song_table SET isPlaying = 0")
    void setIsPlayingFalse();

    @Query("SELECT * FROM favorite_song_table WHERE isPlaying = 1")
    LiveData<List<FavoriteMusic>> getSongFavoritePlaying();
}
