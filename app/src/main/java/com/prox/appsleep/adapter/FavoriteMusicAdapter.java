package com.prox.appsleep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.itemClick.OnItemClickFavoriteMusic;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.FavoriteMusic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMusicAdapter extends RecyclerView.Adapter<FavoriteMusicAdapter.FavoriteMusicViewHolder> {

    private List<FavoriteMusic> favoriteMusicList = new ArrayList<>();
    public Context context;
    public OnItemClickFavoriteMusic onItemClickFavoriteMusic;
    public List<CategoryIconModel> categoryIconModelList = new ArrayList<>();

    public FavoriteMusicAdapter(Context context, OnItemClickFavoriteMusic itemClickFavoriteMusic) {
        this.context = context;
        this.onItemClickFavoriteMusic = itemClickFavoriteMusic;
    }

    @NonNull
    @NotNull
    @Override
    public FavoriteMusicViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoriteMusicViewHolder holder, int position) {
        FavoriteMusic favoriteMusic = favoriteMusicList.get(position);
        holder.tvNameListMix.setText(favoriteMusic.getNameListFavoriteMusic());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        holder.rcvFavoriteListMix.setLayoutManager(linearLayoutManager);

        categoryIconModelList = favoriteMusic.getListFavoriteMusic();
        ListFavoriteMusicAdapter listFavoriteMusicAdapter = new ListFavoriteMusicAdapter(categoryIconModelList);
        holder.rcvFavoriteListMix.setAdapter(listFavoriteMusicAdapter);

        if (favoriteMusic.isPlaying()) {
            holder.ivPlayOrPauseFavoriteMusic.setImageResource(R.drawable.ic_pause);
        } else {
            holder.ivPlayOrPauseFavoriteMusic.setImageResource(R.drawable.ic_play);
        }

        holder.ivPlayOrPauseFavoriteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteMusicDatabase.getInstance(v.getContext()).favoriteMusicDao().setIsPlayingFalse();
                if (favoriteMusic.isPlaying()) {
                    holder.ivPlayOrPauseFavoriteMusic.setImageResource(R.drawable.ic_play);
                    favoriteMusic.setPlaying(false);
                } else {
                    holder.ivPlayOrPauseFavoriteMusic.setImageResource(R.drawable.ic_pause);
                    favoriteMusic.setPlaying(true);
                }
                FavoriteMusicDatabase.getInstance(v.getContext()).favoriteMusicDao().update(favoriteMusic);
                onItemClickFavoriteMusic.ItemClickPlayFavoriteMusicList(favoriteMusic);
            }
        });

        holder.ivEditFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteMusicDatabase.getInstance(v.getContext()).favoriteMusicDao().setIsPlayingFalse();
                if (!favoriteMusic.isPlaying()) {
                    holder.ivPlayOrPauseFavoriteMusic.setImageResource(R.drawable.ic_pause);
                    favoriteMusic.setPlaying(true);
                }
                FavoriteMusicDatabase.getInstance(v.getContext()).favoriteMusicDao().update(favoriteMusic);
                onItemClickFavoriteMusic.ItemClickUpdateFavoriteMusicList(favoriteMusic);
            }
        });

        holder.ivDeleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickFavoriteMusic.ItemClickDeleteFavoriteMusicList(favoriteMusic);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (favoriteMusicList != null) {
            return favoriteMusicList.size();
        }
        return 0;
    }

    public void setFavoriteMusics(List<FavoriteMusic> favoriteMusics) {
        this.favoriteMusicList = favoriteMusics;
        notifyDataSetChanged();
    }

    public static class FavoriteMusicViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameListMix;
        public RecyclerView rcvFavoriteListMix;
        public ImageView ivEditFavorite, ivDeleteFavorite, ivPlayOrPauseFavoriteMusic;

        public FavoriteMusicViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNameListMix = itemView.findViewById(R.id.tvNameListMix);
            rcvFavoriteListMix = itemView.findViewById(R.id.rcvFavoriteListMix);
            ivDeleteFavorite = itemView.findViewById(R.id.ivDeleteFavorite);
            ivEditFavorite = itemView.findViewById(R.id.ivEditFavorite);
            ivPlayOrPauseFavoriteMusic = itemView.findViewById(R.id.ivPlayFavorite);
        }
    }
}
