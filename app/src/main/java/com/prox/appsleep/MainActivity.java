package com.prox.appsleep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.databinding.ActivityMainBinding;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.service.PlayMusicService;
import com.prox.appsleep.sharedpreferences.DataLocalManger;
import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.ads.openads.AppOpenManager;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.rate.ProxRateDialog;
import com.proxglobal.rate.RatingDialogListener;

import java.io.Serializable;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    public boolean isBack;
    public boolean isFavorite;
    private boolean isRequestingPermission = false;
    public boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String label = intent.getStringExtra("openFromNoti");
//        if (label != null) {
//            if (!label.equals("notiOP")) {
//                showRateDialog();
//            }
//        }

        showRateDialog();

        int countOpenApp = DataLocalManger.getCountOpenApp();
        if (countOpenApp > 0){
            if (label == null){
                ProxRateDialog.showIfNeed(getApplicationContext(), getSupportFragmentManager());
            }
        }
        countOpenApp++;
        DataLocalManger.setCountOpenApp(countOpenApp);

        ProxAds.getInstance().initInterstitial(this, ProxUtils.TEST_INTERSTITIAL_ID, "vz3ebfacd56a34480da8", "inter");
        ProxAds.getInstance().showBanner(this, binding.bannerContainer, "ca-app-pub-3940256099942544/6300978111", new AdsCallback() {
            @Override
            public void onShow() {
                super.onShow();
            }

            @Override
            public void onClosed() {
                super.onClosed();
            }

            @Override
            public void onError() {
                super.onError();
            }
        });

        if (!Settings.canDrawOverlays(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_permission_user, null);
            builder.setView(viewP);
            Dialog dialogP = builder.create();
            dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialogP.show();

            TextView tvOk = viewP.findViewById(R.id.tvOkPermission);
            TextView tvCancel = viewP.findViewById(R.id.tvCancelPermission);

            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogP.dismiss();
                    AppOpenManager.getInstance().disableOpenAds();
                    isRequestingPermission = true;
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogP.dismiss();
                }
            });

        }


        if (!DataLocalManger.getFirstInstalled()) {
            addMusicToRoom();
            DataLocalManger.setFirstInstalled(true);
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_alarm_clock, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_favorite) {
                    if (!isFavorite) {
                        ProxAds.getInstance().showInterstitial(MainActivity.this, "inter", new AdsCallback() {
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
                                NavigationUI.onNavDestinationSelected(item, navController);
                                isFavorite=true;
                                if (isPause){
                                    sendActionToService(PlayMusicService.ACTION_RESUME);
                                    isPause = false;
                                }
                            }

                            @Override
                            public void onError() {
                                super.onError();
                                NavigationUI.onNavDestinationSelected(item, navController);
                                isFavorite=true;
                                if (isPause){
                                    sendActionToService(PlayMusicService.ACTION_RESUME);
                                    isPause = false;
                                }
                            }
                        });
                    }else {
                        NavigationUI.onNavDestinationSelected(item, navController);
                        isFavorite = true;
                    }
                } else if (item.getItemId() == R.id.navigation_home) {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    isFavorite = false;
                } else if (item.getItemId() == R.id.navigation_home) {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    isFavorite = false;
                } else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    isFavorite = false;
                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isRequestingPermission) {
            isRequestingPermission = false;
            AppOpenManager.getInstance().enableOpenAds();
        }
    }

    public void addMusicToRoom() {
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_thunderstorm, R.raw.rain_thunders));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rainmedium, R.raw.rain_normal));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rain_light, R.raw.rain_light));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rainontop, R.raw.rain_on_roof));
//        categoryIconModelList.add(new CategoryIcocategoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rainunderumbrella, R.raw.rain_under_umbrella));nModel(R.drawable.ic_rainonwindow, R.raw.rain_on_window));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rainonleaf, R.raw.rain_on_leaves));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_raintwater, R.raw.rain_water));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_rainocean, R.raw.rain_ocean));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_thunderstorm, "Thunderstorm", R.raw.rain_thunders, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainmedium, "Rain Medium", R.raw.rain_normal, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rain_light, "Rain Light", R.raw.rain_light, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainunderumbrella, "Rain Under Umbrella", R.raw.rain_under_umbrella, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainontop, "Rain on Roof", R.raw.rain_on_roof, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainonwindow, "Rain on Window", R.raw.rain_on_window, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainonleaf, "Rain on Leaf", R.raw.rain_on_leaves, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_raintwater, "Rain Water", R.raw.rain_water, false, 1, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_rainocean, "Rain Ocean", R.raw.rain_ocean, false, 1, 50));

        //        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_icforest, R.raw.forest_forest));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_streamsound, R.raw.forest_creek));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_fallingleaves, R.raw.forest_leaves));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_bird, R.raw.forest_birds));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_waterfall, R.raw.forest_waterfall));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_wind, R.raw.forest_wind));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_fire, R.raw.forest_fire));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_cricket, R.raw.forest_grasshoppers));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_frog, R.raw.forest_frogs));

        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_forest_category, "Forest Forest", R.raw.forest_forest, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_streamsound, "Forest Creek", R.raw.forest_creek, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_fallingleaves, "Forest Leaves", R.raw.forest_leaves, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_bird, "Forest Birds", R.raw.forest_birds, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_waterfall, "Forest Waterfall", R.raw.forest_waterfall, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_wind, "Forest Wind", R.raw.forest_wind, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_fire, "Forest Fire", R.raw.forest_fire, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_cricket, "Forest Grasshopper", R.raw.forest_grasshoppers, false, 2, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_frog, "Forest Frogs", R.raw.forest_frogs, false, 2, 50));

        //        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_car, R.raw.city_car));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_traffic, R.raw.city_traffic));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_union, R.raw.city_rails));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_train, R.raw.city_subway));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_planes, R.raw.city_airplane));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_meal, R.raw.city_restaurant));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_keyboard, R.raw.city_keyboard));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_fan, R.raw.city_fan));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_radio, R.raw.city_washing_machine));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_radio, "City Washing Machine", R.raw.city_washing_machine, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_car, "City Car", R.raw.city_car, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_traffic, "City Traffic", R.raw.city_traffic, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_union, " City Rails", R.raw.city_rails, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_train, "City Subway", R.raw.city_subway, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_planes, "City Airplane", R.raw.city_airplane, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_meal, "City Restaurant", R.raw.city_restaurant, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_keyboard, "City Keyboard", R.raw.city_keyboard, false, 3, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_fan, "City Fan", R.raw.city_fan, false, 3, 50));

        //        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_piano, R.raw.meditation_piano));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_flute, R.raw.meditation_flute));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_stones, R.raw.meditation_stones));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_bowl, R.raw.meditation_bowl));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_bell, R.raw.meditation_bells));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_windchimes, R.raw.meditation_wind_chimes));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_noise, R.raw.meditation_white_noise));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_noise, R.raw.meditation_pink_noise));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_noise, R.raw.meditation_brown_noise));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_alpha, R.raw.binaural_alpha));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_beta, R.raw.binaural_beta));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_gamma, R.raw.binaural_gamma));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_delta, R.raw.binaural_delta));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_theta, R.raw.binaural_theta));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_piano, "Meditation Piano", R.raw.meditation_piano, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_flute, "Meditation Flute", R.raw.meditation_flute, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_stones, "Meditation Stones", R.raw.meditation_stones, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_bowl, "Meditation Bowl", R.raw.meditation_bowl, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_bell, "Meditation Bell", R.raw.meditation_bells, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_windchimes, "Meditation Chimes", R.raw.meditation_wind_chimes, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_noise, "Meditation White Noise", R.raw.meditation_white_noise, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_noise1, "Meditation Pink Noise", R.raw.meditation_pink_noise, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_noise2, "Meditation Brown Noise", R.raw.meditation_brown_noise, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_alpha, "Binaural Alpha", R.raw.binaural_alpha, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_beta, "Binaural Beta", R.raw.binaural_beta, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_gamma, "Binaural Gamma", R.raw.binaural_gamma, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_delta, "Binaural Delta", R.raw.binaural_delta, false, 4, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_theta, "Binaural Theta", R.raw.binaural_theta, false, 4, 50));

        //        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_scratching, R.raw.asmr_scratching));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_tapping, R.raw.asmr_tapping));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_pageturning, R.raw.asmr_page_turning));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_hairclippers, R.raw.asmr_hair_clippers));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_chewing, R.raw.asmr_chewing));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_whispering, R.raw.asmr_whispering));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_breathing, R.raw.asmr_breathing));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_crackling, R.raw.asmr_crackling));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_engine, R.raw.asmr_car_engine));
//        categoryIconModelList.add(new CategoryIconModel(R.drawable.ic_catpurring, R.raw.ic_catpurring));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_scratching, "Scratching", R.raw.asmr_scratching, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_tapping, "Tapping", R.raw.asmr_tapping, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_pageturning, "Page Turning", R.raw.asmr_page_turning, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_hairclippers, "Hair Clippers", R.raw.asmr_hair_clippers, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_chewing, "Chewing", R.raw.asmr_chewing, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_whispering, "Whispering", R.raw.asmr_whispering, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_breathing, "Breathing", R.raw.asmr_breathing, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_crackling, "Crackling", R.raw.asmr_crackling, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_engine, "Car Engine", R.raw.asmr_car_engine, false, 5, 50));
        CategoryIconDatabase.getInstance(getApplicationContext()).categoryIconDao()
                .insert(new CategoryIconModel(R.drawable.ic_catpurring, "Cat Purring", R.raw.asmr_cat_purring, false, 5, 50));
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(getApplicationContext(), PlayMusicService.class);
        intent.putExtra("action_music_service", action);

        startService(intent);
    }

    @Override
    public void onBackPressed() {
        isBack = true;
        SharedPreferences sharedPreferences = getSharedPreferences("prox", MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("isRated", false)) {
            ProxRateDialog.showIfNeed(getApplicationContext(), getSupportFragmentManager());
        } else {
            super.onBackPressed();
        }
    }

    private void showRateDialog() {
        ProxRateDialog.Config config = new ProxRateDialog.Config();

        config.setListener(new RatingDialogListener() {
            @Override
            public void onSubmitButtonClicked(int rate, String comment) {
                super.onSubmitButtonClicked(rate, comment);
                Bundle bundle = new Bundle();
                bundle.putString("event_type", "rated");
                bundle.putString("comment", comment);
                bundle.putString("star", rate + " star");
                FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("prox_rating_layout", bundle);
            }

            @Override
            public void onLaterButtonClicked() {
                super.onLaterButtonClicked();
                Bundle bundle = new Bundle();
                bundle.putString("event_type", "cancel");
                FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("prox_rating_layout", bundle);
                if (isBack) {
                    finish();
                }
            }

            @Override
            public void onChangeStar(int rate) {
                super.onChangeStar(rate);
                Bundle bundle = new Bundle();
                bundle.putString("event_type", "rated");
                bundle.putString("star", rate + "star");
                FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("prox_rating_layout", bundle);
                if (rate >= 4) {
                    if (isBack) {
                        finish();
                    }
                }
            }

            @Override
            public void onRated() {
                super.onRated();
            }

            @Override
            public void onDone() {
                super.onDone();
                if (isBack) {
                    finishAndRemoveTask();
                }
            }
        });

        config.setForegroundIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground));
        ProxRateDialog.init(config);
    }
}