package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.HolidayList;

public interface HolidayInfoProvider {
	/**
	 * returns a HolidayList for the year specified in year
	 * @param year
	 * @return
	 */
	public HolidayList getHolidayList(Time year);
	
}
