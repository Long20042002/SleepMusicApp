package com.prox.appsleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.prox.appsleep.model.Alarm;
import com.prox.appsleep.service.CountDownTimerService;
import com.prox.appsleep.service.PlayMusicService;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    public ImageView ivStopAlarm;
    public int timeSnooze;
    public TextClock tcTimeCurrent;
    public TextView tvSnoozeAlarm, tvDateAlarm, tvSnoozeCountDown;
    public CountDownTimer cdt;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("action", 0);
            finishAlarmActivity(action);
        }
    };

    public BroadcastReceiver broadcastReceiverCDAlarm = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTimeCDAlarm(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Window win = getWindow();



        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        this.registerReceiver(broadcastReceiverCDAlarm, new IntentFilter("count_down_alarm"));
        this.registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity_alarm"));
        setStatusBarColor();

        ivStopAlarm = findViewById(R.id.ivStopAlarm);
        tcTimeCurrent = findViewById(R.id.tcTimeCurrent);
        tvSnoozeAlarm = findViewById(R.id.tvSnoozeAlarm);
        tvDateAlarm = findViewById(R.id.tvDateAlarm);
        tvSnoozeCountDown = findViewById(R.id.tvSnoozeCountDown);


        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfDayMonth = new SimpleDateFormat("dd");
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE");
        Date date = new Date();
        String strMonth = sdfMonth.format(date);
        String strDate = sdfDayMonth.format(date);
        String strDay = sdfDay.format(date);
        tvDateAlarm.setText(strDay+", "+ strDate+" "+strMonth);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        if (bundle == null) {
            return;
        }
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");
        timeSnooze = alarm.getTimeSnooze() * 60000;

        ivStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, PlayMusicService.class);
                stopService(intent);
                finishAndRemoveTask();
            }
        });

        tvSnoozeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, PlayMusicService.class);
                stopService(intent);

                  cdt = new CountDownTimer(timeSnooze, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int minutes = (int) ((millisUntilFinished/1000)%3600) / 60;
                        int seconds = (int) (( millisUntilFinished / 1000 ) % 60);
                        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);


                        tvSnoozeCountDown.setVisibility(View.VISIBLE);
                        tvSnoozeCountDown.setText(String.valueOf(timeFormatted));
                        tvSnoozeAlarm.setClickable(false);
                    }

                    @Override
                    public void onFinish() {
                        tvSnoozeCountDown.setVisibility(View.GONE);
                        Intent intentBR = new Intent(getApplicationContext(), AlarmReceiver.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("alarm", alarm);
                        intentBR.putExtra("bundle",bundle );
                        finishAndRemoveTask();
                        sendBroadcast(intentBR);
                        tvSnoozeAlarm.setClickable(true);
                    }
                };
                cdt.start();
            }
        });
    }

    private void setTimeCDAlarm(Intent intent) {
        if (intent.getExtras() != null){
            long millisUntilFinished = intent.getLongExtra("time_count_down_alarm", 0);

            int minutes = (int) ((millisUntilFinished/1000)%3600) / 60;
            int seconds = (int) (( millisUntilFinished / 1000 ) % 60);
            String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

            if (millisUntilFinished >= 1000) {
                tvSnoozeCountDown.setVisibility(View.VISIBLE);
                tvSnoozeCountDown.setText(String.valueOf(timeFormatted));
                tvSnoozeAlarm.setClickable(false);
            }else {
                tvSnoozeCountDown.setVisibility(View.GONE);
                tvSnoozeAlarm.setClickable(true);
                finishAndRemoveTask();
            }
        }
    }

    private void finishAlarmActivity(int action) {
        if (action == PlayMusicService.ACTION_STOP_ALARM){
            finishAndRemoveTask();
        }
    }

    private void setStatusBarColor(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status_bar));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cdt != null) {
            cdt.cancel();
        }
        this.unregisterReceiver(broadcastReceiverCDAlarm);
        this.unregisterReceiver(broadcastReceiver);
    }
}

