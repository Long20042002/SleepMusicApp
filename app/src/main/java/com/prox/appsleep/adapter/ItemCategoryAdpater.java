package com.prox.appsleep.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.database.dbCategoryIconModel.CategoryIconDatabase;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicDatabase;
import com.prox.appsleep.itemClick.OnItemClickListener;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemCategoryAdpater extends RecyclerView.Adapter<ItemCategoryAdpater.ItemCategoryViewHolder> {

    private List<CategoryIconModel> categoryIconModelList;
    public OnItemClickListener onItemClickListener;
    public int count;

    public ItemCategoryAdpater(List<CategoryIconModel> categoryIconModelList, OnItemClickListener listener) {
        this.categoryIconModelList = categoryIconModelList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ItemCategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ItemCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemCategoryViewHolder holder, int position) {
        CategoryIconModel categoryIconModel = categoryIconModelList.get(position);
        holder.ivCategory.setImageResource(categoryIconModel.getImage());
        holder.tvNameMusic.setText(categoryIconModel.getNameMusic());

        if (categoryIconModel.isPlaying()) {
            holder.lnBG.setBackgroundResource(R.drawable.custom_shape_icon_category_music_play);
        } else {
            holder.lnBG.setBackgroundResource(R.drawable.custom_shape_icon_category_music);
        }
        count = DataLocalManger.getCountMusicPlaying();

        holder.lnBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categoryIconModel.isPlaying()) {
                    holder.lnBG.setBackgroundResource(R.drawable.custom_shape_icon_category_music);
                    categoryIconModel.setPlaying(false);
                    count--;
                } else {
                    if (DataLocalManger.getCountMusicPlaying() > 7) {
                        Toast.makeText(view.getContext(), "Up to 8 sounds", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holder.lnBG.setBackgroundResource(R.drawable.custom_shape_icon_category_music_play);
                    categoryIconModel.setPlaying(true);
                    count++;
                }
                DataLocalManger.setCountMusicPlaying(count);
                CategoryIconDatabase.getInstance(view.getContext()).categoryIconDao().update(categoryIconModel);
                FavoriteMusicDatabase.getInstance(view.getContext()).favoriteMusicDao().setIsPlayingFalse();
                onItemClickListener.itemClickListener(categoryIconModel);


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

    public void setCategoryIconModel(List<CategoryIconModel> categoryIconModels) {
        this.categoryIconModelList = categoryIconModels;
        notifyDataSetChanged();
    }

    public static class ItemCategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCategory;
        public TextView tvNameMusic;
        public LinearLayout lnBG;

        public ItemCategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            lnBG = itemView.findViewById(R.id.bgCategory);
            tvNameMusic = itemView.findViewById(R.id.tvNameMusic);
        }
    }
}
