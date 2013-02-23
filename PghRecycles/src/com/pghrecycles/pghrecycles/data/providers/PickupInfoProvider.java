package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

public interface PickupInfoProvider {
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time year);
}
