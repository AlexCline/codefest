package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

public interface PickupInfoProvider {
	/**
	 * Given locationInfo, and a year specified in the date provided by year, provides Pickup Information
	 * @param locationInfo
	 * @param year
	 * @return
	 */
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time year);
}
