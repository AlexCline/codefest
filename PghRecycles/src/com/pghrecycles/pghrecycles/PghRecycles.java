package com.pghrecycles.pghrecycles;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.PickupInfo;
import com.pghrecycles.pghrecycles.data.providers.DBDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.DBHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.DBPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.GeoLocationProvider;
import com.pghrecycles.pghrecycles.data.providers.DivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.HolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.MockDivisionInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.MockHolidayListProvider;
import com.pghrecycles.pghrecycles.data.providers.MockPickupInfoProvider;
import com.pghrecycles.pghrecycles.data.providers.PickupInfoProvider;
import com.pghrecycles.pghrecycles.model.PickupDateModel;
import com.pghrecycles.pghrecycles.notification.Notifier;

public class PghRecycles extends Activity {

	PickupInfoProvider pickupInfoProvider;
	DivisionInfoProvider divisionInfoProvider;
	HolidayListProvider holidayListProvider;
	
	PickupDateModel mPickupDateModel;
	PendingIntent pendingIntent;
//	private static NfcAdapter mAdapter;
//	private static PendingIntent mPendingIntent;
//	private static IntentFilter[] mFilters;
//	private static String[][] mTechLists;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pgh_recycles);
		
		
		
		/* NFC STUFF HERE */
		//mAdapter = NfcAdapter.getDefaultAdapter(this);
//		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
//				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
//		try {
//			ndef.addDataType("*/*");
//		} catch (MalformedMimeTypeException e) {
//			throw new RuntimeException("fail", e);
//		}
//		mFilters = new IntentFilter[] { ndef, };
//		mTechLists = new String[][] { new String[] { TagTechnology.class.getName() } };

	   
		
		
		
		mPickupDateModel = new PickupDateModel(new MockPickupInfoProvider(), new MockDivisionInfoProvider(), new MockHolidayListProvider());
		
		// initialize button listener
		// TODO move this out of activity creation?
		final Button button = (Button) findViewById(R.id.buttonDoLookup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

                // do lookup for pickup
            	PickupInfo pickupInfo = pickupInfoProvider.getPickupInfo(locationInfo, now);
            	int pickupDay = pickupInfo.getDay();
            	((EditText)findViewById(R.id.editTextResults)).setText(pickupDay+"");
            	
            	// do lookup for division
            	Division division = pickupInfo.getDivision();
            	int year = pickupInfo.getYear();
            	Time time = new Time();
//            	time.set(0, 0, year);  // we only care about the year?
            	time.setToNow();
            	
            	
            	Log.e("PghRecycles", "pickup info day: " + pickupDay + " division: " + pickupInfo.getDivision());

//            	HolidayList holidayList = mPickupDateModel.getHolidayList(now);
            	HolidayList holidayList = holidayListProvider.getHolidayList(now);
//        		DivisionInfo divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), now);
            	DivisionInfo divisionInfo = divisionInfoProvider.getDivisionInfo(division, now);
            	
            	TextView nextPickupDateView = (TextView)findViewById(R.id.next_pickup_date);
            	final Time nextRefusePickupDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, now).getDate(); 
            	nextPickupDateView.setText(nextRefusePickupDate.format3339(true));
            	
            	TextView nextRecycleDateView = (TextView)findViewById(R.id.next_recycle_date);
            	final Time nextRecyclingPickupDate = mPickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, now).getDate(); 
            	nextRecycleDateView.setText(nextRecyclingPickupDate.format3339(true));
            	
            	TextView nextYardDebrisDateView = (TextView)findViewById(R.id.next_yard_debris_date);
            	final Time nextYardDebrisPickupDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, now).getDate(); 
            	nextYardDebrisDateView.setText(nextYardDebrisPickupDate.format3339(true));
            	

        		mHandler.postDelayed(new Runnable() {

        			@Override
        			public void run() {
        				Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_recycle_ticker), 
        						getResources().getString(R.string.notification_recycle_title), 
        						String.format(getResources().getString(R.string.notification_recycle_content), nextRecyclingPickupDate.format("%A")));
        			}
        			
        		}, new Random().nextInt(5000)+1000);

        	
        		mHandler.postDelayed(new Runnable() {

        			@Override
        			public void run() {
        				Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_garbage_ticker), 
        						getResources().getString(R.string.notification_garbage_title), 
        						String.format(getResources().getString(R.string.notification_garbage_content),  nextRefusePickupDate.format("%A")));
        			}
        			
        		}, new Random().nextInt(10000)+10000);

        		mHandler.postDelayed(new Runnable() {

        			@Override
        			public void run() {
        				Notifier.notifyRealtime(PghRecycles.this, getResources().getString(R.string.notification_debris_ticker), 
        						getResources().getString(R.string.notification_debris_title), 
        						String.format(getResources().getString(R.string.notification_debris_content), nextYardDebrisPickupDate.format("%B%e")));
        			}
        			

        		}, new Random().nextInt(60000)+30000);      
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
	
	Handler mHandler = new Handler();

	private void test() {
		
		Time currentDate = new Time();
		currentDate.set(23,2-1,2013);
		currentDate.normalize(true);
		PickupInfo pickupInfo = mPickupDateModel.getPickupInfo(new LocationInfo(), currentDate);
		HolidayList holidayList = mPickupDateModel.getHolidayList(currentDate);
		DivisionInfo divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		PickupDate nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(24,2-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(25,2-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(26,2-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(27,2-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(28,2-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(1,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.set(2,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(3,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(4,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(5,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(6,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(7,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(8,3-1,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(1,0,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);

		currentDate.set(4,6,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);		
		
		currentDate.set(24,11,2013);
		currentDate.normalize(true);
		holidayList = mPickupDateModel.getHolidayList(currentDate);
		divisionInfo = mPickupDateModel.getDisivionInfo(pickupInfo.getDivision(), currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		
		currentDate.setToNow();
		currentDate.normalize(true);		
		PickupDate nextYardDebrisDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		
		currentDate.set(18,4,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);

		currentDate.set(19,4,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);		
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		currentDate.set(9,10,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);		
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}

		currentDate.set(10,10,2013);
		currentDate.normalize(true);		
		nextYardDebrisDate = mPickupDateModel.getNextYardDebrisPickupDate(divisionInfo, currentDate);
		if (nextYardDebrisDate != null) {
			Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next yard date: " + nextYardDebrisDate.getDate().format3339(true));
		}
		
		currentDate.setToNow();
		currentDate.normalize(true);
		PickupDate nextRecycleDate = mPickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
		currentDate.set(1,0,2013);
		currentDate.normalize(true);				
		nextRecycleDate = mPickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
		currentDate.set(31,11,2013);
		currentDate.normalize(true);				
		nextRecycleDate = mPickupDateModel.getNextRecyclingPickupDate(pickupInfo, holidayList, divisionInfo, currentDate);
		nextRefuseDate = mPickupDateModel.getNextRefusePickupDate(pickupInfo, holidayList, currentDate);
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next pickup date: " + nextRefuseDate.getDate().format3339(true));
		Log.e("PghRecycles", " current date: " + currentDate.format3339(true) + " next recyle date: " + nextRecycleDate.getDate().format3339(true));
		
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
		divisionInfoProvider = new DBDivisionInfoProvider(this);
		holidayListProvider = new DBHolidayListProvider(this);
	}
//	
//	public void onResume() {
//		super.onResume();
//		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
//				mTechLists);
//	}

//	@Override
//	public void onNewIntent(Intent intent) {
//		Log.e("PghRecycles", "got new intent");
//		//resolveIntent(intent);
//
//	}

//	@Override
//	public void onPause() {
//		super.onPause();
//		mAdapter.disableForegroundDispatch(this);
//	}
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Check to see that the Activity started due to an Android Beam
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            processIntent(getIntent());
//        }
//    }
//
//    @Override
//    public void onNewIntent(Intent intent) {
//    	Log.e("PghRecycles", "got new intent");
//        // onResume gets called after this to handle the intent
//        setIntent(intent);
//    }
//
//    /**
//     * Parses the NDEF Message from the intent and prints to the TextView
//     */
//    void processIntent(Intent intent) {
//        /*Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
//                NfcAdapter.EXTRA_NDEF_MESSAGES);
//        // only one message sent during the beam
//        NdefMessage msg = (NdefMessage) rawMsgs[0];
//        // record 0 contains the MIME type, record 1 is the AAR, if present
//        mInfoText.setText(new String(msg.getRecords()[0].getPayload()));*/
//    	Log.e("PghRecycles", "processing.. new intent");
//    }
}
