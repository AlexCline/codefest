package com.pghrecycles.pghrecyles.listeners;

import java.util.ArrayList;

import com.pghrecycles.pghrecycles.R;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class GetLocationButtonListener extends Activity implements OnClickListener {
	private Context context = null;
	
	public GetLocationButtonListener(Context context){
		this.context = context;
		Log.e("PghRecycles", context.toString());
	}
	
	@Override
	public void onClick(View v) {
		View parent = (View)v.getParent();

		GeoLocationProvider geo = new GeoLocationProvider(context);
		if(geo.canGetLocation()) {
		    double latitude = geo.getLatitude();
		    double longitude = geo.getLongitude();
		    
		    ArrayList<String> streetAddr = geo.getStreetAddress();
		    
		    ((EditText)parent.findViewById(R.id.editTextAddress)).setText(streetAddr.get(0));
		    ((EditText)parent.findViewById(R.id.editTextStreet)).setText(streetAddr.get(1));
		    ((EditText)parent.findViewById(R.id.editTextZip)).setText(streetAddr.get(2));
		    
		    // \n is for new line
		    Toast.makeText(context.getApplicationContext(), "Your Location is - \nLat: " + latitude + 
		    		"\nLong: " + longitude, Toast.LENGTH_LONG).show();
		    Toast.makeText(context.getApplicationContext(), "Your Address is - \n" + streetAddr, 
		    		Toast.LENGTH_LONG).show();
		} else {
			Log.e("PghRecycles", " Unable to get geolocation");
			geo.showSettingsAlert();
		}
		//read your lovely variable
	}

};