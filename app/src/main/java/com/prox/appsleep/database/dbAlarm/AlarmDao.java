package com.prox.appsleep.database.dbAlarm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.prox.appsleep.model.Alarm;
import com.prox.appsleep.model.CategoryIconModel;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Query("Update alarm_table set nameOfMixAlarm = 'My Alarm Mix' where nameOfMixAlarm =:nameMix")
    void updateNameMixAlarm(String nameMix);

    @Delete
    void delete(Alarm alarm);

    @Query("SELECT * FROM alarm_table order by hourAlarm asc, minutesAlarm asc")
    LiveData<List<Alarm>> getAllAlarm();

    @Query("SELECT * FROM alarm_table WHERE broadCastId =:id")
    Alarm getAlarmFromID(int id);
}
