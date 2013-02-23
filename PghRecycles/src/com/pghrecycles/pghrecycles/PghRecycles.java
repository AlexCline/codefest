package com.pghrecycles.pghrecycles;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.MockDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.MockHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.MockPickupInfoProvider;
import com.pghrecycles.pghrecycles.model.PickupDateModel;

public class PghRecycles extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pgh_recycles);

		Time currentDate = new Time();
		currentDate.set(23,2-1,2013);
		currentDate.normalize(true);
		PickupDateModel pickupDateModel = new PickupDateModel(new MockPickupInfoProvider(), new MockDivisionInfoProvider(), new MockHolidayListProvider());
		PickupInfo pickupInfo = pickupDateModel.getPickupInfo(new LocationInfo(), currentDate);
		HolidayList holidayList = pickupDateModel.getHolidayList(currentDate);
		DivisionInfo divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		PickupDate nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(24,2-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(25,2-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(26,2-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(27,2-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(28,2-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(1,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(2,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(3,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(4,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(5,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(6,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(7,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(8,3-1,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(1,0,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(4,6,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);		
		
		currentDate.set(24,11,2013);
		currentDate.normalize(true);
		holidayList = pickupDateModel.getHolidayList(currentDate);
		divisionInfo = pickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.setToNow();
		currentDate.normalize(true);		
		PickupDate nextYardDebrisDate = pickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		
		currentDate.set(18,4,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = pickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);

		currentDate.set(19,4,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = pickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);		
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		currentDate.set(9,10,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = pickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);		
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		currentDate.set(10,10,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = pickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		} else {
			Log.e("PghRecycles", " no next date");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pgh_recycles, menu);
		return true;
	}

}
