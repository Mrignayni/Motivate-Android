package com.jozapps.inspire;

import android.app.Application;

import com.jozapps.inspire.model.Quote;
import com.jozapps.inspire.util.KeyHelper;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class InspireApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Enable Crash Reporting
        if (!BuildConfig.DEBUG) {
            ParseCrashReporting.enable(this);
        }

        ParseObject.registerSubclass(Quote.class);
        Parse.initialize(this, KeyHelper.getParseApplicationId(this), KeyHelper.getParseClientKey
                (this));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
