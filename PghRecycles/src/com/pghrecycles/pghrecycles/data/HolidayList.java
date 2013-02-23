package com.pghrecycles.pghrecycles.data;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Time;

public class HolidayList {
	
	private List<Holiday> mHolidayList;
	
	public HolidayList() {
		mHolidayList = new ArrayList<Holiday>();
	}
	
	public void add(Holiday holiday) {
		if (!mHolidayList.contains(holiday)) {
			mHolidayList.add(holiday);
		}
	}
	
	/**
	 * returns the list of holidays which fall during the week that date is in 
	 * @param date
	 * @return
	 */
	public List<Holiday> getHolidaysInWeek(Time date) {
		List<Holiday> holidaysInWeek = new ArrayList<Holiday>();
		
		//TODO: logic here
		
		return holidaysInWeek;
	}
	
	/**
	 * returns true if the week that date falls in, contains a holiday
	 * @param date
	 * @return
	 */
	public boolean isWeekContainingHoliday(Time date) {
		//TODO: logic here
		return false;
	}
}
