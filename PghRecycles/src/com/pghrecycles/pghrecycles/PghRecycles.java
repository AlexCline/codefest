package com.pghrecycles.pghrecycles;


import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DBPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;
import com.pghrecycles.pghrecycles.data.providers.MockDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.MockHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.MockPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;
import com.pghrecycles.pghrecycles.model.PickupDateModel;

public class PghRecycles extends Activity {

	PickupInfoProvider pickupInfoProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pgh_recycles);
		
		// initialize button listener
		// TODO move this out of activity creation?
		final Button button = (Button) findViewById(R.id.buttonDoLookup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // do lookup
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
            	PickupInfo pickupInfo = pickupInfoProvider.getPickupInfo(locationInfo, now);
            	int pickupDay = pickupInfo.getDay();
            	((EditText)findViewById(R.id.editTextResults)).setText(pickupDay+"");
            }
        });
		
		GeoLocationProvider geo = new GeoLocationProvider(this);
		if(geo.canGetLocation()) {
            double latitude = geo.getLatitude();
            double longitude = geo.getLongitude();
            
            ArrayList<String> streetAddr = geo.getStreetAddress();
            
            ((EditText)findViewById(R.id.editTextAddress)).setText(streetAddr.get(0));
            ((EditText)findViewById(R.id.editTextStreet)).setText(streetAddr.get(1));
            ((EditText)findViewById(R.id.editTextZip)).setText(streetAddr.get(2));
            
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + 
            		"\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Your Address is - \n" + streetAddr, 
            		Toast.LENGTH_LONG).show();
		} else {
			Log.e("PghRecycles", " Unable to get geolocation");
			geo.showSettingsAlert();
		}
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
		
	}

}
