package com.prox.appsleep.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.prox.appsleep.AlarmActivity;
import com.prox.appsleep.MainActivity;
import com.prox.appsleep.R;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.model.Alarm;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.NotificationMediaReceiver;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.prox.appsleep.MusicApplication.CHANNEL_ID;

public class PlayMusicService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_START = 4;
    public static final int ACTION_NEXT = 5;
    public static final int ACTION_PRE = 6;
    public static final int ACTION_STOP = 7;
    public static final int ACTION_DELETE = 8;
    public static final int ACTION_STOP_ALARM = 9;
    public static final int ACTION_SNOOZE = 10;


    private MediaPlayer mediaPlayerAlarm1;
    private MediaPlayer mediaPlayerAlarm2;
    private MediaPlayer mediaPlayerAlarm3;
    private MediaPlayer mediaPlayerAlarm4;
    private MediaPlayer mediaPlayerAlarm5;
    private MediaPlayer mediaPlayerAlarm6;
    private MediaPlayer mediaPlayerAlarm7;
    private MediaPlayer mediaPlayerAlarm8;


    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    private MediaPlayer mediaPlayer4;
    private MediaPlayer mediaPlayer5;
    private MediaPlayer mediaPlayer6;
    private MediaPlayer mediaPlayer7;
    private MediaPlayer mediaPlayer8;
    public boolean isPlaying;
    private CategoryIconModel mCategoryIconModel;
    private Alarm mAlarm;
    public List<CategoryIconModel> categoryIconModelList = new ArrayList<>();

    public CountDownTimer cdt;

    public int progress;
    public CategoryIconModel categoryIconModelMusicPlaying;

    private Vibrator vibrator;
    public boolean isVibrator;

    public Intent intentCDAlarm = new Intent("count_down_alarm");
    public boolean isSnooze = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("ServiceCast")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        Bundle alarmBundle = intent.getBundleExtra("bundle");

        if (bundle != null) {
            CategoryIconModel categoryIconModel = (CategoryIconModel) bundle.get("listSong");
            if (categoryIconModel != null) {
                startMusic(categoryIconModel);
                mCategoryIconModel = categoryIconModel;
                sendNotificationMedia(categoryIconModel);
            }
        }

        if (alarmBundle != null) {
            CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao().setIsPlayingFalse();
            DataLocalManger.setStatusMedia(false);
            FavoriteMusicDatabase.getInstance(getApplicationContext()).favoriteMusicDao().setIsPlayingFalse();
            sendActionToActivity(PlayMusicService.ACTION_CLEAR);
            releaseMusic();
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            Alarm alarm = (Alarm) alarmBundle.getSerializable("alarm");

            startAlarm(alarm);
            mAlarm = alarm;
            isVibrator = alarm.isVibration();
            sendNotificationAlarm(alarm);


            if (Settings.canDrawOverlays(getApplicationContext())) {
                Intent intentAl = new Intent(getApplicationContext(), AlarmActivity.class);
                Bundle mArgs = new Bundle();
                mArgs.putSerializable("alarm", alarm);
                intentAl.putExtra("DATA", mArgs);
                intentAl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intentAl);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isVibrator) {
                    vibrator.vibrate(new long[]{0, 300, 1000}, 0);
                }
            }
        }

        int actionMusic = intent.getIntExtra("action_music_service", 0);
        if ("volume".equals(intent.getAction())) {
            progress = intent.getIntExtra("ctrlVolume", 0);
            if (bundle != null) {
                categoryIconModelMusicPlaying = (CategoryIconModel) bundle.get("musicPlaying");
            }
            controlVolumeMusic(progress, categoryIconModelMusicPlaying);
        }

        handleActionMusic(actionMusic);
        return START_NOT_STICKY;
    }

    private void sendNotificationAlarm(Alarm alarm) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification_alarm);

        remoteViews.setOnClickPendingIntent(R.id.tvTurnOffNotiAlarm, getPendingIntent(this, ACTION_STOP_ALARM));

        remoteViews.setOnClickPendingIntent(R.id.tvSnoozeNotiAlarm, getPendingIntent(this, ACTION_SNOOZE));


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_of_top_home);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_active)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews);

        Notification notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    private void sendNotificationMedia(CategoryIconModel categoryIconModel) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra("openFromNoti", "notiOP");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_of_top_home);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_active)
                .setContentTitle("Sleep music")
                .setContentText("Now playing")
                .setContentIntent(notifyPendingIntent)
                .setLargeIcon(bitmap)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1 /* #1: pause button */));

        if (isPlaying) {
            notificationBuilder
                    .addAction(R.drawable.ic_pausenoti, "Pause", getPendingIntent(this, ACTION_PAUSE)) // #0
                    .addAction(R.drawable.ic_stop, "Stop", getPendingIntent(this, ACTION_CLEAR))  // #1
                    .addAction(R.drawable.ic_alarm_active, "Alarm", getPendingIntent(this, ACTION_NEXT))   // #2
                    .addAction(R.drawable.ic_favorite, "Favorite", getPendingIntent(this, ACTION_PRE));
        } else {
            notificationBuilder
                    .addAction(R.drawable.ic_playnoti, "Pause", getPendingIntent(this, ACTION_RESUME)) // #0
                    .addAction(R.drawable.ic_stop, "Stop", getPendingIntent(this, ACTION_CLEAR))  // #1
                    .addAction(R.drawable.ic_alarm_active, "Alarm", getPendingIntent(this, ACTION_NEXT))   // #2
                    .addAction(R.drawable.ic_favorite, "Favorite", getPendingIntent(this, ACTION_PRE));
        }
        Notification notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, NotificationMediaReceiver.class);
        intent.putExtra("action_music", action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                sendActionToActivity(ACTION_PAUSE);
                break;
            case ACTION_RESUME:
                resumeMusic();
                sendActionToActivity(ACTION_RESUME);
                break;
            case ACTION_STOP:
                stopMusic();
                categoryIconModelList.clear();
                break;
            case ACTION_DELETE:
                break;
            case ACTION_STOP_ALARM:
                sendActionToAlarmActivity(ACTION_STOP_ALARM);
                stopSelf();
                stopAlarm();
                break;
            case ACTION_SNOOZE:
                if (!isSnooze) {
                    snoozeCountDown();
                }
                break;
            case ACTION_CLEAR:
                stopSelf();
                CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao().setIsPlayingFalse();
                FavoriteMusicDatabase.getInstance(getApplicationContext()).favoriteMusicDao().setIsPlayingFalse();
                DataLocalManger.setStatusMedia(false);
                sendActionToActivity(ACTION_CLEAR);
                break;
        }
    }

    private void snoozeCountDown() {
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (mediaPlayerAlarm1 != null) {
            mediaPlayerAlarm1.pause();
        }
        if (mediaPlayerAlarm2 != null) {
            mediaPlayerAlarm2.pause();
        }
        if (mediaPlayerAlarm3 != null) {
            mediaPlayerAlarm3.pause();
        }
        if (mediaPlayerAlarm4 != null) {
            mediaPlayerAlarm4.pause();
        }
        if (mediaPlayerAlarm5 != null) {
            mediaPlayerAlarm5.pause();
        }
        if (mediaPlayerAlarm6 != null) {
            mediaPlayerAlarm6.pause();
        }
        if (mediaPlayerAlarm7 != null) {
            mediaPlayerAlarm7.pause();
        }
        if (mediaPlayerAlarm8 != null) {
            mediaPlayerAlarm8.pause();
        }

        isSnooze = true;


        int timeCountDown = mAlarm.getTimeSnooze() * 60000;
        cdt = new CountDownTimer(timeCountDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intentCDAlarm.putExtra("time_count_down_alarm", millisUntilFinished);
                sendBroadcast(intentCDAlarm);
            }

            @Override
            public void onFinish() {

                isSnooze = false;

                if (mediaPlayerAlarm1 != null) {
                    mediaPlayerAlarm1.start();
                }
                if (mediaPlayerAlarm2 != null) {
                    mediaPlayerAlarm2.start();
                }
                if (mediaPlayerAlarm3 != null) {
                    mediaPlayerAlarm3.start();
                }
                if (mediaPlayerAlarm4 != null) {
                    mediaPlayerAlarm4.start();
                }
                if (mediaPlayerAlarm5 != null) {
                    mediaPlayerAlarm5.start();
                }
                if (mediaPlayerAlarm6 != null) {
                    mediaPlayerAlarm6.start();
                }
                if (mediaPlayerAlarm7 != null) {
                    mediaPlayerAlarm7.start();
                }
                if (mediaPlayerAlarm8 != null) {
                    mediaPlayerAlarm8.start();
                }

                if (Settings.canDrawOverlays(getApplicationContext())) {
                    Intent intentAl = new Intent(getApplicationContext(), AlarmActivity.class);
                    Bundle mArgs = new Bundle();
                    mArgs.putSerializable("alarm", mAlarm);
                    intentAl.putExtra("DATA", mArgs);
                    intentAl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intentAl);
                }
            }
        };
        cdt.start();
    }

    private void startAlarm(Alarm alarm) {
        if (alarm.getCategoryIconModelList() != null) {

            for (int i = 1; i < alarm.getCategoryIconModelList().size() + 1; i++) {
                switch (i) {
                    case 1:
                        mediaPlayerAlarm1 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(0).getMusicResource());
                        mediaPlayerAlarm1.setLooping(true);
                        mediaPlayerAlarm1.start();
                        break;
                    case 2:
                        mediaPlayerAlarm2 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(1).getMusicResource());
                        mediaPlayerAlarm2.setLooping(true);
                        mediaPlayerAlarm2.start();
                        break;
                    case 3:
                        mediaPlayerAlarm3 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(2).getMusicResource());
                        mediaPlayerAlarm3.setLooping(true);
                        mediaPlayerAlarm3.start();
                        break;
                    case 4:
                        mediaPlayerAlarm4 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(3).getMusicResource());
                        mediaPlayerAlarm4.setLooping(true);
                        mediaPlayerAlarm4.start();
                        isPlaying = true;
                        break;
                    case 5:
                        mediaPlayerAlarm5 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(4).getMusicResource());
                        mediaPlayerAlarm5.setLooping(true);
                        mediaPlayerAlarm5.start();
                        break;
                    case 6:
                        mediaPlayerAlarm6 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(5).getMusicResource());
                        mediaPlayerAlarm6.setLooping(true);
                        mediaPlayerAlarm6.start();
                        break;
                    case 7:
                        mediaPlayerAlarm7 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(6).getMusicResource());
                        mediaPlayerAlarm7.setLooping(true);
                        mediaPlayerAlarm7.start();
                        break;
                    case 8:
                        mediaPlayerAlarm8 = MediaPlayer.create(getApplicationContext(), alarm.getCategoryIconModelList().get(7).getMusicResource());
                        mediaPlayerAlarm8.setLooping(true);
                        mediaPlayerAlarm8.start();
                        break;
                    default:
                        Toast.makeText(this, "Up to 8 sounds", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void stopAlarm() {
        if (mediaPlayerAlarm1 != null) {
            mediaPlayerAlarm1.release();
        }
        if (mediaPlayerAlarm2 != null) {
            mediaPlayerAlarm2.release();
        }
        if (mediaPlayerAlarm3 != null) {
            mediaPlayerAlarm3.release();
        }
        if (mediaPlayerAlarm4 != null) {
            mediaPlayerAlarm4.release();
        }
        if (mediaPlayerAlarm5 != null) {
            mediaPlayerAlarm5.release();
        }
        if (mediaPlayerAlarm6 != null) {
            mediaPlayerAlarm6.release();
        }
        if (mediaPlayerAlarm7 != null) {
            mediaPlayerAlarm7.release();
        }
        if (mediaPlayerAlarm8 != null) {
            mediaPlayerAlarm8.release();
        }
    }

    //-------remote music--------------

    private void controlVolumeMusic(int progress, CategoryIconModel categoryIconModelPlaying) {
        final float log1 = (float) (Math.log(progress + 1) / Math.log(100));
        for (int i = 0; i < categoryIconModelList.size(); i++) {
            if (categoryIconModelList.get(i).getMusicResource() == categoryIconModelPlaying.getMusicResource()) {
                switch (i + 1) {
                    case 1:
                        mediaPlayer1.setVolume(log1, log1);
                        break;
                    case 2:
                        mediaPlayer2.setVolume(log1, log1);
                        break;
                    case 3:
                        mediaPlayer3.setVolume(log1, log1);
                        break;
                    case 4:
                        mediaPlayer4.setVolume(log1, log1);
                        break;
                    case 5:
                        mediaPlayer5.setVolume(log1, log1);
                        break;
                    case 6:
                        mediaPlayer6.setVolume(log1, log1);
                        break;
                    case 7:
                        mediaPlayer7.setVolume(log1, log1);
                        break;
                    case 8:
                        mediaPlayer8.setVolume(log1, log1);
                        break;
                }
            }
        }
    }

    private void stopMusic() {
        if (mediaPlayer1 != null && isPlaying) {
            mediaPlayer1.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer3 != null) {
            mediaPlayer3.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer4 != null) {
            mediaPlayer4.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer5 != null) {
            mediaPlayer5.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer6 != null) {
            mediaPlayer6.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer7 != null) {
            mediaPlayer7.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer8 != null) {
            mediaPlayer8.stop();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }
    }

    private void releaseMusic() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }

        if (mediaPlayer3 != null) {
            mediaPlayer3.release();
            mediaPlayer3 = null;
        }

        if (mediaPlayer4 != null) {
            mediaPlayer4.release();
            mediaPlayer4 = null;
        }

        if (mediaPlayer5 != null) {
            mediaPlayer5.release();
            mediaPlayer5 = null;
        }

        if (mediaPlayer6 != null) {
            mediaPlayer6.release();
            mediaPlayer6 = null;
        }

        if (mediaPlayer7 != null) {
            mediaPlayer7.release();
            mediaPlayer7 = null;
        }

        if (mediaPlayer8 != null) {
            mediaPlayer8.release();
            mediaPlayer8 = null;
        }
    }

    public void pauseMusic() {

        if (mediaPlayer1 != null && isPlaying) {
            mediaPlayer1.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer3 != null) {
            mediaPlayer3.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer4 != null) {
            mediaPlayer4.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer5 != null) {
            mediaPlayer5.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer6 != null) {
            mediaPlayer6.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer7 != null) {
            mediaPlayer7.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer8 != null) {
            mediaPlayer8.pause();
            isPlaying = false;
            sendNotificationMedia(mCategoryIconModel);
        }

        sendActionToActivity(ACTION_PAUSE);
    }

    public void resumeMusic() {
        if (mediaPlayer1 != null && !isPlaying) {
            mediaPlayer1.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer3 != null) {
            mediaPlayer3.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer4 != null) {
            mediaPlayer4.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer5 != null) {
            mediaPlayer5.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer6 != null) {
            mediaPlayer6.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer7 != null) {
            mediaPlayer7.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }

        if (mediaPlayer8 != null) {
            mediaPlayer8.start();
            isPlaying = true;
            sendNotificationMedia(mCategoryIconModel);
        }
        sendActionToActivity(ACTION_RESUME);
    }

    public void startMusic(CategoryIconModel categoryIconModel) {

        for (int i = 0; i < categoryIconModelList.size(); i++) {
            if (categoryIconModelList.get(i).getMusicResource() == categoryIconModel.getMusicResource()) {
                releaseMusic();
                categoryIconModelList.remove(i);
                if (categoryIconModelList.size() == 0) {
                    stopSelf();
                    isPlaying = false;
                    FavoriteMusicDatabase.getInstance(getApplicationContext()).favoriteMusicDao().setIsPlayingFalse();
                    sendActionToActivity(ACTION_CLEAR);
                    return;
                }
                for (int j = 1; j < categoryIconModelList.size() + 1; j++) {
                    switch (j) {
                        case 1:
                            if (mediaPlayer1 != null) {
                                return;
                            }
                            mediaPlayer1 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(0).getMusicResource());
                            mediaPlayer1.setLooping(true);
                            mediaPlayer1.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 2:
                            if (mediaPlayer2 != null) {
                                return;
                            }
                            mediaPlayer2 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(1).getMusicResource());
                            mediaPlayer2.setLooping(true);
                            mediaPlayer2.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 3:
                            if (mediaPlayer3 != null) {
                                return;
                            }
                            mediaPlayer3 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(2).getMusicResource());
                            mediaPlayer3.setLooping(true);
                            mediaPlayer3.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 4:
                            if (mediaPlayer4 != null) {
                                return;
                            }
                            mediaPlayer4 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(3).getMusicResource());
                            mediaPlayer4.setLooping(true);
                            mediaPlayer4.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 5:
                            if (mediaPlayer5 != null) {
                                return;
                            }
                            mediaPlayer5 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(4).getMusicResource());
                            mediaPlayer5.setLooping(true);
                            mediaPlayer5.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 6:
                            if (mediaPlayer6 != null) {
                                return;
                            }
                            mediaPlayer6 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(5).getMusicResource());
                            mediaPlayer6.setLooping(true);
                            mediaPlayer6.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 7:
                            if (mediaPlayer7 != null) {
                                return;
                            }
                            mediaPlayer7 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(6).getMusicResource());
                            mediaPlayer7.setLooping(true);
                            mediaPlayer7.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                        case 8:
                            if (mediaPlayer8 != null) {
                                return;
                            }
                            mediaPlayer8 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(7).getMusicResource());
                            mediaPlayer8.setLooping(true);
                            mediaPlayer8.start();
                            isPlaying = true;
                            sendActionToActivity(ACTION_START);
                            break;
                    }
                }
                return;
            }
        }

        if (categoryIconModelList.size() > 7) {
            return;
        } else {
            categoryIconModelList.add(categoryIconModel);
        }

        switch (categoryIconModelList.size()) {
            case 1:
                mediaPlayer1 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(0).getMusicResource());
                mediaPlayer1.setLooping(true);
                mediaPlayer1.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 2:
                mediaPlayer2 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(1).getMusicResource());
                mediaPlayer2.setLooping(true);
                mediaPlayer2.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 3:
                mediaPlayer3 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(2).getMusicResource());
                mediaPlayer3.setLooping(true);
                mediaPlayer3.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 4:
                mediaPlayer4 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(3).getMusicResource());
                mediaPlayer4.setLooping(true);
                mediaPlayer4.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 5:
                mediaPlayer5 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(4).getMusicResource());
                mediaPlayer5.setLooping(true);
                mediaPlayer5.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 6:
                mediaPlayer6 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(5).getMusicResource());
                mediaPlayer6.setLooping(true);
                mediaPlayer6.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 7:
                mediaPlayer7 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(6).getMusicResource());
                mediaPlayer7.setLooping(true);
                mediaPlayer7.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            case 8:
                mediaPlayer8 = MediaPlayer.create(getApplicationContext(), categoryIconModelList.get(7).getMusicResource());
                mediaPlayer8.setLooping(true);
                mediaPlayer8.start();
                isPlaying = true;
                sendActionToActivity(ACTION_START);
                break;
            default:
                Toast.makeText(this, "Up to 8 sounds", Toast.LENGTH_SHORT).show();
        }

    }

    //--------------------------------

    public void sendActionToActivity(int action) {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("objectSong", mCategoryIconModel);
        bundle.putBoolean("status_player", isPlaying);
        bundle.putInt("action_music", action);
        bundle.putSerializable("listMusic", (Serializable) categoryIconModelList);

        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendActionToAlarmActivity(int action) {
        Intent intent = new Intent("send_data_to_activity_alarm");
        intent.putExtra("action", action);
        (getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (cdt != null) {
            cdt.cancel();
        }
        releaseMusic();
        stopAlarm();
        DataLocalManger.setCountMusicPlaying(0);
    }
}
