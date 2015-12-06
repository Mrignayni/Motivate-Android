package com.jozapps.inspire.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jozapps.inspire.model.AlarmTime;

public class LocalStorage {
    private static final String PREFS_NAME = "InspirePrefs";
    private static final String KEY_HOUR = "HourPref";
    private static final String KEY_MINUTE = "MinutePref";

    private SharedPreferences mSettings;

    public LocalStorage(Context context) {
        mSettings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public AlarmTime getAlarmTime() {
        int hour = mSettings.getInt(KEY_HOUR, 10);
        int minute = mSettings.getInt(KEY_MINUTE, 0);
        return new AlarmTime(hour, minute);
    }

    public void storeAlarmTime(AlarmTime alarmTime) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(KEY_HOUR, alarmTime.getHour());
        editor.putInt(KEY_MINUTE, alarmTime.getMinute());
        editor.commit();
    }
}
