package com.pghrecycles.pghrecycles.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.pghrecycles.pghrecycles.R;

public class Notifier {
	public static void notifyRealtime(Context context, CharSequence ticker, CharSequence title, CharSequence contentText) {
	    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification noti = new Notification.Builder(context).setDefaults(Notification.DEFAULT_ALL)
        .setContentTitle(title)
        .setTicker(ticker)
        .setContentText(contentText)
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_launcher)
        .build();
	    
	    notificationManager.notify(R.layout.activity_pgh_recycles, noti);
	}
}