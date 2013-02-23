package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.LocationInfo;

public interface DivisionInfoProvider {
	public DivisionInfo getDivisionInfo(LocationInfo locationInfo, Time year);
}
