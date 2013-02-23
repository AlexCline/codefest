package com.pghrecycles.pghrecycles;


import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DBPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
            	int zip = Integer.parseInt(((EditText)findViewById(R.id.editTextZip)).getText().toString());
            	String street = ((EditText)findViewById(R.id.editTextStreet)).getText().toString();
            	int address = Integer.parseInt(((EditText)findViewById(R.id.editTextAddress)).getText().toString());
            	LocationInfo locationInfo = new LocationInfo(address, street, "", zip);
            	Time now = new Time();
            	now.setToNow();
            	PickupInfo pickupInfo = pickupInfoProvider.getPickupInfo(locationInfo, now);
            	int pickupDay = pickupInfo.getDay();
            	((EditText)findViewById(R.id.editTextResults)).setText(pickupDay+"");
            }
        });
		
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
