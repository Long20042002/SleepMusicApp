package com.prox.appsleep.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "category_icon_model_table")
public class CategoryIconModel implements Serializable {

    @PrimaryKey
    private int image;
    public String nameMusic;
    private int musicResource;
    private int categoryMusic;
    private int volumeMusic;
    private boolean isPlaying;

    public CategoryIconModel(int image, String nameMusic, int musicResource, boolean isPlaying, int categoryMusic, int volumeMusic) {
        this.image = image;
        this.nameMusic = nameMusic;
        this.musicResource = musicResource;
        this.isPlaying = isPlaying;
        this.categoryMusic = categoryMusic;
        this.volumeMusic = volumeMusic;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public int getMusicResource() {
        return musicResource;
    }

    public void setMusicResource(int musicResource) {
        this.musicResource = musicResource;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getCategoryMusic() {
        return categoryMusic;
    }

    public void setCategoryMusic(int categoryMusic) {
        this.categoryMusic = categoryMusic;
    }

    public int getVolumeMusic() {
        return volumeMusic;
    }

    public void setVolumeMusic(int volumeMusic) {
        this.volumeMusic = volumeMusic;
    }
}
