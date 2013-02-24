package com.pghrecycles.pghrecycles.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pghrecycles.pghrecycles.PghRecycles;
import com.pghrecycles.pghrecycles.R;

public class Notifier {
	public static void notifyRealtime(Context context, CharSequence ticker, CharSequence title, CharSequence contentText) {
//		Intent intent = new Intent(context, PghRecycles.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//    	
//    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//	    Notification noti = new Notification.Builder(context).setDefaults(Notification.DEFAULT_ALL)
//        .setContentTitle(title)
//        .setTicker(ticker)
//        .setContentText(contentText)
//        //.setPriority(Notification.PRIORITY_DEFAULT)
//        .setSmallIcon(R.drawable.ic_launcher)
//        .setContentIntent(pendingIntent)
//        .build();
//	    
//	    notificationManager.notify(R.layout.activity_pgh_recycles, noti);
	}
}
