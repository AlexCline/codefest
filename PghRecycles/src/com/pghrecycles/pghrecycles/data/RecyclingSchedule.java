package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

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
		return (weekNumber % 2) == (startWeek % 2);
	}

	@Override
	public String toString() {
		return "RecyclingSchedule [startWeek=" + startWeek + "]";
	}
	
}
