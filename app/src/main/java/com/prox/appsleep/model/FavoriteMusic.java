package com.prox.appsleep.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "favorite_song_table")
public class FavoriteMusic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameListFavoriteMusic;
    private List<CategoryIconModel> listFavoriteMusic;
    private boolean isPlaying;

    @Ignore
    private boolean isSelected;

    public FavoriteMusic(String nameListFavoriteMusic, List<CategoryIconModel> listFavoriteMusic, boolean isPlaying) {
        this.nameListFavoriteMusic = nameListFavoriteMusic;
        this.listFavoriteMusic = listFavoriteMusic;
        this.isPlaying = isPlaying;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameListFavoriteMusic() {
        return nameListFavoriteMusic;
    }

    public void setNameListFavoriteMusic(String nameListFavoriteMusic) {
        this.nameListFavoriteMusic = nameListFavoriteMusic;
    }

    public List<CategoryIconModel> getListFavoriteMusic() {
        return listFavoriteMusic;
    }

    public void setListFavoriteMusic(List<CategoryIconModel> listFavoriteMusic) {
        this.listFavoriteMusic = listFavoriteMusic;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
