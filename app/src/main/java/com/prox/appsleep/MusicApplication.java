package com.prox.appsleep;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.sharedpreferences.DataLocalManger;
import com.proxglobal.proxads.ads.openads.AppOpenManager;
import com.proxglobal.proxads.ads.openads.ProxOpenAdsApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicApplication extends ProxOpenAdsApplication {

    public static final String CHANNEL_ID = "CHANNEL_MUSIC_APP";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        DataLocalManger.init(getApplicationContext());
        AppOpenManager.getInstance().registerDisableOpenAdsAt(SplashActivity.class);
    }

    @Override
    protected String getOpenAdsId() {
        return getResources().getString(R.string.id_open_app);
    }

    @Override
    public List<String> getListTestDeviceId() {
        return Arrays.asList("5AF8088AC0437DD92A31FA2F8E8181FB");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel music",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0});
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
