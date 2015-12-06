package com.jozapps.inspire;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jozapps.inspire.alarm.AlarmService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmService(context).startAlarm();
        }
    }
}
