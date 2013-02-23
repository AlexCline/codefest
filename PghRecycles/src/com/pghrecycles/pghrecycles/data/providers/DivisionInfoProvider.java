package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;

public interface DivisionInfoProvider {
	public DivisionInfo getDivisionInfo(Division division, Time year);
}
