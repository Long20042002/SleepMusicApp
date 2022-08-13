package com.prox.appsleep.ui.alarmclock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmClockViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlarmClockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Coming soon");
    }

    public LiveData<String> getText() {
        return mText;
    }
}