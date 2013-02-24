package com.pghrecycles.pghrecycles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;
import com.pghrecycles.pghrecycles.model.ApplicationState;

public class CheckInActivity extends Activity {
	private static NfcAdapter mAdapter;
	private GoogleMap mMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);
		final Context context = this;
		
		this.getActionBar().setHomeButtonEnabled(true);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setTitle(R.string.title_activity_check_in);
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
			
		MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mMap = mapFrag.getMap();

		if (savedInstanceState == null) {
//			//CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044, -73.98180484771729));
//			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(40.4660, -79.9650));
//			CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
//
//			mMap.moveCamera(center);
//			mMap.animateCamera(zoom);
		}
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.setMyLocationEnabled(true);
        centerOnMyLocation();
        
        Button btnCheckIn = (Button)findViewById(R.id.checkin_button);
        btnCheckIn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				int points = 0;
		    	CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);
		    	CheckBox cb2 = (CheckBox) findViewById(R.id.checkBox2);
		    	CheckBox cb3 = (CheckBox) findViewById(R.id.checkBox3);
		    	CheckBox cb4 = (CheckBox) findViewById(R.id.checkBox4);
		    	CheckBox cb5 = (CheckBox) findViewById(R.id.checkBox5);
		    	
		    	points += (cb1.isChecked() ? 100 : 0);
		    	points += (cb2.isChecked() ? 100 : 0);
		    	points += (cb3.isChecked() ? 100 : 0);
		    	points += (cb4.isChecked() ? 150 : 0);
		    	points += (cb5.isChecked() ? 300 : 0);
		    	
		    	final int pointsF = points;
		    	
				new AlertDialog.Builder(context)
			    .setTitle("Check-In Successful!")
			    .setMessage("You've successfully checked in and have gained " + points + " points!  Congratulations!")
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	ApplicationState mApplicationState = ApplicationState.getInstance();
			        	mApplicationState.addPoints(pointsF);
			        	Intent i = new Intent(getBaseContext(), Dashboard.class);
						startActivity(i);
			        }
			     })
			     .show();

				//Toast.makeText(getApplicationContext(), "Check-In Pressed", Toast.LENGTH_LONG).show();
			}
		});
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
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_check_in, menu);
		return true;
	}

	
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}
	
	private void centerOnMyLocation() {		
		GeoLocationProvider glp = new GeoLocationProvider(this);
		
		Location myLocation = glp.getLocation();
		if (myLocation != null) {
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
	
			mMap.moveCamera(center);
			mMap.animateCamera(zoom);
		} else {
			Log.e("CheckinActivity", "location is null");
		}
	}

    @Override
    public void onResume() {
        super.onResume();
        centerOnMyLocation();

        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    	Log.e("PghRecycles", "got new intent");
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        /*Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        mInfoText.setText(new String(msg.getRecords()[0].getPayload()));*/
    	//Log.e("PghRecycles", "processing.. new intent");
    	CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);
    	cb1.setChecked(true);
    	CheckBox cb2 = (CheckBox) findViewById(R.id.checkBox2);
    	cb2.setChecked(true);
    }	
}
