package com.pghrecycles.pghrecycles.data;

import java.util.ArrayList;
import java.util.List;

public class YardDebrisSchedule {
	private List<PickupDate> mPickupDates;
	
	/**
	 * add a pickup date to the list
	 * @param date
	 */
	public void addPickupDate(PickupDate date) {
		if (!mPickupDates.contains(date)) {
			mPickupDates.add(date);
		}
		
		//TODO: sort?
	}
	
	public YardDebrisSchedule() {
		mPickupDates = new ArrayList<PickupDate>();
	}
	
	/**
	 * returns the list of pickup dates in the schedule
	 * @return
	 */
	public List<PickupDate> getPickupDates() {
		return mPickupDates;		
	}

	@Override
	public String toString() {
		return "YardDebrisSchedule [mPickupDates=" + mPickupDates + "]";
	}
	
}
