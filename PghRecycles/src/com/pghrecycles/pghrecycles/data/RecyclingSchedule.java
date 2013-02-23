package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;
import android.util.Log;

public class RecyclingSchedule {
	private int startWeek;

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}
	
	/**
	 * returns whether the date has recycling occurring on it
	 * @param date
	 * @return
	 */
	public boolean isDateForRecycling(Time date) {
		int weekNumber = date.getWeekNumber();
		Log.e("PghRecycles", "weeknumber: " + weekNumber + " startWeek: " + startWeek);
		return (weekNumber % 2) == (startWeek % 2);
	}
}
