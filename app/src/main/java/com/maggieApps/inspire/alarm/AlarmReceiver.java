package com.jozapps.inspire.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jozapps.inspire.R;
import com.jozapps.inspire.dao.QuoteDAO;
import com.jozapps.inspire.model.Quote;
import com.jozapps.inspire.util.ColorUtils;
import com.jozapps.inspire.views.MainActivity;

public class AlarmReceiver extends BroadcastReceiver implements QuoteDAO.QuoteListener {

    NotificationManager mNotificationManager;
    private QuoteDAO mQuoteDAO = new QuoteDAO();
    private Context mContext;

    @Override
    public void onReceive(Context pContext, Intent pIntent) {
        mContext = pContext;
        mQuoteDAO.addListener(this);
        mQuoteDAO.getQuoteToday();
        mQuoteDAO.getUpcoming();
        mQuoteDAO.clearOldCache();
        mNotificationManager =
                (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onTodayQuoteReceived(Quote quote) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, MainActivity
                        .newIntentForNotification(mContext),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // get dark color for notification icon
        int notifColor;
        if (ColorUtils.isColorDark(ColorUtils.getTodayColor(mContext))) {
            notifColor = ColorUtils.getTodayColor(mContext);
        } else {
            notifColor = ColorUtils.getTodayAccentColor(mContext);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(quote.getText()))
                .setSmallIcon(R.drawable.ic_notif).setContentTitle(String.format(mContext
                        .getResources().getString(R.string.notification_title), quote
                        .getAuthor()))
                .setContentText(quote.getText())
                .setColor(notifColor)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(0, notificationBuilder.build());
        mQuoteDAO.removeListener(this);
    }
}
