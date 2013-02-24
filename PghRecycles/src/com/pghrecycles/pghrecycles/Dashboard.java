package com.pghrecycles.pghrecycles;

import com.pghrecycles.pghrecycles.data.providers.PointsProvider;
import com.pghrecycles.pghrecyles.listeners.CheckInButtonListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends Activity {
	
	PointsProvider pointsProvider = new PointsProvider();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		ImageView btnCheckIn = (ImageView)findViewById(R.id.checkInButton);
		btnCheckIn.setOnClickListener(new CheckInButtonListener(pointsProvider));
		
		((TextView) findViewById(R.id.pointsHolder)).setText(Integer.toString(pointsProvider.getPoints()));
		

		
		ImageView btnHome = (ImageView)findViewById(R.id.homeButton);
		btnHome.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Home Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		TextView btnNotifyHolder = (TextView)findViewById(R.id.notifyHolder);
		btnNotifyHolder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Notify Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnPickUpSched = (ImageView)findViewById(R.id.pickUpScheduleButton);
		btnPickUpSched.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Pick-up Schedule Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnRecycleAnything = (ImageView)findViewById(R.id.recycleAnythingButton);
		btnRecycleAnything.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Recycle Anything Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnYourActivity = (ImageView)findViewById(R.id.yourActivityButton);
		btnYourActivity.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Your Activity Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnYourHood = (ImageView)findViewById(R.id.yourHoodButton);
		btnYourHood.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Your Hood Pressed", Toast.LENGTH_LONG).show();
			}
		});
	}

}
