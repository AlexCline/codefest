package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

public class DateTimeUtil {
	public static final String DAYS_MAP[] = new String[] {
		"Sunday",
		"Monday",
		"Tuesday",
		"Wednesday",
		"Thursday",
		"Friday",
		"Saturday",
	};	
	
	public static void setToMidnight(Time dateTime) {
    	dateTime.set(0, 0, 0, dateTime.monthDay, dateTime.month, dateTime.year);
		dateTime.normalize(true);
	}
}
