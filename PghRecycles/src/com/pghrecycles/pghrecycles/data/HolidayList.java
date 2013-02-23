package com.pghrecycles.pghrecycles.data;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Time;

public class HolidayList {
	private static final int MS_IN_DAY = (1000 * 60 * 60 * 24);
	
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
	 * determines if there is a holiday preceding the supplied date 
	 * @param date
	 * @return
	 */
	public boolean isHolidayInWeekOnOrBefore(Time date) {
		for (Holiday holiday : mHolidayList) {
			Time holidayDate = holiday.getDate();
			if (holidayDate.before(date) || (holidayDate.year == date.year && holidayDate.month == date.month && holidayDate.monthDay == date.monthDay)) {
				float distance = (date.toMillis(true) - holidayDate.toMillis(true)) / (float)MS_IN_DAY;
				
				if (distance >= 0 && distance < 6) {
					if (holidayDate.weekDay < date.weekDay) {
						// this bumps the date
						return true;
					}					
				}
			}
		}
		return false;
	}
}