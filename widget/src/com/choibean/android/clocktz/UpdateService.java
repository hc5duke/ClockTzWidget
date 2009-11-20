package com.choibean.android.clocktz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateService extends Service {
	private DateFormat format;
	private TimeZone timezone;
	private Date date;

	@Override
	public void onStart(Intent intent, int startId) {
		// Build the widget update for today
		RemoteViews updateViews = buildUpdate(this);

		// Push update for this widget to the home screen
		ComponentName thisWidget = new ComponentName(this, ClockTzWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, updateViews);
	}

	/**
	 * Build a widget update to show the current Wiktionary "Word of the day."
	 * Will block until the online API returns.
	 */
	public RemoteViews buildUpdate(Context context) {
		date = new Date();
		format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault());
		timezone = TimeZone.getTimeZone("Asia/Seoul");
		format.setTimeZone(timezone);

		RemoteViews updateViews = null;

		// Build an update that holds the updated widget contents
		updateViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_word);

		String[] timezoneText = timezone.getID().split("/");
		updateViews.setTextViewText(R.id.word_title, timezoneText[timezoneText.length-1]);
		updateViews.setTextViewText(R.id.definition, format.format(date));

        Intent detailIntent = new Intent(context, PreferencesFromXml.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, detailIntent, 0);
        updateViews.setOnClickPendingIntent(R.id.widget, pending);
		return updateViews;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't need to bind to this service
		return null;
	}
}