package com.pghrecycles.pghrecycles;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;

public class CheckInActivity extends Activity {
	private static NfcAdapter mAdapter;
	private GoogleMap mMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);
		
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
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
//            Intent intent = new Intent(this, PghRecycles.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        	this.finish();
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
    	Log.e("PghRecycles", "processing.. new intent");
    }	
}
