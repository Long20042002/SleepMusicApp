package com.prox.appsleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.prox.appsleep.service.PlayMusicService;
import com.prox.appsleep.sharedpreferences.DataLocalManger;
import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class SplashActivity extends AppCompatActivity {

    public Animation animation, animation1, animation2;
    public ImageView ivStar1, ivStar2, ivStar3;
    public boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //star blink
        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_star_splash);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_star_splash2);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_star_splash3);
        ivStar1.startAnimation(animation);
        ivStar2.startAnimation(animation1);
        ivStar3.startAnimation(animation2);

        ProxAds.getInstance().showSplash(this, new AdsCallback() {
            @Override
            public void onShow() {
                super.onShow();
                if (DataLocalManger.getStatusMedia()) {
                    sendActionToService(PlayMusicService.ACTION_PAUSE);
                    isPause = true;
                }
            }

            @Override
            public void onClosed() {
                super.onClosed();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (isPause) {
                    sendActionToService(PlayMusicService.ACTION_RESUME);
                    isPause = false;
                }
            }

            @Override
            public void onError() {
                super.onError();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (isPause) {
                    sendActionToService(PlayMusicService.ACTION_RESUME);
                    isPause = false;
                }
            }
        }, getResources().getString(R.string.id_interstitial_splash), null, 12000);
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(getApplicationContext(), PlayMusicService.class);
        intent.putExtra("action_music_service", action);

        startService(intent);
    }
}