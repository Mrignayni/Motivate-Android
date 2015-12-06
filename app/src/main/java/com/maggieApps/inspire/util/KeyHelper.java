package com.jozapps.inspire.util;

import android.content.Context;
import android.text.TextUtils;

import com.jozapps.inspire.R;

public class KeyHelper {

    public static String getParseApplicationId(Context context) {
        String key = context.getString(R.string.parse_application_id);
        if (TextUtils.isEmpty(key) || key.equals("add_your_own")) {
            throw new RuntimeException("You need to add a Parse Application ID!");
        }
        return key;
    }

    public static String getParseClientKey(Context context) {
        String key = context.getString(R.string.parse_client_key);
        if (TextUtils.isEmpty(key) || key.equals("add_your_own")) {
            throw new RuntimeException("You need to add a Parse Client Key!");
        }
        return key;
    }
}
