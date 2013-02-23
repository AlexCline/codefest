package com.pghrecycles.pghrecycles.data;

import java.util.ArrayList;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

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
	 * determines if there is a holiday preceding the supplied date 
	 * @param date
	 * @return
	 */
	public boolean isHolidayInWeekBefore(Time date) {
		for (Holiday holiday : mHolidayList) {
			Time holidayDate = holiday.getDate();
			Log.e("PghRecycles", "holiday: " + holidayDate.format3339(true) + " before? " + holidayDate.before(date));
			if (holidayDate.before(date)) {
				float distance = (holidayDate.toMillis(true) - date.toMillis(true)) / (1000 * 60 * 60 * 24.0f);
				if (distance > 0 && distance < 6) {
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