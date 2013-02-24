package com.pghrecycles.pghrecycles;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.pghrecycles.pghrecycles.data.providers.HolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.PointsProvider;
import com.pghrecycles.pghrecycles.model.PickupDateModel;
import com.pghrecycles.pghrecycles.notification.Notifier;
import com.pghrecycles.pghrecyles.listeners.CheckInButtonListener;
import com.pghrecycles.pghrecyles.listeners.GetLocationButtonListener;

public class PghRecycles extends Activity {

	PickupInfoProvider pickupInfoProvider;
	DivisionInfoProvider divisionInfoProvider;
	HolidayListProvider holidayListProvider;

	PickupDateModel mPickupDateModel;
	
	PointsProvider pointsProvider = new PointsProvider();
	
	// ugly static way to get a context
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pgh_recycles);
		context = this;
		
		ImageButton btnGetLocation = (ImageButton)findViewById(R.id.getLocationButton);
		btnGetLocation.setOnClickListener(new GetLocationButtonListener(this));
		
		Button checkoutButton = (Button)findViewById(R.id.buttonCheckout);
		checkoutButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getBaseContext(), CheckInActivity.class);
					startActivity(i);
				}
		});
		ImageButton btnCheckIn = (ImageButton)findViewById(R.id.checkInButton);
		btnCheckIn.setOnClickListener(new CheckInButtonListener(pointsProvider));
		
		//Get the points
		
		((TextView) findViewById(R.id.pointsHolder)).setText(Integer.toString(pointsProvider.getPoints()));
		
		pickupInfoProvider = new DBPickupInfoProvider(this);
		divisionInfoProvider = new DBDivisionInfoProvider(this);
		holidayListProvider = new DBHolidayListProvider(this);

		mPickupDateModel = new PickupDateModel(pickupInfoProvider, divisionInfoProvider, holidayListProvider);

		// initialize button listener
		// TODO move this out of activity creation?
		final Button button = (Button) findViewById(R.id.buttonDoLookup);
		final PghRecycles pghRecyclesActivity = this;
		
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

				if (pickupInfo == null) {
					Toast.makeText(PghRecycles.this, getResources().getString(R.string.address_not_found), Toast.LENGTH_SHORT).show();
				} else {
					int pickupDay = pickupInfo.getDay();
					((EditText) findViewById(R.id.editTextResults)).setText(pickupDay + "");

					Log.e("PghRecycles", "pickup info day: " + pickupDay + " division: " + pickupInfo.getDivision());

					HolidayList holidayList = mPickupDateModel.getHolidayList(now);
					DivisionInfo divisionInfo = mPickupDateModel.getDivisionInfo(pickupInfo.getDivision(), now);

					TextView nextPickupDateView = (TextView) findViewById(R.id.next_pickup_date);
					final Time nextRefusePickupDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, now).getDate();
					nextPickupDateView.setText(nextRefusePickupDate.format3339(true));

					TextView nextRecycleDateView = (TextView) findViewById(R.id.next_recycle_date);
					final Time nextRecyclingPickupDate = mPickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, now).getDate();
					nextRecycleDateView.setText(nextRecyclingPickupDate.format3339(true));

					TextView nextYardDebrisDateView = (TextView) findViewById(R.id.next_yard_debris_date);
					final Time nextYardDebrisPickupDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, now).getDate();
					nextYardDebrisDateView.setText(nextYardDebrisPickupDate.format3339(true));

					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_recycle_ticker), getResources().getString(R.string.notification_recycle_title),
									String.format(getResources().getString(R.string.notification_recycle_content), nextRecyclingPickupDate.format("%A")));
						}

					}, new Random().nextInt(5000) + 1000);

					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_garbage_ticker), getResources().getString(R.string.notification_garbage_title),
									String.format(getResources().getString(R.string.notification_garbage_content), nextRefusePickupDate.format("%A")));
						}

					}, new Random().nextInt(10000) + 10000);

					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_debris_ticker), getResources().getString(R.string.notification_debris_title),
									String.format(getResources().getString(R.string.notification_debris_content), nextYardDebrisPickupDate.format("%B%e")));
						}

					}, new Random().nextInt(60000) + 30000);
				}
				
				// for testing the recycling panel
				Intent loadRecyclingIntent = new Intent(pghRecyclesActivity, RecyclableListActivity.class);
				//detailIntent.putExtra(ItemListActivity.SOME_KEY, someVal);  // in case we want to jump to a particular page?
				try {
					Thread.sleep(500);
				} catch (InterruptedException e){}
				startActivity(loadRecyclingIntent);
				
			}
		});

		test();
	}

	Handler mHandler = new Handler();
	private static final String TAG = "GreenPerks";
	private static final boolean FLAG_DEBUG = true;
	private static void log(String msg) {
		if (FLAG_DEBUG) {
			Log.e(TAG, msg);
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pgh_recycles, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();

	}
	
	public void sampleShareIntent() {
		
		Intent share = new Intent(Intent.ACTION_SEND);
//		share.setType("image/jpeg"); // might be text, sound, whatever
		share.setType("text/plain");
		
//		String uriStrImg = "android.resource://com.pghrecycles.pghrecycles/" + R.drawable.breakfast;
		
//		share.putExtra(Intent.EXTRA_STREAM, uriStrImg);
//		share.putExtra(Intent.EXTRA_TEXT, "sample text to be shared");
		share.putExtra(Intent.EXTRA_SUBJECT, "sample subject");
		share.putExtra(Intent.EXTRA_TEXT, "http://alexanderconrad.org");
//		startActivity(Intent.createChooser(share, "share some breakfast"));
//		startActivity(Intent.createChooser(share, "share some text"));
		
		Intent chooser = Intent.createChooser(share, "intent chooser title");
		startActivity(chooser);
		
	}
}
