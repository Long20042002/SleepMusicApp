package com.prox.appsleep.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.prox.appsleep.R;
import com.prox.appsleep.database.dbAlarm.AlarmDatabase;
import com.prox.appsleep.itemClick.OnItemClickAlarm;
import com.prox.appsleep.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    public OnItemClickAlarm onItemClickAlarm;
    public List<Alarm> alarms;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public AlarmAdapter(OnItemClickAlarm onItemClickAlarm) {
        this.onItemClickAlarm = onItemClickAlarm;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);

        Log.d("TAG", "onBindViewHolder:  "+ alarm.isEnable());

        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(alarm.getBroadCastId()));

        String hourAlarm = String.format("%02d", alarm.getHourAlarm());
        String minutesAlarm = String.format("%02d", alarm.getMinutesAlarm());

        holder.tvTimeAlarm.setText(hourAlarm + ":" + minutesAlarm);
        holder.tvNameMusicAlarm.setText(alarm.getNameOfMixAlarm());

        holder.swOnOffAlarm.setChecked(alarm.isEnable());
        holder.swOnOffAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setEnable(holder.swOnOffAlarm.isChecked());
                AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);
            }
        });

        holder.swOnOffAlarm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });

        if (!holder.swOnOffAlarm.isChecked()) {
            if (alarm.isMon()) {
                holder.tvMonday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivMonday.setVisibility(View.VISIBLE);
            } else {
                holder.tvMonday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivMonday.setVisibility(View.GONE);
            }

            if (alarm.isTue()) {
                holder.tvTuesday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivTuesday.setVisibility(View.VISIBLE);
            } else {
                holder.tvTuesday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivTuesday.setVisibility(View.GONE);
            }

            if (alarm.isWed()) {
                holder.tvWednesday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivWednesday.setVisibility(View.VISIBLE);
            } else {
                holder.tvWednesday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivWednesday.setVisibility(View.GONE);
            }

            if (alarm.isThu()) {
                holder.tvThursday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivThursday.setVisibility(View.VISIBLE);
            } else {
                holder.tvThursday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivThursday.setVisibility(View.GONE);
            }

            if (alarm.isFri()) {
                holder.tvFriday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivFriday.setVisibility(View.VISIBLE);
            } else {
                holder.tvFriday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivFriday.setVisibility(View.GONE);
            }

            if (alarm.isSta()) {
                holder.tvSaturday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivSaturday.setVisibility(View.VISIBLE);
            } else {
                holder.tvSaturday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivSaturday.setVisibility(View.GONE);
            }

            if (alarm.isSun()) {
                holder.tvSunday.setTextColor(Color.parseColor("#7f89a8"));
                holder.ivSunday.setVisibility(View.VISIBLE);
            } else {
                holder.tvSunday.setTextColor(Color.parseColor("#716FD5"));
                holder.ivSunday.setVisibility(View.GONE);
            }
        } else {
            if (alarm.isMon()) {
                holder.tvMonday.setTextColor(Color.WHITE);
                holder.ivMonday.setVisibility(View.VISIBLE);
            } else {
                holder.tvMonday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivMonday.setVisibility(View.GONE);
            }

            if (alarm.isTue()) {
                holder.tvTuesday.setTextColor(Color.WHITE);
                holder.ivTuesday.setVisibility(View.VISIBLE);
            } else {
                holder.tvTuesday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivTuesday.setVisibility(View.GONE);
            }

            if (alarm.isWed()) {
                holder.tvWednesday.setTextColor(Color.WHITE);
                holder.ivWednesday.setVisibility(View.VISIBLE);
            } else {
                holder.tvWednesday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivWednesday.setVisibility(View.GONE);
            }

            if (alarm.isThu()) {
                holder.tvThursday.setTextColor(Color.WHITE);
                holder.ivThursday.setVisibility(View.VISIBLE);
            } else {
                holder.tvThursday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivThursday.setVisibility(View.GONE);
            }

            if (alarm.isFri()) {
                holder.tvFriday.setTextColor(Color.WHITE);
                holder.ivFriday.setVisibility(View.VISIBLE);
            } else {
                holder.tvFriday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivFriday.setVisibility(View.GONE);
            }

            if (alarm.isSta()) {
                holder.tvSaturday.setTextColor(Color.WHITE);
                holder.ivSaturday.setVisibility(View.VISIBLE);
            } else {
                holder.tvSaturday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivSaturday.setVisibility(View.GONE);
            }

            if (alarm.isSun()) {
                holder.tvSunday.setTextColor(Color.WHITE);
                holder.ivSunday.setVisibility(View.VISIBLE);
            } else {
                holder.tvSunday.setTextColor(Color.parseColor("#FFA88A"));
                holder.ivSunday.setVisibility(View.GONE);
            }
        }

        if (alarm.isEnable()) {
            holder.cstrItemAlarmLayot.setBackgroundResource(R.drawable.custom_shape_of_list_alarm);
        } else {
            holder.cstrItemAlarmLayot.setBackgroundResource(R.drawable.custom_shape_of_list_alarm_off);
        }

        holder.cstrItemAlarmLayot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickAlarm.ItemClickAlarm(alarm);
            }
        });

        holder.layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickAlarm.ItemClickDeleteAlarm(alarm);
                AlarmDatabase.getInstance(v.getContext()).alarmDao().delete(alarm);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (alarms != null) {
            return alarms.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataAlarm(List<Alarm> alarmList) {
        this.alarms = alarmList;
        notifyDataSetChanged();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTimeAlarm, tvNameMusicAlarm, tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;
        public ImageView ivMonday, ivTuesday, ivWednesday, ivThursday, ivFriday, ivSaturday, ivSunday;
        public SwitchCompat swOnOffAlarm;
        public ConstraintLayout cstrItemAlarmLayot;
        public LinearLayout bottom_wrapper, layout_item, layout_delete;
        private SwipeRevealLayout swipeRevealLayout;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeAlarm = itemView.findViewById(R.id.tvTimeAlarm);
            tvNameMusicAlarm = itemView.findViewById(R.id.tvNameMusicAlarm);

            tvMonday = itemView.findViewById(R.id.tvMonday);
            tvTuesday = itemView.findViewById(R.id.tvTuesday);
            tvWednesday = itemView.findViewById(R.id.tvWednesday);
            tvThursday = itemView.findViewById(R.id.tvThursday);
            tvFriday = itemView.findViewById(R.id.tvFriday);
            tvSaturday = itemView.findViewById(R.id.tvSaturday);
            tvSunday = itemView.findViewById(R.id.tvSunday);

            ivMonday = itemView.findViewById(R.id.ivMonday);
            ivTuesday = itemView.findViewById(R.id.ivTuesday);
            ivWednesday = itemView.findViewById(R.id.ivWednesday);
            ivThursday = itemView.findViewById(R.id.ivThursday);
            ivFriday = itemView.findViewById(R.id.ivFriday);
            ivSaturday = itemView.findViewById(R.id.ivSarturday);
            ivSunday = itemView.findViewById(R.id.ivSunday);

            swOnOffAlarm = itemView.findViewById(R.id.swOnOffAlarm);

            cstrItemAlarmLayot = itemView.findViewById(R.id.cstrItemAlarmLayot);

            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            layout_delete = itemView.findViewById(R.id.lnDeleteAlarm);

        }
    }
}
