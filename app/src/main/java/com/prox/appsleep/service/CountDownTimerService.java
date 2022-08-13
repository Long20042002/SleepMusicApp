package com.prox.appsleep.service;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

public class CountDownTimerService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "your.package.name";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("setTimeOffMusic".equals(intent.getAction())){
            int timer = intent.getIntExtra("timer", 0);
            cdt = new CountDownTimer(timer, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    bi.putExtra("countdown", millisUntilFinished);
                    sendBroadcast(bi);
                }
                @Override
                public void onFinish() {
                    bi.putExtra("countdownTimerFinished", true);
                    sendBroadcast(bi);
                    stopForeground(true);
                    sendActionToService(PlayMusicService.ACTION_PAUSE);
                    DataLocalManger.setStatusMedia(false);
                    stopSelf();
                }
            }; cdt.start();
        }

        return START_STICKY;
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(getApplicationContext(), PlayMusicService.class);
        intent.putExtra("action_music_service", action);

        getApplicationContext().startService(intent);
    }

    @Override
    public void onDestroy() {
        cdt.cancel();
        super.onDestroy();
    }
}
