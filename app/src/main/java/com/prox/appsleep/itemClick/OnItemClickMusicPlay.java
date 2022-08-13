package com.prox.appsleep.itemClick;

import com.prox.appsleep.model.CategoryIconModel;

public interface OnItemClickMusicPlay {
    void itemMusicPlayDelete(CategoryIconModel categoryIconModel);
    void seekBarCtrlMusicPlay(CategoryIconModel categoryIconModel, int Progress);
}
