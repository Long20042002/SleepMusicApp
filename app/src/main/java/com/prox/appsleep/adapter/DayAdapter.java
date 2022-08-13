package com.prox.appsleep.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.R;
import com.prox.appsleep.itemClick.OnItemClickDay;
import com.prox.appsleep.model.DayAlarm;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    public List<DayAlarm> dayAlarmList = new ArrayList<>();
    public OnItemClickDay onItemClickDay;

    public DayAdapter(List<DayAlarm> dayAlarmList, OnItemClickDay onItemClickDay) {
        this.dayAlarmList = dayAlarmList;
        this.onItemClickDay = onItemClickDay;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_alarm, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayAlarm dayAlarm = dayAlarmList.get(position);
        holder.tvNameDay.setText(dayAlarm.getDay());
        holder.tvNameDay.setBackgroundResource(dayAlarm.getResourceDay());



        holder.cstrItemAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvNameDay.getBackground() == null){
                    holder.tvNameDay.setBackgroundResource(R.drawable.custom_shape_set_day_alarm_active);
                    holder.tvNameDay.setTextColor(Color.BLACK);
                    onItemClickDay.ItemDayClick(holder.getAdapterPosition()+1);
                }else {
                    holder.tvNameDay.setBackground(null);
                    holder.tvNameDay.setTextColor(Color.parseColor("#425180"));
                    onItemClickDay.ItemDayClick(-(holder.getAdapterPosition()+1));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dayAlarmList != null) {
            return dayAlarmList.size();
        }
        return 0;
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cstrItemAlarm;
        TextView tvNameDay;
        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            cstrItemAlarm = itemView.findViewById(R.id.cstrItemAlarm);
            tvNameDay = itemView.findViewById(R.id.tvNameDay);
        }
    }
}
