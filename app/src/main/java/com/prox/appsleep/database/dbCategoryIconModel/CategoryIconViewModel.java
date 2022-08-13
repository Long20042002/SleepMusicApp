package com.prox.appsleep.database.dbCategoryIconModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryIconViewModel extends AndroidViewModel {
    private CategoryIconDatabase categoryIconDatabase;
    public CategoryIconViewModel(@NonNull @NotNull Application application) {
        super(application);
        categoryIconDatabase = CategoryIconDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<CategoryIconModel>> getAllCategoryIcon(){
        return categoryIconDatabase.categoryIconDao().getAllCategoryIconModel();
    }

//    public LiveData<List<CategoryIconModel>> getCategoryIconModelPlaying(){
//        return categoryIconDatabase.categoryIconDao().getCategoryIconModelPlaying();
//    }

    public LiveData<List<CategoryIconModel>> getAllCategoryIconTrue(){
        return categoryIconDatabase.categoryIconDao().getAllCategoryIconModelTrue();
    }

    public LiveData<List<CategoryIconModel>> getCategoryIconRain(){
        return categoryIconDatabase.categoryIconDao().getCategoryIconModelRain();
    }

    public LiveData<List<CategoryIconModel>> getCategoryIconForest(){
        return categoryIconDatabase.categoryIconDao().getCategoryIconModelForest();
    }

    public LiveData<List<CategoryIconModel>> getCategoryIconCity(){
        return categoryIconDatabase.categoryIconDao().getCategoryIconModelCity();
    }

    public LiveData<List<CategoryIconModel>> getCategoryIconRelax(){
        return categoryIconDatabase.categoryIconDao().getCategoryIconModelRelax();
    }

    public LiveData<List<CategoryIconModel>> getCategoryIconASMR(){
        return categoryIconDatabase.categoryIconDao().getCategoryIconModelASMR();
    }
}
