package com.prox.appsleep.database.dbAlarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.model.Alarm;

import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    private AlarmDatabase alarmDatabase;
    public AlarmViewModel(@NonNull Application application) {
        super(application);
        alarmDatabase = AlarmDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Alarm>> getAllAlarm(){
        return alarmDatabase.alarmDao().getAllAlarm();
    }
}
