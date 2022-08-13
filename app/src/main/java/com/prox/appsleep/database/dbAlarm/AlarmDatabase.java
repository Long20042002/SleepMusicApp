package com.prox.appsleep.database.dbAlarm;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prox.appsleep.database.dbFavoriteMusic.TypeConverterListMusicFavorite;
import com.prox.appsleep.model.Alarm;

@Database(entities = {Alarm.class}, version = 1)
@TypeConverters({TypeConverterListMusicFavorite.class})
public abstract class AlarmDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "alarm.db";
    private static AlarmDatabase instance;

    public static synchronized AlarmDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AlarmDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AlarmDao alarmDao();
}
