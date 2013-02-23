package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

public class MockPickupInfoProvider implements PickupInfoProvider {

	@Override
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time year) {
		PickupInfo pickupInfo = new PickupInfo();
		
		pickupInfo.setDay(Time.THURSDAY);
		pickupInfo.setStreet("Test");
		pickupInfo.setStreetBase("TestBase");
		pickupInfo.setHood("Morningside");
		pickupInfo.setZip(15206);
		pickupInfo.setLeftLow(1);
		pickupInfo.setLeftHigh(2);
		pickupInfo.setRightLow(1);
		pickupInfo.setRightHigh(2);
		pickupInfo.setDivision(DivisionInfo.Division.EASTERN);
		
		return pickupInfo;
	}
}