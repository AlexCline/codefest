package com.pghrecycles.pghrecycles.model;

import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.HolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;

public class PickupDateModel {
	private static final int MS_IN_DAY = (1000 * 60 * 60 * 24);
	private static final int DAYS_IN_WEEK = 7;
	private PickupInfoProvider mPickupInfoProvider;
	private DivisionInfoProvider mDivisionInfoProvider;
	private HolidayListProvider mHolidayListProvider;
	
	
	public PickupDateModel(PickupInfoProvider pickupInfoProvider, DivisionInfoProvider divisionInfoProvider, HolidayListProvider holidayListProvider) {
		mPickupInfoProvider = pickupInfoProvider;
		mDivisionInfoProvider = divisionInfoProvider;
		mHolidayListProvider = holidayListProvider;
	}
	
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time date) {
		return mPickupInfoProvider.getPickupInfo(locationInfo, date);
	}
	public HolidayList getHolidayList(Time date) {
		return mHolidayListProvider.getHolidayList(date);
	}
	public DivisionInfo getDisivionInfo(Division division, Time date) {
		return mDivisionInfoProvider.getDivisionInfo(division, date);
	}
	
	/**
	 * 
	 * @param pickupInfo
	 * @param holidayList
	 * @param date
	 * @return
	 */
	public PickupDate getNextRefusePickupDate(PickupInfo pickupInfo, HolidayList holidayList, Time currentDate) {
		PickupDate nextPickupDate = null;
		Time pickup = new Time(currentDate);	
		
		// today
		int distanceDays = 0;
		
		if (currentDate.weekDay < pickupInfo.getDay()) {
			// upcoming
			distanceDays = (pickupInfo.getDay()-currentDate.weekDay);
			Log.e("PghRecycles", "upcoming: current date weekday: " + currentDate.weekDay + " pickupInfoDay: " + pickupInfo.getDay() + " distance Days: " + distanceDays);
			
		} else if (currentDate.weekDay > pickupInfo.getDay()) {
			// next weekday
			distanceDays = ((DAYS_IN_WEEK-currentDate.weekDay) + pickupInfo.getDay()) % (DAYS_IN_WEEK);
			
			Log.e("PghRecycles", "next week: current date weekday: " + currentDate.weekDay + " pickupInfoDay: " + pickupInfo.getDay() + " distance Days: " + distanceDays);
		}

		pickup.set(pickup.toMillis(true) + (distanceDays * MS_IN_DAY));			
		
		Log.e("PghRecycles", "holidayInWeek? " + holidayList.isHolidayInWeekBefore(pickup));			
		
		// bump to next day (not business day), if holiday immediately precedes the day
		if (holidayList.isHolidayInWeekBefore(pickup)) {
			pickup.set(pickup.toMillis(true) + MS_IN_DAY);
		}
		
		nextPickupDate = new PickupDate(pickup);
		return nextPickupDate;
	}
	
	/**
	 * 
	 * @param pickupInfo
	 * @param holidayList
	 * @param divisionInfo
	 * @param date
	 * @return
	 */
	public PickupDate getNextRecyclingPickupDate(PickupInfo pickupInfo, HolidayList holidayList, DivisionInfo divisionInfo, Time currentDate) {
		return null;
	}

	/**
	 * may return null if there are no future yard debris pickup dates
	 * @param pickupInfo
	 * @param holidayList
	 * @param divisionInfo
	 * @param date
	 * @return
	 */
	public PickupDate getNextYardDebrisPickupDate(DivisionInfo divisionInfo, Time currentDate) {
		PickupDate earliestDate = null;
		for (PickupDate pd : divisionInfo.getYardDebrisSchedule().getPickupDates()) {
			if (currentDate.before(pd.getDate())) {
				if (earliestDate == null || pd.getDate().before(earliestDate.getDate())) {
					earliestDate = pd;
				}
			}
		}
		return earliestDate;
	}
}