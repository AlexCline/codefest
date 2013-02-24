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
	
	private float getDateDistanceInDays(Time earlier, Time later) {
		Time fromCompare = new Time(later);
		fromCompare.set(0, 0, 0, later.monthDay, later.month, later.year);
		fromCompare.normalize(false);
		Time toCompare = new Time(earlier);
		toCompare.set(0, 0, 0, earlier.monthDay, earlier.month, earlier.year);
		toCompare.normalize(false);
		
		float distance = (later.toMillis(true) - earlier.toMillis(true)) / (float)MS_IN_DAY;
		return distance;
	}
	
	/**
	 * determines if there is a holiday preceding the supplied date 
	 * @param date
	 * @return
	 */
	public boolean isHolidayInWeekOnOrBefore(Time date) {
		for (Holiday holiday : mHolidayList) {
			Time holidayDate = holiday.getDate();
						
			boolean datesEqual = (holidayDate.format3339(true).equals(date.format3339(true)));
			
			if (datesEqual) {
				return true;
			}
			
			if (holidayDate.before(date)) {
				
				float distance = getDateDistanceInDays(holidayDate, date);
				
				//Log.e(this.getClass().getName(), "comparing: " + holidayDate.format3339(true) + " with " + date.format3339(true) + " distance: "+ distance + " " + datesEqual + " " + holidayDate.weekDay + " " + date.weekDay);	
				
				if ((distance >= 0 && distance < 6)) {
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