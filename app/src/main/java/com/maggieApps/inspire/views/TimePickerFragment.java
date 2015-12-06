package com.jozapps.inspire.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.jozapps.inspire.alarm.AlarmService;
import com.jozapps.inspire.metrics.TrackerConstants;
import com.jozapps.inspire.model.AlarmTime;
import com.jozapps.inspire.util.LocalStorage;
import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "timePicker";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlarmTime alarmTime = new LocalStorage(getActivity()).getAlarmTime();
        return new TimePickerDialog(getActivity(), this, alarmTime.getHour(), alarmTime.getMinute(),
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        trackSetNotificationTime(hour, minute);
        new LocalStorage(getActivity()).storeAlarmTime(new AlarmTime(hour, minute));
        new AlarmService(getActivity()).startAlarm();
    }

    private void trackSetNotificationTime(int hour, int minute) {
        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("hour", "" + hour);
        dimensions.put("minute", "" + minute);
        ParseAnalytics.trackEventInBackground(TrackerConstants.SET_NOTIFICATION_TIME_ACTION,
                dimensions);
    }
}
