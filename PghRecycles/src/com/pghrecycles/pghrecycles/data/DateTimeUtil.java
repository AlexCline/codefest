package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

public class DateTimeUtil {
	public static void setToMidnight(Time dateTime) {
    	dateTime.set(0, 0, 0, dateTime.monthDay, dateTime.month, dateTime.year);
		dateTime.normalize(true);
	}
}
