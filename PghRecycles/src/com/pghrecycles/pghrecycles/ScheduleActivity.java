package com.pghrecycles.pghrecycles;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pghrecycles.pghrecycles.data.DateTimeUtil;
import com.pghrecycles.pghrecycles.model.ApplicationState;
import com.pghrecycles.pghrecycles.model.PickupDateModel;
import com.pghrecycles.pghrecycles.notification.Notifier;

public class ScheduleActivity extends Activity {
	private ApplicationState mApplicationState = ApplicationState.getInstance();
	private PickupDateModel mPickupDateModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		this.getActionBar().setHomeButtonEnabled(true);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setTitle(R.string.title_activity_schedule);

		Button checkinButton = (Button) findViewById(R.id.checkin_button);
		checkinButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), CheckInActivity.class);
				startActivity(i);
			}
		});

	}
	
	
	Handler mHandler = new Handler();
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mPickupDateModel = mApplicationState.getPickupDateModel();
		
		if (mApplicationState.getPickupInfo() != null) {
			Time now = new Time();
			now.setToNow();
			DateTimeUtil.setToMidnight(now);
			
			TextView nextPickupDayView = (TextView) findViewById(R.id.next_pickup_day);
			nextPickupDayView.setText(getTextForDayNumber(mApplicationState.getPickupInfo().getDay()) + " (" + mApplicationState.getPickupInfo().getDivision().toString() + " Division)");
			
			TextView nextPickupDateView = (TextView) findViewById(R.id.next_pickup_date);
			final Time nextRefusePickupDate = mPickupDateModel.getNextRefusePickupDate(mApplicationState.getPickupInfo(), mApplicationState.getHolidayList(), now).getDate();
			nextPickupDateView.setText(nextRefusePickupDate.format("%A, %m/%d"));

			TextView nextRecycleDateView = (TextView) findViewById(R.id.next_recycle_date);
			final Time nextRecyclingPickupDate = mPickupDateModel.getNextRecyclingPickupDate(mApplicationState.getPickupInfo(), mApplicationState.getHolidayList(), mApplicationState.getDivisionInfo(), now).getDate();
			nextRecycleDateView.setText(nextRecyclingPickupDate.format("%A, %m/%d"));

			TextView nextYardDebrisDateView = (TextView) findViewById(R.id.next_yard_debris_date);
			final Time nextYardDebrisPickupDate = mPickupDateModel.getNextYardDebrisPickupDate(mApplicationState.getDivisionInfo(), now).getDate();
			nextYardDebrisDateView.setText(nextYardDebrisPickupDate.format("%B %e"));
			
			if (nextRefusePickupDate.format3339(true).equals(nextRecyclingPickupDate.format3339(true))) {
								
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Notifier.notifyRealtime(ScheduleActivity.this, getResources().getString(R.string.notification_recycle_ticker), getResources().getString(R.string.notification_recycle_title),
								String.format(getResources().getString(R.string.notification_recycle_content), nextRecyclingPickupDate.format("%A")));
					}
	
				}, new Random().nextInt(10000) + 10000);
				
			} else {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Notifier.notifyRealtime(ScheduleActivity.this, getResources().getString(R.string.notification_garbage_ticker), getResources().getString(R.string.notification_garbage_title),
								String.format(getResources().getString(R.string.notification_garbage_content), nextRefusePickupDate.format("%A")));
					}
	
				}, new Random().nextInt(10000) + 10000);
			}

//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					Notifier.notifyRealtime(ScheduleActivity.this, getResources().getString(R.string.notification_debris_ticker), getResources().getString(R.string.notification_debris_title),
//							String.format(getResources().getString(R.string.notification_debris_content), nextYardDebrisPickupDate.format("%B%e")));
//				}
//			}, new Random().nextInt(60000) + 30000);				
		}
	}
	
	private static Object sToken = new Object();
	private static boolean sHasRunnables = false;

	private String getTextForDayNumber(int day) {
		return DateTimeUtil.DAYS_MAP[day];
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_schedule, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_settings:
			Intent intent2 = new Intent(this, SetupLocationActivity.class);
			startActivity(intent2);
			return true;
		}
		return true;
	}
}
