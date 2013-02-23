package com.pghrecycles.pghrecycles.data;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Time;

public class HolidayList {
	
	private List<Holiday> mHolidayList;
	
	public HolidayList() {
		mHolidayList = new ArrayList<Holiday>();
	}
	
	public void addHoliday(Holiday holiday) {
		
	}
	
	/**
	 * returns the list of holidays which fall during the week that date is in 
	 * @param date
	 * @return
	 */
	public List<Holiday> holidaysInWeek(Time date) {
		return null;
	}
	
	/**
	 * returns true if the week that date falls in, contains a holiday
	 * @param date
	 * @return
	 */
	public boolean isWeekContainingHoliday(Time date) {
		return false;
	}
}
