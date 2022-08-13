package com.prox.appsleep.itemClick;

import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;

public interface OnItemClickFavoriteMusic {
    void ItemClickPlayFavoriteMusicList(FavoriteMusic favoriteMusic);
    void ItemClickDeleteFavoriteMusicList(FavoriteMusic favoriteMusic);
    void ItemClickUpdateFavoriteMusicList(FavoriteMusic favoriteMusic);
}
