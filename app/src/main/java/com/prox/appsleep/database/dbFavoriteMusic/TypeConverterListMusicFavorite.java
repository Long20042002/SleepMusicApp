package com.prox.appsleep.database.dbFavoriteMusic;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeConverterListMusicFavorite {

   @TypeConverter
    public String fromListFavoriteMusic(List<CategoryIconModel> categoryIconModelList){
       if (categoryIconModelList == null){
           return (null);
       }
       Gson gson = new Gson();
       Type type = new TypeToken<List<CategoryIconModel>>() {}.getType();
       String json = gson.toJson(categoryIconModelList, type);
       return json;
   }

   @TypeConverter
    public List<CategoryIconModel> toListFavoriteMusic(String favoriteMusicString){
       if (favoriteMusicString == null){
           return (null);
       }
       Gson gson = new Gson();
       Type type = new TypeToken<List<CategoryIconModel>>() {}.getType();
       List<CategoryIconModel> countryLangList = gson.fromJson(favoriteMusicString, type);
       return countryLangList;
   }

    @TypeConverter
    public String fromAlarmSound(List<FavoriteMusic> favoriteMusicList){
        if (favoriteMusicList == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FavoriteMusic>>() {}.getType();
        String json = gson.toJson(favoriteMusicList, type);
        return json;
    }

    @TypeConverter
    public List<FavoriteMusic> toAlarmSound(String alarmSoundString){
        if (alarmSoundString == null){
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FavoriteMusic>>() {}.getType();
        List<FavoriteMusic> countryLangList = gson.fromJson(alarmSoundString, type);
        return countryLangList;
    }
}
