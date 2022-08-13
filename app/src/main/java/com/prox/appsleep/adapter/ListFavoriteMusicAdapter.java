package com.prox.appsleep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.model.CategoryIconModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListFavoriteMusicAdapter extends RecyclerView.Adapter<ListFavoriteMusicAdapter.ListFavoriteMusicViewHolder> {

    List<CategoryIconModel> categoryIconModelList;

    public ListFavoriteMusicAdapter(List<CategoryIconModel> categoryIconModelList) {
        this.categoryIconModelList = categoryIconModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ListFavoriteMusicViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_favorite, parent, false);
        return new ListFavoriteMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListFavoriteMusicViewHolder holder, int position) {
        CategoryIconModel categoryIconModel = categoryIconModelList.get(position);
        holder.ivFavoriteMusic.setImageResource(categoryIconModel.getImage());
    }

    @Override
    public int getItemCount() {
        if (categoryIconModelList != null) {
            return categoryIconModelList.size();
        }
        return 0;
    }

    public static class ListFavoriteMusicViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFavoriteMusic;
        public ListFavoriteMusicViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivFavoriteMusic = itemView.findViewById(R.id.ivFavoriteMusic);
        }
    }
}
