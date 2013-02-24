package com.pghrecycles.pghrecycles;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DBDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.DBPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.DivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.MockDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.MockHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.MockPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;
import com.pghrecycles.pghrecycles.model.PickupDateModel;

public class PghRecycles extends Activity {

	PickupInfoProvider pickupInfoProvider;
	DivisionInfoProvider divisionInfoProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pgh_recycles);
		
		// initialize button listener
		// TODO move this out of activity creation?
		final Button button = (Button) findViewById(R.id.buttonDoLookup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// build LocationInfo object
            	int zip = -1;
            	try {
            		zip = Integer.parseInt(((EditText)findViewById(R.id.editTextZip)).getText().toString());
            	} catch (NumberFormatException e) {
            		// input not a number
            	}
            	String street = ((EditText)findViewById(R.id.editTextStreet)).getText().toString();
            	int address = -1;
            	try {
            		address = Integer.parseInt(((EditText)findViewById(R.id.editTextAddress)).getText().toString());
            	} catch (NumberFormatException e) {
            		// input not a number
            	}
            	LocationInfo locationInfo = new LocationInfo(address, street, "", zip);
            	Time now = new Time();
            	now.setToNow();

                // do lookup for pickup
            	PickupInfo pickupInfo = pickupInfoProvider.getPickupInfo(locationInfo, now);
            	int pickupDay = pickupInfo.getDay();
            	((EditText)findViewById(R.id.editTextResults)).setText(pickupDay+"");
            	
            	// do lookup for division
            	Division division = pickupInfo.getDivision();
            	int year = pickupInfo.getYear();
            	Time time = new Time();
            	time.set(0, 0, year);  // we only care about the year?
            	DivisionInfo divisionInfo = divisionInfoProvider.getDivisionInfo(division, time);
            	
            }
        });
		

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
		
		
		currentDate.setToNow();
		currentDate.normalize(true);
		PickupDate nextRecycleDate = pickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
		currentDate.set(1,0,2013);
		currentDate.normalize(true);				
		nextRecycleDate = pickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
		currentDate.set(31,11,2013);
		currentDate.normalize(true);				
		nextRecycleDate = pickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = pickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pgh_recycles, menu);
		return true;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// initialize connection to database
		// TODO move this out of activity creation?
		pickupInfoProvider = new DBPickupInfoProvider(this);
		divisionInfoProvider = new DBDivisionInfoProvider(this);
		
	}

}
