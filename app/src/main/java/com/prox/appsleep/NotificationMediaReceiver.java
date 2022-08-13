package com.prox.appsleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.prox.appsleep.service.PlayMusicService;

public class NotificationMediaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("action_music", 0);
        int progress = intent.getIntExtra("ctrlVolume", 0);

        Intent intentService = new Intent(context, PlayMusicService.class);
        intentService.putExtra("action_music_service", actionMusic);
        intentService.putExtra("ctrlVolume", progress);

        context.startService(intentService);
    }
}
