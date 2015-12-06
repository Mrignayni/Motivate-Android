package com.jozapps.inspire.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jozapps.inspire.R;
import com.jozapps.inspire.alarm.AlarmService;
import com.jozapps.inspire.dao.QuoteDAO;
import com.jozapps.inspire.metrics.TrackerConstants;
import com.jozapps.inspire.model.Quote;
import com.jozapps.inspire.util.ColorUtils;
import com.parse.ParseAnalytics;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainFragment extends Fragment implements QuoteDAO.QuoteListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.main_background)
    RelativeLayout mainLayout;
    @InjectView(R.id.main_quote)
    TextView titleTextView;
    @InjectView(R.id.main_author)
    TextView descriptionTextView;
    @InjectView(R.id.main_share_button)
    FloatingActionButton shareButton;

    private QuoteDAO mQuoteDAO = new QuoteDAO();
    private Quote mQuote;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuoteDAO.addListener(this);
        new AlarmService(getActivity()).startAlarm();
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());
        mQuoteDAO.getQuoteToday();
        mQuoteDAO.getUpcoming();
        mQuoteDAO.clearOldCache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDAO.removeListener(this);
    }

    private void initViews() {
        toolbar.inflateMenu(R.menu.menu_alarm);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_alarm:
                        DialogFragment newFragment = new TimePickerFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(),
                                TimePickerFragment.TAG);
                        break;
                }
                return true;
            }
        });
    }


    private void updateViews() {
        if (mQuote != null) {
            titleTextView.setText(mQuote.getText());
            descriptionTextView.setText(mQuote.getAuthor());
            int colorRes = ColorUtils.getTodayColor(getActivity());
            int accentColor = ColorUtils.getTodayAccentColor(getActivity());
            int whiteBlackColor = ColorUtils.getWhiteBlackAccent(getActivity(), colorRes);
            mainLayout.setBackgroundColor(colorRes);
            titleTextView.setTextColor(whiteBlackColor);
            descriptionTextView.setTextColor(whiteBlackColor);
            shareButton.setBackgroundTintList(ColorUtils.getTintListForColor(accentColor));
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareQuote(mQuote);
                }
            });
            Drawable drawable = shareButton.getDrawable();
            DrawableCompat.setTint(drawable, ColorUtils.getWhiteBlackAccent(getActivity(),
                    accentColor));
            shareButton.setImageDrawable(drawable);
        }
    }

    private void shareQuote(Quote quote) {
        if (quote == null) {
            return;
        }

        trackShareAction();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, String.format(getResources().getString(R.string
                .share_title), quote.getAuthor()));
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string
                .share_body), quote.getText(), quote.getAuthor()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onTodayQuoteReceived(Quote quote) {
        mQuote = quote;
        updateViews();
    }

    private void trackShareAction() {
        ParseAnalytics.trackEventInBackground(TrackerConstants.SHARE_ACTION);
    }
}
