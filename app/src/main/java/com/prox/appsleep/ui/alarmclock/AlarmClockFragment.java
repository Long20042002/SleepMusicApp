package com.prox.appsleep.ui.alarmclock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.appsleep.AlarmReceiver;
import com.prox.appsleep.R;
import com.prox.appsleep.adapter.AlarmAdapter;
import com.prox.appsleep.adapter.AlarmSoundAdapter;
import com.prox.appsleep.adapter.DayAdapter;
import com.prox.appsleep.database.dbAlarm.AlarmDatabase;
import com.prox.appsleep.database.dbAlarm.AlarmViewModel;
import com.prox.appsleep.database.dbFavoriteMusic.FavoriteMusicViewModel;
import com.prox.appsleep.databinding.FragmentAlarmClockBinding;
import com.prox.appsleep.itemClick.OnItemClickAlarm;
import com.prox.appsleep.itemClick.OnItemClickAlarmSound;
import com.prox.appsleep.itemClick.OnItemClickDay;
import com.prox.appsleep.itemClick.OnItemClickFavoriteMusic;
import com.prox.appsleep.model.Alarm;
import com.prox.appsleep.model.CategoryIconModel;
import com.prox.appsleep.model.DayAlarm;
import com.prox.appsleep.model.FavoriteMusic;
import com.prox.appsleep.sharedpreferences.DataLocalManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AlarmClockFragment extends Fragment {

    public AlarmClockViewModel alarmClockViewModel;
    private FragmentAlarmClockBinding binding;
    public DayAdapter dayAdapter;
    public AlarmManager alarmManager;
    public PendingIntent pendingIntent;
    public Intent intent;
    public Calendar calendar;
    public AlarmViewModel alarmViewModel;
    public AlarmAdapter alarmAdapter;

    private boolean mon = false;
    private boolean tue = false;
    private boolean wed = false;
    private boolean thu = false;
    private boolean fri = false;
    private boolean sta = false;
    private boolean sun = false;
    private boolean vibration = true;

    private boolean ckb1 = false;
    private boolean ckb3 = false;
    private boolean ckb5 = false;
    private boolean ckb7 = false;
    private boolean ckb10 = false;

    private FavoriteMusicViewModel favoriteMusicViewModel;

    private List<CategoryIconModel> categoryIconModelListAlarm;
    private List<FavoriteMusic> mFavoriteMusicList;
    private FavoriteMusic mFavoriteMusic;

    public int sizeListFV;


    public TextView tvNameOfMixAlarm, tvSnooze;
    public int timeSzoone;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alarmClockViewModel = new ViewModelProvider(this).get(AlarmClockViewModel.class);
        binding = FragmentAlarmClockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        mFavoriteMusicList = new ArrayList<>();

        favoriteMusicViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMusicViewModel.class);
        favoriteMusicViewModel.getAllFavoriteMusic().observe(getViewLifecycleOwner(), new Observer<List<FavoriteMusic>>() {
            @Override
            public void onChanged(List<FavoriteMusic> favoriteMusics) {
                sizeListFV = favoriteMusics.size();
            }
        });

        binding.tvSetAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        binding.lnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        alarmAdapter = new AlarmAdapter(new OnItemClickAlarm() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void ItemClickAlarm(Alarm alarm) {
                alarmItemClick(alarm);
            }

            @Override
            public void ItemClickDeleteAlarm(Alarm alarm) {
                Intent intent = new Intent(requireActivity(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(
                        requireActivity(),
                        alarm.getBroadCastId(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
            }
        });

        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarm().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms.size() > 0) {
                    binding.cstrAlarmNoData.setVisibility(View.GONE);
                    binding.cstrAlarmData.setVisibility(View.VISIBLE);
                    alarmAdapter.setDataAlarm(alarms);
                } else {
                    binding.cstrAlarmData.setVisibility(View.GONE);
                    binding.cstrAlarmNoData.setVisibility(View.VISIBLE);
                }


            }
        });

        binding.rclListAlarm.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rclListAlarm.setAdapter(alarmAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        binding.rclListAlarm.addItemDecoration(itemDecoration);
        alarmClockViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void alarmItemClick(Alarm alarmUpdate) {
        setDefault();
        ckb1 = ckb3 = ckb5 = ckb7 = ckb10 = false;
        timeSzoone = alarmUpdate.getTimeSnooze();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_set_alarm, null);
        builder.setView(viewP);
        Dialog dialogP = builder.create();
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogP.show();

        for (int i = 0; i < mFavoriteMusicList.size(); i++) {
            if (mFavoriteMusicList.get(i).getNameListFavoriteMusic().equalsIgnoreCase(alarmUpdate.getNameOfMixAlarm())) {
                DataLocalManger.setPositionAlarmMix(i);
            }
        }

        /*Dialog set day alarm*/
        List<DayAlarm> dayAlarmList = new ArrayList<>();
        if (alarmUpdate.isMon()) {
            dayAlarmList.add(new DayAlarm("M", R.drawable.custom_shape_set_day_alarm_active));
            mon = true;
        } else {
            dayAlarmList.add(new DayAlarm("M", 0));

        }

        if (alarmUpdate.isTue()) {
            dayAlarmList.add(new DayAlarm("T", R.drawable.custom_shape_set_day_alarm_active));
            tue = true;
        } else {
            dayAlarmList.add(new DayAlarm("T", 0));

        }

        if (alarmUpdate.isWed()) {
            dayAlarmList.add(new DayAlarm("W", R.drawable.custom_shape_set_day_alarm_active));
            wed = true;

        } else {
            dayAlarmList.add(new DayAlarm("W", 0));

        }

        if (alarmUpdate.isThu()) {
            dayAlarmList.add(new DayAlarm("T", R.drawable.custom_shape_set_day_alarm_active));
            thu = true;
        } else {
            dayAlarmList.add(new DayAlarm("T", 0));

        }

        if (alarmUpdate.isFri()) {
            dayAlarmList.add(new DayAlarm("F", R.drawable.custom_shape_set_day_alarm_active));
            fri = true;
        } else {
            dayAlarmList.add(new DayAlarm("F", 0));
        }

        if (alarmUpdate.isSta()) {
            dayAlarmList.add(new DayAlarm("S", R.drawable.custom_shape_set_day_alarm_active));
            sta = true;
        } else {
            dayAlarmList.add(new DayAlarm("S", 0));

        }

        if (alarmUpdate.isSun()) {
            dayAlarmList.add(new DayAlarm("S", R.drawable.custom_shape_set_day_alarm_active));
            sun = true;
        } else {
            dayAlarmList.add(new DayAlarm("S", 0));

        }


        dayAdapter = new DayAdapter(dayAlarmList, new OnItemClickDay() {
            @Override
            public void ItemDayClick(int pos) {
                switch (pos) {
                    case 1:
                        mon = true;
                        break;
                    case 2:
                        tue = true;
                        break;
                    case 3:
                        wed = true;
                        break;
                    case 4:
                        thu = true;
                        break;
                    case 5:
                        fri = true;
                        break;
                    case 6:
                        sta = true;
                        break;
                    case 7:
                        sun = true;
                        break;
                    case -1:
                        mon = false;
                        break;
                    case -2:
                        tue = false;
                        break;
                    case -3:
                        wed = false;
                        break;
                    case -4:
                        thu = false;
                        break;
                    case -5:
                        fri = false;
                        break;
                    case -6:
                        sta = false;
                        break;
                    case -7:
                        sun = false;
                        break;
                }
            }
        });

        RecyclerView rclDayAlarm = viewP.findViewById(R.id.rclDayAlarm);
        rclDayAlarm.setLayoutManager(new LinearLayoutManager(viewP.getContext(), RecyclerView.HORIZONTAL, false));
        rclDayAlarm.setAdapter(dayAdapter);


        /*Set time dialog*/
        NumberPicker npHourAlarm = viewP.findViewById(R.id.npHourAlarm);
        NumberPicker npMinutesAlarm = viewP.findViewById(R.id.npMinutesAlarm);

        npHourAlarm.setMinValue(0);
        npHourAlarm.setMaxValue(24);
        npMinutesAlarm.setMinValue(0);
        npMinutesAlarm.setMaxValue(59);
        npHourAlarm.setTextColor(Color.WHITE);
        npMinutesAlarm.setTextColor(Color.WHITE);
        npHourAlarm.setValue(alarmUpdate.getHourAlarm());
        npMinutesAlarm.setValue(alarmUpdate.getMinutesAlarm());

        npHourAlarm.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        npMinutesAlarm.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });


        /*Dialog remote*/
        ConstraintLayout cstrSetAlarmMix = viewP.findViewById(R.id.cstrSetAlarmMix);
        ConstraintLayout cstrVibration = viewP.findViewById(R.id.cstrVibration);
        ConstraintLayout cstrSetSnooze = viewP.findViewById(R.id.cstrSetSnooze);
        tvNameOfMixAlarm = viewP.findViewById(R.id.tvNameOfMixAlarm);
        tvSnooze = viewP.findViewById(R.id.tvTimeSnooze);

        tvNameOfMixAlarm.setText(alarmUpdate.getNameOfMixAlarm());
        cstrSetAlarmMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmMix(alarmUpdate);
            }
        });

        SwitchCompat swVibration = viewP.findViewById(R.id.swVibration);
        swVibration.setChecked(alarmUpdate.isVibration());
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibration = isChecked;
            }
        });

        tvSnooze.setText(String.valueOf(alarmUpdate.getTimeSnooze() + " Minutes"));
        cstrSetSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSnooze(alarmUpdate);
            }
        });

        /*Dialog dismiss*/
        ImageView ivCloseDialogSetAlarm = viewP.findViewById(R.id.ivCloseDialogSetAlarm);

        ivCloseDialogSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });


        /*Dialog set alarm*/

        TextView tvSetAlarm = viewP.findViewById(R.id.tvSetAlarm);
        TextView tvRemoveAlarm = viewP.findViewById(R.id.tvRemoveAlarm);

        tvSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Calendar timeSetAlarm = Calendar.getInstance();
                timeSetAlarm.set(Calendar.HOUR_OF_DAY, npHourAlarm.getValue());
                timeSetAlarm.set(Calendar.MINUTE, npMinutesAlarm.getValue());
                timeSetAlarm.set(Calendar.SECOND, 0);

                if (timeSetAlarm.getTimeInMillis() <= now.getTimeInMillis()) {
                    timeSetAlarm.add(Calendar.HOUR_OF_DAY, 24);
                }
                Alarm alarm;
                if (categoryIconModelListAlarm != null) {
                    alarm = new Alarm(
                            alarmUpdate.getBroadCastId(),
                            npHourAlarm.getValue(),
                            npMinutesAlarm.getValue(),
                            tvNameOfMixAlarm.getText().toString(),
                            timeSzoone,
                            vibration,
                            mon, tue, wed, thu, fri, sta, sun,
                            true,
                            categoryIconModelListAlarm,
                            false,
                            false
                    );
                }else {
                    alarm = new Alarm(
                            alarmUpdate.getBroadCastId(),
                            npHourAlarm.getValue(),
                            npMinutesAlarm.getValue(),
                            tvNameOfMixAlarm.getText().toString(),
                            timeSzoone,
                            vibration,
                            mon, tue, wed, thu, fri, sta, sun,
                            true,
                            alarmUpdate.getCategoryIconModelList(),
                            false,
                            false
                    );
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarm);
                intent = new Intent(requireActivity(), AlarmReceiver.class);
                intent.putExtra("bundle", bundle);

                pendingIntent = PendingIntent.getBroadcast(requireActivity(),
                        alarmUpdate.getBroadCastId(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeSetAlarm.getTimeInMillis(), pendingIntent);

                AlarmDatabase.getInstance(viewP.getContext()).alarmDao().update(alarm);
                dialogP.dismiss();
            }
        });

        /*Dialog remove alarm*/
        tvRemoveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setAlarm() {
        setDefault();
        ckb1 = ckb3 = ckb5 = ckb7 = ckb10 = false;
        timeSzoone = 5;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_set_alarm, null);
        builder.setView(viewP);
        Dialog dialogP = builder.create();
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogP.show();

        DataLocalManger.setPositionAlarmMix(-1);

        /*Dialog set day alarm*/
        List<DayAlarm> dayAlarmList = new ArrayList<>();
        dayAlarmList.add(new DayAlarm("M", 0));
        dayAlarmList.add(new DayAlarm("T", 0));
        dayAlarmList.add(new DayAlarm("W", 0));
        dayAlarmList.add(new DayAlarm("T", 0));
        dayAlarmList.add(new DayAlarm("F", 0));
        dayAlarmList.add(new DayAlarm("S", 0));
        dayAlarmList.add(new DayAlarm("S", 0));

        dayAdapter = new DayAdapter(dayAlarmList, new OnItemClickDay() {
            @Override
            public void ItemDayClick(int pos) {
                switch (pos) {
                    case 1:
                        mon = true;
                        break;
                    case 2:
                        tue = true;
                        break;
                    case 3:
                        wed = true;
                        break;
                    case 4:
                        thu = true;
                        break;
                    case 5:
                        fri = true;
                        break;
                    case 6:
                        sta = true;
                        break;
                    case 7:
                        sun = true;
                        break;
                    case -1:
                        mon = false;
                        break;
                    case -2:
                        tue = false;
                        break;
                    case -3:
                        wed = false;
                        break;
                    case -4:
                        thu = false;
                        break;
                    case -5:
                        fri = false;
                        break;
                    case -6:
                        sta = false;
                        break;
                    case -7:
                        sun = false;
                        break;
                }
            }
        });

        RecyclerView rclDayAlarm = viewP.findViewById(R.id.rclDayAlarm);
        rclDayAlarm.setLayoutManager(new LinearLayoutManager(viewP.getContext(), RecyclerView.HORIZONTAL, false));
        rclDayAlarm.setAdapter(dayAdapter);


        /*Set time dialog*/
        NumberPicker npHourAlarm = viewP.findViewById(R.id.npHourAlarm);
        NumberPicker npMinutesAlarm = viewP.findViewById(R.id.npMinutesAlarm);

        npHourAlarm.setMinValue(0);
        npHourAlarm.setMaxValue(24);
        npMinutesAlarm.setMinValue(0);
        npMinutesAlarm.setMaxValue(59);
        npHourAlarm.setTextColor(Color.WHITE);
        npMinutesAlarm.setTextColor(Color.WHITE);

        Calendar calendar = Calendar.getInstance();
        npHourAlarm.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npMinutesAlarm.setValue(calendar.get(Calendar.MINUTE));

        npHourAlarm.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        npMinutesAlarm.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });


        /*Dialog remote*/
        ConstraintLayout cstrSetAlarmMix = viewP.findViewById(R.id.cstrSetAlarmMix);
        ConstraintLayout cstrVibration = viewP.findViewById(R.id.cstrVibration);
        ConstraintLayout cstrSetSnooze = viewP.findViewById(R.id.cstrSetSnooze);
        tvNameOfMixAlarm = viewP.findViewById(R.id.tvNameOfMixAlarm);
        tvSnooze = viewP.findViewById(R.id.tvTimeSnooze);

        cstrSetAlarmMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmMix(null);
            }
        });

        SwitchCompat swVibration = viewP.findViewById(R.id.swVibration);
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibration = isChecked;
            }
        });


        cstrSetSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSnooze(null);
            }
        });

        /*Dialog dismiss*/
        ImageView ivCloseDialogSetAlarm = viewP.findViewById(R.id.ivCloseDialogSetAlarm);

        ivCloseDialogSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });

        /*Dialog set alarm*/

        TextView tvSetAlarm = viewP.findViewById(R.id.tvSetAlarm);
        TextView tvRemoveAlarm = viewP.findViewById(R.id.tvRemoveAlarm);

        tvSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                Calendar timeSetAlarm = Calendar.getInstance();
                timeSetAlarm.set(Calendar.HOUR_OF_DAY, npHourAlarm.getValue());
                timeSetAlarm.set(Calendar.MINUTE, npMinutesAlarm.getValue());
                timeSetAlarm.set(Calendar.SECOND, 0);

                if (timeSetAlarm.getTimeInMillis() <= now.getTimeInMillis()) {
                    timeSetAlarm.add(Calendar.HOUR_OF_DAY, 24);
                }

                Alarm alarm = new Alarm(
                        (int) timeSetAlarm.getTimeInMillis(),
                        npHourAlarm.getValue(),
                        npMinutesAlarm.getValue(),
                        tvNameOfMixAlarm.getText().toString(),
                        timeSzoone,
                        vibration,
                        mon, tue, wed, thu, fri, sta, sun,
                        true,
                        categoryIconModelListAlarm,
                        false,
                        false
                );

                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarm);
                intent = new Intent(requireActivity(), AlarmReceiver.class);
                intent.putExtra("bundle", bundle);

                pendingIntent = PendingIntent.getBroadcast(requireActivity(),
                        (int) timeSetAlarm.getTimeInMillis(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeSetAlarm.getTimeInMillis(), pendingIntent);

                Log.e("TAG", "onClick: " + timeSetAlarm.getTimeInMillis());
                AlarmDatabase.getInstance(viewP.getContext()).alarmDao().insert(alarm);
                alarmAdapter.notifyDataSetChanged();
                dialogP.dismiss();
            }
        });

        /*Dialog remove alarm*/
        tvRemoveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });
    }


    private void setSnooze(Alarm alarm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_set_snooze, null);
        builder.setView(viewP);
        Dialog dialogP = builder.create();
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogP.show();

        CheckBox ckb1MSzoone = viewP.findViewById(R.id.ckb1MSzoone);
        CheckBox ckb3MSzoone = viewP.findViewById(R.id.ckb3MSzoone);
        CheckBox ckb5MSzoone = viewP.findViewById(R.id.ckb5MSzoone);
        CheckBox ckb7MSzoone = viewP.findViewById(R.id.ckb7MSzoone);
        CheckBox ckb10MSzoone = viewP.findViewById(R.id.ckb10MSzoone);
        TextView tvDoneSelectSzoone = viewP.findViewById(R.id.tvDoneSelectSzoone);
        TextView tvBackSelectSzoone = viewP.findViewById(R.id.tvBackSelectSzoone);

        ckb1MSzoone.setChecked(ckb1);
        ckb3MSzoone.setChecked(ckb3);
        ckb5MSzoone.setChecked(ckb5);
        ckb7MSzoone.setChecked(ckb7);
        ckb10MSzoone.setChecked(ckb10);

        if (timeSzoone == 5 && alarm == null) {
            ckb5MSzoone.setChecked(true);
        }

        if (alarm != null) {
            if (alarm.getTimeSnooze() == 1) {
                ckb1MSzoone.setChecked(true);
            } else if (alarm.getTimeSnooze() == 3) {
                ckb3MSzoone.setChecked(true);
            } else if (alarm.getTimeSnooze() == 5) {
                ckb5MSzoone.setChecked(true);
            } else if (alarm.getTimeSnooze() == 7) {
                ckb7MSzoone.setChecked(true);
            } else {
                ckb10MSzoone.setChecked(true);
            }
        }

        tvBackSelectSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
                switch (timeSzoone) {
                    case 1:
                        ckb1 = true;
                        break;
                    case 3:
                        ckb3 = true;
                        break;
                    case 5:
                        ckb5 = true;
                        break;
                    case 7:
                        ckb7 = true;
                        break;
                    case 10:
                        ckb10 = true;
                        break;
                }
            }
        });

        ckb1MSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckb3MSzoone.setChecked(false);
                ckb5MSzoone.setChecked(false);
                ckb7MSzoone.setChecked(false);
                ckb10MSzoone.setChecked(false);
                ckb3 = ckb5 = ckb7 = ckb10 = false;
            }
        });

        ckb3MSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckb1MSzoone.setChecked(false);
                ckb5MSzoone.setChecked(false);
                ckb7MSzoone.setChecked(false);
                ckb10MSzoone.setChecked(false);
                ckb1 = ckb5 = ckb7 = ckb10 = false;

            }
        });

        ckb5MSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckb1MSzoone.setChecked(false);
                ckb3MSzoone.setChecked(false);
                ckb7MSzoone.setChecked(false);
                ckb10MSzoone.setChecked(false);
                ckb3 = ckb1 = ckb7 = ckb10 = false;

            }
        });

        ckb7MSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckb1MSzoone.setChecked(false);
                ckb5MSzoone.setChecked(false);
                ckb3MSzoone.setChecked(false);
                ckb10MSzoone.setChecked(false);
                ckb3 = ckb5 = ckb1 = ckb10 = false;

            }
        });

        ckb10MSzoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckb1MSzoone.setChecked(false);
                ckb5MSzoone.setChecked(false);
                ckb7MSzoone.setChecked(false);
                ckb1MSzoone.setChecked(false);
                ckb3 = ckb5 = ckb7 = ckb1 = false;
            }
        });

        tvDoneSelectSzoone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (alarm != null) {
                    if (ckb1MSzoone.isChecked()) {
                        timeSzoone = 1;
                        tvSnooze.setText("1 minutes");
                        alarm.setTimeSnooze(1);
                        AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);
                        ckb1 = true;
                    } else if (ckb3MSzoone.isChecked()) {
                        timeSzoone = 3;
                        tvSnooze.setText("3 minutes");
                        alarm.setTimeSnooze(3);
                        AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);

                        ckb3 = true;
                    } else if (ckb5MSzoone.isChecked()) {
                        timeSzoone = 5;
                        tvSnooze.setText("5 minutes");
                        alarm.setTimeSnooze(5);
                        AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);
                        ckb5 = true;
                    } else if (ckb7MSzoone.isChecked()) {
                        timeSzoone = 7;
                        tvSnooze.setText("7 minutes");
                        alarm.setTimeSnooze(7);
                        AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);

                        ckb7 = true;
                    } else if (ckb10MSzoone.isChecked()) {
                        timeSzoone = 10;
                        tvSnooze.setText("10 minutes");
                        alarm.setTimeSnooze(10);
                        AlarmDatabase.getInstance(v.getContext()).alarmDao().update(alarm);
                        ckb10 = true;
                    }
                } else {
                    if (ckb1MSzoone.isChecked()) {
                        timeSzoone = 1;
                        tvSnooze.setText("1 minutes");
                        ckb1 = true;
                    } else if (ckb3MSzoone.isChecked()) {
                        timeSzoone = 3;
                        tvSnooze.setText("3 minutes");
                        ckb3 = true;
                    } else if (ckb5MSzoone.isChecked()) {
                        timeSzoone = 5;
                        tvSnooze.setText("5 minutes");
                        ckb5 = true;
                    } else if (ckb7MSzoone.isChecked()) {
                        timeSzoone = 7;
                        tvSnooze.setText("7 minutes");
                        ckb7 = true;
                    } else if (ckb10MSzoone.isChecked()) {
                        timeSzoone = 10;
                        tvSnooze.setText("10 minutes");
                        ckb10 = true;
                    }
                }


                dialogP.dismiss();

            }

        });

    }


    private void setAlarmMix(Alarm alarm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View viewP = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_set_alarm_sound, null);
        builder.setView(viewP);
        Dialog dialogP = builder.create();
        dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogP.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


        TextView tvDoneSelectAlarm = viewP.findViewById(R.id.tvDoneSelectAlarm);
        TextView tvBackSelectAlarm = viewP.findViewById(R.id.tvBackSelectAlarm);
        if (tvNameOfMixAlarm.getText().toString().equalsIgnoreCase("My Alarm Mix")) {
            DataLocalManger.setPositionAlarmMix(-1);
        }

        AlarmSoundAdapter adapter = new AlarmSoundAdapter(new OnItemClickAlarmSound() {
            @Override
            public void ItemClickAlarmSound(FavoriteMusic favoriteMusic) {
                mFavoriteMusic = favoriteMusic;
                categoryIconModelListAlarm = favoriteMusic.getListFavoriteMusic();
            }
        });

        FavoriteMusicViewModel favoriteMusicViewModel = new ViewModelProvider(requireActivity()).get(FavoriteMusicViewModel.class);
        favoriteMusicViewModel.getAllFavoriteMusic().observe(getViewLifecycleOwner(), new Observer<List<FavoriteMusic>>() {
            @Override
            public void onChanged(List<FavoriteMusic> favoriteMusicList) {
                mFavoriteMusicList = favoriteMusicList;
                adapter.setDataAlarmSound(favoriteMusicList);
            }
        });

        if (sizeListFV > 0) {
            dialogP.show();
        } else {
            Toast.makeText(viewP.getContext(), "Please add your favorite sound list first!", Toast.LENGTH_SHORT).show();
        }

        RecyclerView rcvSelectAlarmMix = viewP.findViewById(R.id.rcvSelectAlarmMix);
        rcvSelectAlarmMix.setLayoutManager(new LinearLayoutManager(viewP.getContext(), RecyclerView.VERTICAL, false));
        rcvSelectAlarmMix.setAdapter(adapter);

        tvDoneSelectAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvNameOfMixAlarm.setText(mFavoriteMusic.getNameListFavoriteMusic());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogP.dismiss();

            }
        });

        tvBackSelectAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogP.dismiss();
            }
        });
    }


    private void setDefault() {
        mon = tue = wed = thu = fri = sta = sun = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}