package com.pghrecycles.pghrecycles;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pghrecycles.pghrecycles.data.DateTimeUtil;
import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DBDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.DBHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.DBPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.DivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;
import com.pghrecycles.pghrecycles.data.providers.HolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;
import com.pghrecycles.pghrecycles.model.ApplicationState;
import com.pghrecycles.pghrecycles.model.PickupDateModel;
import com.pghrecycles.pghrecycles.notification.Notifier;

public class SetupLocationActivity extends Activity {
	PickupInfoProvider pickupInfoProvider;
	DivisionInfoProvider divisionInfoProvider;
	HolidayListProvider holidayListProvider;
	PickupDateModel mPickupDateModel;
	
	ApplicationState mApplicationState = ApplicationState.getInstance();
	

	private static final String TAG = "GreenPerks";
	private static final boolean FLAG_DEBUG = true;
	private static void log(String msg) {
		if (FLAG_DEBUG) {
			Log.e(TAG, msg);
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_location);
		
		this.getActionBar().setHomeButtonEnabled(true);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setTitle(R.string.title_activity_setup_location);
		
		Button btnGetLocation = (Button)findViewById(R.id.getLocationButton);
		
		
		btnGetLocation.setOnClickListener(/*new GetLocationButtonListener(this)*/ new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				GeoLocationProvider geo = new GeoLocationProvider(getApplicationContext());
				if(geo.canGetLocation()) {
				    double latitude = geo.getLatitude();
				    double longitude = geo.getLongitude();
				    
				    ArrayList<String> streetAddr = geo.getStreetAddress();
				    
				    ((EditText)findViewById(R.id.editTextAddress)).setText(streetAddr.get(0));
				    ((EditText)findViewById(R.id.editTextStreet)).setText(streetAddr.get(1));
				    ((EditText)findViewById(R.id.editTextZip)).setText(streetAddr.get(2));
				    
				    // \n is for new line
//				    Toast.makeText(context.getApplicationContext(), "Your Location is - \nLat: " + latitude + 
//				    		"\nLong: " + longitude, Toast.LENGTH_LONG).show();
//				    Toast.makeText(context.getApplicationContext(), "Your Address is - \n" + streetAddr, 
//				    		Toast.LENGTH_LONG).show();
				} else {
					Log.e("PghRecycles", " Unable to get geolocation");
					geo.showSettingsAlert();
				}				
			}
		});

		pickupInfoProvider = new DBPickupInfoProvider(this);
		divisionInfoProvider = new DBDivisionInfoProvider(this);
		holidayListProvider = new DBHolidayListProvider(this);
		mPickupDateModel = new PickupDateModel(pickupInfoProvider, divisionInfoProvider, holidayListProvider);
		mApplicationState.setPickupDateModel(mPickupDateModel);
		
		final Button button = (Button) findViewById(R.id.buttonDoLookup);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// build LocationInfo object
				int zip = -1;
				try {
					zip = Integer.parseInt(((EditText) findViewById(R.id.editTextZip)).getText().toString());
				} catch (NumberFormatException e) {
					// input not a number
				}
				String street = ((EditText) findViewById(R.id.editTextStreet)).getText().toString();
				int address = -1;
				try {
					address = Integer.parseInt(((EditText) findViewById(R.id.editTextAddress)).getText().toString());
				} catch (NumberFormatException e) {
					// input not a number
				}
				LocationInfo locationInfo = new LocationInfo(address, street, "", zip);
				Time now = new Time();
				now.setToNow();
				DateTimeUtil.setToMidnight(now);

				// do lookup for pickup
				PickupInfo pickupInfo = mPickupDateModel.getPickupInfo(locationInfo, now);

				if (pickupInfo == null || pickupInfo.getDivision() == null) {
					Toast.makeText(SetupLocationActivity.this, getResources().getString(R.string.address_not_found), Toast.LENGTH_SHORT).show();
				} else {
					HolidayList holidayList = mPickupDateModel.getHolidayList(now);
					DivisionInfo divisionInfo = mPickupDateModel.getDivisionInfo(pickupInfo.getDivision(), now);
					mApplicationState.setPickupInfo(pickupInfo);
					mApplicationState.setDivisionInfo(divisionInfo);
					mApplicationState.setHolidayList(holidayList);
					finish();
				}
			}
		});

		test();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
        	finish();
            return true;
		}
		return true;
    }


	private void test() {
		PickupDateModel model = mPickupDateModel;
		// PickupDateModel model = new PickupDateModel(new
		// MockPickupInfoProvider(), new MockDivisionInfoProvider(), new
		// MockHolidayListProvider());
		
		LocationInfo locationInfo = new LocationInfo(925, "Chislett", "", 15206);
		Time currentDate = new Time();
		currentDate.set(23, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		PickupInfo pickupInfo = model.getPickupInfo(locationInfo, currentDate);
		HolidayList holidayList = model.getHolidayList(currentDate);
		DivisionInfo divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		PickupDate nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		PickupDate nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/23 Central -- > 2/28");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/23 Central -- > 2/28");

		currentDate.set(24, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/24 Central -- > 2/28");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/24 Central -- > 2/28");

		currentDate.set(25, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/25 Central -- > 2/28");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/25 Central -- > 2/28");
		

		currentDate.set(26, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/26 Central -- > 2/28");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/26 Central -- > 2/28");

		currentDate.set(27, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/27 Central -- > 2/28");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/27 Central -- > 2/28");

		currentDate.set(28, 2 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL") + " test 2/28 Central -- > 2/28 (same date)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-02-28") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 2/28 Central -- > 2/28 (same date)");
		

		currentDate.set(1, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/1 Central -- > 3/7 (next week)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/01 Central -- > 03-14 (2 wks)");
		

		currentDate.set(2, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);

		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/2 Central -- > 3/7 (next week)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/02 Central -- > 03-14");
		

		currentDate.set(3, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/3 Central -- > 3/7 (sunday, later in the week)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/03 Central -- > 03-14");

		currentDate.set(4, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/4 Central -- > 3/7");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/04 Central -- > 03-14");

		currentDate.set(5, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/5 Central -- > 3/7");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/05 Central -- > 03-14");
		

		currentDate.set(6, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/6 Central -- > 3/7");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/06 Central -- > 03-14");

		currentDate.set(7, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-07") ? "OK" : "FAIL") + " test 3/7 Central -- > 3/7 (same day)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/07 Central -- > 03-14");

		currentDate.set(8, 3 - 1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL") + " test 3/8 Central -- > 3/14 (next week)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-03-14") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 03/08 Central -- > 03-14");

		currentDate.set(28, 6-1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-07-05") ? "OK" : "FAIL (" + nextRefuseDate.getDate().format3339(true) + ")") + " test 6/28 Central -- > 7/5 (holiday bump next week)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-07-05") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 06/28 Central -- > 7/5 (holiday bump)");

		
		currentDate.set(1, 0, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-01-04") ? "OK" : "FAIL (" + nextRefuseDate.getDate().format3339(true) + ")") + " test 1/1 Central -- > 1/4 (holiday bump to next day)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-01-04") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 1/1 Central -- > 1/4 (holiday bump)");

		currentDate.set(4, 7-1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-07-05") ? "OK" : "FAIL") + " test 7/4 Central -- > 7/5 (next day, holiday bump)");
		log((nextRecycleDate.getDate().format3339(true).equals("2013-07-05") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 07/04 Central -- > 7/5 (holiday bump)");

		currentDate.set(24, 12-1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2013-12-27") ? "OK" : "FAIL (" + nextRefuseDate.getDate().format3339(true) + ")") + " test 12/24 Central -- > 12/27 (same week, holiday bump)");
		log((nextRecycleDate.getDate().format3339(true).equals("2014-01-02") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 12/24 Central -- > 01/02 (holiday bump, next year)");

		currentDate.set(31, 12-1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		holidayList = model.getHolidayList(currentDate);
		divisionInfo = model.getDivisionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		log((nextRefuseDate.getDate().format3339(true).equals("2014-01-02") ? "OK" : "FAIL (" + nextRefuseDate.getDate().format3339(true) + ")") + " test 12/31 Central -- > 01/02 (next year)");
		log((nextRecycleDate.getDate().format3339(true).equals("2014-01-02") ? "OK" : "FAIL (" + nextRecycleDate.getDate().format3339(true) + ")") + " test recycle 12/31 Central -- > 01/02 (holiday bump, next year)");
		
		
		currentDate.set(23, 2-1, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		PickupDate nextYardDebrisDate = model.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		log((nextYardDebrisDate.getDate().format3339(true).equals("2013-05-18") ? "OK" : "FAIL") + " test yard debris 2/23 Central --> 5/18");

		currentDate.set(18, 4, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		nextYardDebrisDate = model.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		log((nextYardDebrisDate.getDate().format3339(true).equals("2013-05-18") ? "OK" : "FAIL") + " test yard debris 5/18 Central --> 5/18 (same day)");

		currentDate.set(19, 4, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		nextYardDebrisDate = model.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		log((nextYardDebrisDate.getDate().format3339(true).equals("2013-11-09") ? "OK" : "FAIL") + " test yard debris 5/19 Central --> 11/9 (later in year)");

		
		currentDate.set(9, 10, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		nextYardDebrisDate = model.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		log((nextYardDebrisDate.getDate().format3339(true).equals("2013-11-09") ? "OK" : "FAIL") + " test yard debris 11/9 Central --> 11/9 (same day)");
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		/*
		currentDate.set(10, 10, 2013);
		DateTimeUtil.setToMidnight(currentDate);
		nextYardDebrisDate = model.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		currentDate.setToNow();
		DateTimeUtil.setToMidnight(currentDate);
		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));

		currentDate.set(1, 0, 2013);
		DateTimeUtil.setToMidnight(currentDate);

		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));

		currentDate.set(31, 11, 2013);
		DateTimeUtil.setToMidnight(currentDate);

		nextRecycleDate = model.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = model.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		*/
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_pgh_recycles, menu);
//		return true;
//	}
}
