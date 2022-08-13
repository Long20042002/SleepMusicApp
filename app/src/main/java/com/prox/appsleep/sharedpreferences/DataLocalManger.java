package com.prox.appsleep.sharedpreferences;

import android.content.Context;

public class DataLocalManger {

    private static final String PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL";
    private static final String PREF_ISPLAYING = "PREF_ISPLAYING";
    private static final String PREF_COUNT_MUSIC_PLAYING = "PREF_COUNT_MUSIC_PLAYING";
    private static final String PREF_POSITION_MIX_ALARM = "PREF_POSITION_MIX_ALARM";
    private static final String PREF_COUNT_OPEN_APP = "PREF_COUNT_OPEN_APP";


    private static DataLocalManger instance;
    private SharedPreferencesStatusMedia sharedPreferencesStatusMedia;

    public static void init(Context context){
        instance = new DataLocalManger();
        instance.sharedPreferencesStatusMedia = new SharedPreferencesStatusMedia(context);
    }

    public static DataLocalManger getInstance(){
        if (instance == null){
            instance = new DataLocalManger();
        }
        return instance;
    }

    public static void setFirstInstalled(boolean isFirst){
        DataLocalManger.getInstance().sharedPreferencesStatusMedia.putBooleanValue(PREF_FIRST_INSTALL,isFirst);
    }

    public static boolean getFirstInstalled(){
        return DataLocalManger.getInstance().sharedPreferencesStatusMedia.getBooleanValue(PREF_FIRST_INSTALL);
    }

    public static void setStatusMedia(boolean isPlaying){
        DataLocalManger.getInstance().sharedPreferencesStatusMedia.putBooleanValue(PREF_ISPLAYING, isPlaying);
    }

    public static boolean getStatusMedia(){
        return DataLocalManger.getInstance().sharedPreferencesStatusMedia.getBooleanValue(PREF_ISPLAYING);
    }

    public static void setCountMusicPlaying(int count){
        DataLocalManger.getInstance().sharedPreferencesStatusMedia.putIntValue(PREF_COUNT_MUSIC_PLAYING, count);
    }

    public static int getCountMusicPlaying(){
        return DataLocalManger.getInstance().sharedPreferencesStatusMedia.getIntValue(PREF_COUNT_MUSIC_PLAYING);
    }

    public static void setPositionAlarmMix(int position){
        DataLocalManger.getInstance().sharedPreferencesStatusMedia.putIntValue(PREF_POSITION_MIX_ALARM, position);
    }

    public static int getPositionAlarmMix(){
        return DataLocalManger.getInstance().sharedPreferencesStatusMedia.getIntValue(PREF_POSITION_MIX_ALARM);
    }

    public static void setCountOpenApp(int count){
        DataLocalManger.getInstance().sharedPreferencesStatusMedia.putIntValue(PREF_COUNT_OPEN_APP, count);
    }

    public static int getCountOpenApp(){
        return DataLocalManger.getInstance().sharedPreferencesStatusMedia.getIntValue(PREF_COUNT_OPEN_APP);
    }
}
