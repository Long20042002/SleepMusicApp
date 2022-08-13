package com.prox.appsleep.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {
    @PrimaryKey
    private int broadCastId;
    private int hourAlarm;
    private int minutesAlarm;
    private String nameOfMixAlarm;
    private int timeSnooze;
    private boolean vibration;
    private boolean mon = false;
    private boolean tue = false;
    private boolean wed = false;
    private boolean thu = false;
    private boolean fri = false;
    private boolean sta = false;
    private boolean sun = false;
    private boolean enable = false;
    private List<CategoryIconModel> categoryIconModelList;
    private boolean showCheckBox = false;
    private boolean isCheckBoxChecked = false;

    public Alarm(int broadCastId, int hourAlarm, int minutesAlarm, String nameOfMixAlarm, int timeSnooze, boolean vibration, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sta, boolean sun, boolean enable, List<CategoryIconModel> categoryIconModelList, boolean showCheckBox, boolean isCheckBoxChecked) {

        this.broadCastId = broadCastId;
        this.hourAlarm = hourAlarm;
        this.minutesAlarm = minutesAlarm;
        this.nameOfMixAlarm = nameOfMixAlarm;
        this.timeSnooze = timeSnooze;
        this.vibration = vibration;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sta = sta;
        this.sun = sun;
        this.enable = enable;
        this.categoryIconModelList = categoryIconModelList;
        this.showCheckBox = showCheckBox;
        this.isCheckBoxChecked = isCheckBoxChecked;
    }

    public int getBroadCastId() {
        return broadCastId;
    }

    public void setBroadCastId(int broadCastId) {
        this.broadCastId = broadCastId;
    }

    public int getHourAlarm() {
        return hourAlarm;
    }

    public void setHourAlarm(int hourAlarm) {
        this.hourAlarm = hourAlarm;
    }

    public int getMinutesAlarm() {
        return minutesAlarm;
    }

    public void setMinutesAlarm(int minutesAlarm) {
        this.minutesAlarm = minutesAlarm;
    }

    public String getNameOfMixAlarm() {
        return nameOfMixAlarm;
    }

    public void setNameOfMixAlarm(String nameOfMixAlarm) {
        this.nameOfMixAlarm = nameOfMixAlarm;
    }

    public int getTimeSnooze() {
        return timeSnooze;
    }

    public void setTimeSnooze(int timeSnooze) {
        this.timeSnooze = timeSnooze;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSta() {
        return sta;
    }

    public void setSta(boolean sta) {
        this.sta = sta;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<CategoryIconModel> getCategoryIconModelList() {
        return categoryIconModelList;
    }

    public void setCategoryIconModelList(List<CategoryIconModel> categoryIconModelList) {
        this.categoryIconModelList = categoryIconModelList;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public boolean isCheckBoxChecked() {
        return isCheckBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        isCheckBoxChecked = checkBoxChecked;
    }
}
