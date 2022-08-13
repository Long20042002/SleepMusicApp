package com.prox.appsleep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.prox.appsleep.database.dbAlarm.AlarmDatabase;
import com.prox.appsleep.database.dbAlarm.AlarmViewModel;
import com.prox.appsleep.model.Alarm;
import com.prox.appsleep.service.PlayMusicService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d("TAG", "onReceive: nhan broat cast");

        Bundle bundle = intent.getBundleExtra("bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        Alarm alarmSend = AlarmDatabase.getInstance(context.getApplicationContext())
                .alarmDao()
                .getAlarmFromID(alarm.getBroadCastId());

        if (alarmSend == null || !alarmSend.isEnable()) {
            return;
        }

        if (alarm.isMon() == alarm.isTue() == alarm.isWed() == alarm.isThu() == alarm.isFri() == alarm.isSta() == alarm.isSun() == false) {
            Intent myIntent = new Intent(context, PlayMusicService.class);
            myIntent.putExtra("bundle", bundle);
            context.startForegroundService(myIntent);
        } else {
            setAlarmNextDay(alarmSend, context);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            switch (day) {
                case Calendar.SUNDAY:
                    if (alarmSend.isSun()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.MONDAY:
                    if (alarmSend.isMon()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.TUESDAY:
                    if (alarmSend.isTue()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if (alarmSend.isWed()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.THURSDAY:
                    if (alarmSend.isThu()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.FRIDAY:
                    if (alarmSend.isFri()) {
                        startService(context, bundle);
                    }
                    break;
                case Calendar.SATURDAY:
                    if (alarmSend.isSta()) {
                        startService(context, bundle);
                    }
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startService(Context context, Bundle bundle) {
        Intent myIntent = new Intent(context, PlayMusicService.class);
        myIntent.putExtra("bundle", bundle);
        context.startForegroundService(myIntent);
    }

    private void setAlarmNextDay(Alarm alarm, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHourAlarm());
        calendar.set(Calendar.MINUTE, alarm.getMinutesAlarm());
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.getBroadCastId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent);
    }
}
