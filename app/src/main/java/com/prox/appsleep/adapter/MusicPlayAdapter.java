package com.prox.appsleep.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.itemClick.OnItemClickListener;
import com.prox.appsleep.itemClick.OnItemClickMusicPlay;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayAdapter extends RecyclerView.Adapter<MusicPlayAdapter.MusicPlayViewHolder> {

    private List<CategoryIconModel> categoryIconModelList = new ArrayList<>();
    public OnItemClickMusicPlay onItemClickMusicPlay;

    public MusicPlayAdapter(List<CategoryIconModel> categoryIconModelList, OnItemClickMusicPlay listener) {
        this.categoryIconModelList = categoryIconModelList;
        this.onItemClickMusicPlay = listener;
    }

    @NonNull
    @NotNull
    @Override
    public MusicPlayViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_play, parent, false);
        return new MusicPlayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MusicPlayViewHolder holder, int position) {
        CategoryIconModel categoryIconModel = categoryIconModelList.get(position);
        holder.ivItemPlayMusic.setImageResource(categoryIconModel.getImage());
        holder.ivDeleteItemPlayMusic.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                onItemClickMusicPlay.itemMusicPlayDelete(categoryIconModel);
                int count = DataLocalManger.getCountMusicPlaying();
                count--;
                DataLocalManger.setCountMusicPlaying(count);
                notifyDataSetChanged();
            }
        });

        holder.sbVolumePlayMusic.setMax(99);
        holder.sbVolumePlayMusic.setProgress(categoryIconModel.getVolumeMusic());
        holder.sbVolumePlayMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onItemClickMusicPlay.seekBarCtrlMusicPlay(categoryIconModel, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                categoryIconModel.setVolumeMusic(seekBar.getProgress());
                CategoryIconDatabase.getInstance(seekBar.getContext()).categoryIconDao().update(categoryIconModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryIconModelList != null) {
            return categoryIconModelList.size();
        }
        return 0;
    }

    public void setDataMusicPlay(List<CategoryIconModel> categoryIconModels){
        this.categoryIconModelList = categoryIconModels;
        notifyDataSetChanged();
    }

    public class MusicPlayViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout lnItemPlayMusic;
        public ImageView ivItemPlayMusic, ivDeleteItemPlayMusic;
        public SeekBar sbVolumePlayMusic;
        public MusicPlayViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            lnItemPlayMusic = itemView.findViewById(R.id.lnItemPlayMusic);
            ivItemPlayMusic = itemView.findViewById(R.id.ivItemPlayMusic);
            ivDeleteItemPlayMusic = itemView.findViewById(R.id.ivDeleteItemPlayMusic);
            sbVolumePlayMusic = itemView.findViewById(R.id.sbVolumePlayMusic);
        }
    }
}
