package com.jozapps.inspire.dao;

import com.jozapps.inspire.model.Quote;
import com.jozapps.inspire.util.DateUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class QuoteDAO {

    public interface QuoteListener {
        void onTodayQuoteReceived(Quote quote);
    }

    private static final int MAX_QUOTES_TO_CACHE = 14;
    private List<QuoteListener> listeners = new ArrayList<>();

    public void addListener(QuoteListener listener) {
        listeners.add(listener);
    }

    public void removeListener(QuoteListener listener) {
        listeners.remove(listener);
    }

    public void getUpcoming() {
        final ParseQuery<Quote> query = ParseQuery.getQuery(Quote.class);
        query.whereGreaterThanOrEqualTo(Quote.DISPLAY_DATE, DateUtils.getDateAsGMTMidnight());
        query.setLimit(MAX_QUOTES_TO_CACHE);
        query.findInBackground(new FindCallback<Quote>() {
            @Override
            public void done(List<Quote> quotes, ParseException e) {
                // something went wrong
                if (e != null) {
                    return;
                }

                try {
                    // remove all old data
                    ParseObject.unpinAll();
                    // add updated quotes
                    ParseObject.pinAll(quotes);
                    getQuoteToday();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void getQuoteToday() {
        final ParseQuery<Quote> query = ParseQuery.getQuery(Quote.class);
        query.orderByAscending(Quote.DISPLAY_DATE);
        query.whereGreaterThanOrEqualTo(Quote.DISPLAY_DATE, DateUtils.getDateAsGMTMidnight());
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Quote>() {
            @Override
            public void done(Quote quote, ParseException e) {
                // something went wrong
                if (e != null) {
                    return;
                }

                for (QuoteListener listener : listeners) {
                    listener.onTodayQuoteReceived(quote);
                }
            }
        });
    }

    public void clearOldCache() {
        final ParseQuery<Quote> query = ParseQuery.getQuery(Quote.class);
        query.fromLocalDatastore();
        query.whereLessThan(Quote.DISPLAY_DATE, DateUtils.getDateAsGMTMidnight());
        query.findInBackground(new FindCallback<Quote>() {
            @Override
            public void done(List<Quote> quotes, ParseException e) {
                // something went wrong
                if (e != null) {
                    return;
                }

                try {
                    ParseObject.unpinAll(quotes);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
