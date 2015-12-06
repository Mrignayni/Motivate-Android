package com.jozapps.inspire.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.jozapps.inspire.R;
import com.jozapps.inspire.metrics.TrackerConstants;
import com.parse.ParseAnalytics;


public class MainActivity extends ActionBarActivity {

    private static final String ARG_NOTIF = "isFromNotification";

    public static Intent newIntentForNotification(Context context) {
        Intent intent = new Intent
                (context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ARG_NOTIF, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackNotificationClick();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commit();
        }
    }

    private void trackNotificationClick() {
        if (getIntent().hasExtra(ARG_NOTIF)) {
            if (getIntent().getBooleanExtra(ARG_NOTIF, false)) {
                ParseAnalytics.trackEventInBackground(TrackerConstants.NOTIFICATION_ACTION);
            }
        }
    }

}
