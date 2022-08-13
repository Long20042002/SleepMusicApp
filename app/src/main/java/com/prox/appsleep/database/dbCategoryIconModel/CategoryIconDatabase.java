package com.prox.appsleep.database.dbCategoryIconModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.prox.appsleep.model.CategoryIconModel;

@Database(entities = {CategoryIconModel.class} , version = 1)
public abstract class CategoryIconDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "category_icon_model.db";
    private static CategoryIconDatabase instance;

    public static synchronized CategoryIconDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),CategoryIconDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CategoryIconDao categoryIconDao();
}
