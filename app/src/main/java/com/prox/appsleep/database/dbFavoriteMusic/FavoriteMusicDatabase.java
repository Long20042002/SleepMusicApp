package com.prox.appsleep.database.dbFavoriteMusic;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prox.appsleep.model.FavoriteMusic;

@Database(entities = {FavoriteMusic.class}, version = 1)
@TypeConverters({TypeConverterListMusicFavorite.class})
public abstract class FavoriteMusicDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "favorite_music.db";
    private static FavoriteMusicDatabase instance;

    public static synchronized FavoriteMusicDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),FavoriteMusicDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract FavoriteMusicDao favoriteMusicDao();
}
