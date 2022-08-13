package com.prox.appsleep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.itemClick.OnItemClickAlarmSound;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

import java.util.ArrayList;
import java.util.List;

public class AlarmSoundAdapter extends RecyclerView.Adapter<AlarmSoundAdapter.AlarmSoundViewHolder> {

    public List<FavoriteMusic> favoriteMusicList = new ArrayList<>();
    public OnItemClickAlarmSound onItemClickAlarmSound;
    private int currentPosition = -1;

    public AlarmSoundAdapter(OnItemClickAlarmSound onItemClickAlarmSound) {
        this.onItemClickAlarmSound = onItemClickAlarmSound;
    }

    @NonNull
    @Override
    public AlarmSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_sound, parent, false);
        return new AlarmSoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmSoundViewHolder holder, int position) {
        FavoriteMusic favoriteMusic = favoriteMusicList.get(position);

        favoriteMusic.setSelected(DataLocalManger.getPositionAlarmMix() == position);
        holder.tvNameAlarmSound.setText(favoriteMusic.getNameListFavoriteMusic());
        holder.ckbAlarmMix.setChecked(favoriteMusic.isSelected());


        holder.ckbAlarmMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickAlarmSound.ItemClickAlarmSound(favoriteMusic);
                currentPosition = holder.getAdapterPosition();
                DataLocalManger.setPositionAlarmMix(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (favoriteMusicList.size() != 0){
            return favoriteMusicList.size();
        }
        return 0;
    }

    public void setDataAlarmSound(List<FavoriteMusic> favoriteMusics){
        this.favoriteMusicList = favoriteMusics;
        notifyDataSetChanged();
    }

    public class AlarmSoundViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameAlarmSound;
        public ConstraintLayout cstrItemAlarmSound;
        public CheckBox ckbAlarmMix;
        public AlarmSoundViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameAlarmSound = itemView.findViewById(R.id.tvNameAlarmSound);
            cstrItemAlarmSound = itemView.findViewById(R.id.cstrItemAlarmSound);
            ckbAlarmMix = itemView.findViewById(R.id.ckbAlarmMix);
        }
    }
}
