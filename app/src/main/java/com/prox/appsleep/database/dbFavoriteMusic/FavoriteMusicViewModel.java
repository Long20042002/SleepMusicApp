package com.prox.appsleep.database.dbFavoriteMusic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prox.appsleep.model.FavoriteMusic;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteMusicViewModel extends AndroidViewModel {
    private FavoriteMusicDatabase favoriteMusicDatabase;
    public FavoriteMusicViewModel(@NonNull @NotNull Application application) {
        super(application);
        favoriteMusicDatabase = FavoriteMusicDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<FavoriteMusic>> getAllFavoriteMusic(){
        return favoriteMusicDatabase.favoriteMusicDao().getAllFavoriteMusic();
    }

    public LiveData<List<FavoriteMusic>> getSongFavoritePlaying(){
        return favoriteMusicDatabase.favoriteMusicDao().getSongFavoritePlaying();
    }
}
