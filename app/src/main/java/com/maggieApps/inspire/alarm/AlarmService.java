package com.jozapps.inspire.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.jozapps.inspire.model.AlarmTime;
import com.jozapps.inspire.util.DateUtils;
import com.jozapps.inspire.util.LocalStorage;

import java.util.Calendar;

public class AlarmService {
    private Context context;
    private PendingIntent mAlarmSender;

    public AlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver
                .class), 0);
    }

    public void startAlarm() {
        AlarmTime alarmTime = new LocalStorage(context).getAlarmTime();
        Calendar start = DateUtils.createCalendarAtTimeToday(0, alarmTime.getMinute(), alarmTime
                .getHour());
        // make sure alarm starts in the future
        Calendar now = DateUtils.createCalendarNow();
        if (start.before(now)) {
            start.add(Calendar.HOUR, 24);
        }
        long firstTime = start.getTimeInMillis();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, firstTime, AlarmManager.INTERVAL_DAY,
                mAlarmSender);
    }
}
