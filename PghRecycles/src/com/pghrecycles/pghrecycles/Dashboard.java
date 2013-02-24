package com.pghrecycles.pghrecycles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pghrecycles.pghrecycles.data.providers.PointsProvider;
import com.pghrecycles.pghrecycles.model.ApplicationState;

public class Dashboard extends Activity {
	
	PointsProvider pointsProvider = new PointsProvider();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dashboard);
		
		ApplicationState mApplicationState = ApplicationState.getInstance();
		
		if (mApplicationState.getPickupInfo() == null) {
			Intent i = new Intent(getBaseContext(), SetupLocationActivity.class);
			startActivity(i);		
		}

		ImageView btnCheckIn = (ImageView)findViewById(R.id.checkInButton);
		//btnCheckIn.setOnClickListener(new CheckInButtonListener(pointsProvider));
		btnCheckIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
        
				Intent i = new Intent(getBaseContext(), CheckInActivity.class);
				startActivity(i);				
				//Toast.makeText(getApplicationContext(), "Home Pressed", Toast.LENGTH_LONG).show();
			}			
		});
		((TextView) findViewById(R.id.pointsHolder)).setText(Integer.toString(pointsProvider.getPoints()));
		

		
		ImageView btnHome = (ImageView)findViewById(R.id.homeButton);
		btnHome.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "Home Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		TextView btnNotifyHolder = (TextView)findViewById(R.id.notifyHolder);
		btnNotifyHolder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "Notify Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnPickUpSched = (ImageView)findViewById(R.id.pickUpScheduleButton);
		btnPickUpSched.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "Pick-up Schedule Pressed", Toast.LENGTH_LONG).show();
				Intent i = new Intent(getBaseContext(), ScheduleActivity.class);
				startActivity(i);				
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
				//Toast.makeText(getApplicationContext(), "Your Activity Pressed", Toast.LENGTH_LONG).show();
			}
		});
		
		ImageView btnYourHood = (ImageView)findViewById(R.id.yourHoodButton);
		btnYourHood.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "Your Hood Pressed", Toast.LENGTH_LONG).show();
			}
		});
	}

}
