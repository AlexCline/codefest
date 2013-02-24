package com.pghrecycles.pghrecycles.data.providers;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.RecyclingSchedule;
import com.pghrecycles.pghrecycles.data.YardDebrisSchedule;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class DBDivisionInfoProvider implements DivisionInfoProvider {
	
	// table names
	public static final String DATABASE_TABLE_DIVISION = "division";
	public static final String DATABASE_TABLE_RECYCLE_WEEK = "recycle_week";
	public static final String DATABASE_TABLE_YARD_DEBRIS_SCHED = "yard_debris_sched";
	public static final String DATABASE_TABLE_YARD_DEBRIS_PICKUP_DATE = "yard_debris_pickup_date";
	// col names
	public static final String DATABASE_COL_DIVISION = "division";
	public static final String DATABASE_COL_YEAR = "year";
	public static final String DATABASE_COL_DATE = "date";
	public static final String DATABASE_COL_NAME = "name";
	public static final String DATABASE_COL_START_WEEK = "start_week";
	
	// information about the global application environment
	private static Context context;
	// private DatabaseOpener dbOpener;
	private DataBaseHelper dbOpener;
	private SQLiteDatabase db;
	
	public DBDivisionInfoProvider(Context context) {
		this.context = context;
		dbOpener = DataBaseHelper.getDBHelper(context);
		db = dbOpener.getReadableDatabase();
	}
	
	@Override
	public DivisionInfo getDivisionInfo(Division division, Time year) {
		
		int divisionID = division.ordinal();
		int yearID = year.year;

//		// (don't need holiday for division?) TABLE holiday ( year INTEGER, name STRING, date INTEGER );
//		TABLE yard_debris_pickup_date ( id INTEGER PRIMARY KEY, date INTEGER );
//		TABLE yard_debris_sched ( division INTEGER, date INTEGER, year INTEGER);
//		TABLE recycle_week ( division INTEGER, start_week INTEGER, year INTEGER);
		
//		TABLE division ( year INTEGER, name STRING , id INTEGER PRIMARY KEY);
		
//		public DivisionInfo(Division division, String name,
//				YardDebrisSchedule yardDebrisSchedule,
//				RecyclingSchedule recyclingSchedule) {
		
		// first, build YardDebrisSchedule, RecyclingSchedule
		String selectYardDebrisSched = "SELECT * FROM "+DATABASE_TABLE_YARD_DEBRIS_SCHED+" WHERE division = '"+divisionID+"' AND year = '"+yearID+"'";
		Log.v(this.getClass().getName(), "query: "+selectYardDebrisSched);
		Cursor resultsYardDebrisSched = db.rawQuery(selectYardDebrisSched, null);
		YardDebrisSchedule yardDebrisSchedule = new YardDebrisSchedule();
		
		try {
			resultsYardDebrisSched.moveToFirst();
			int numResults = resultsYardDebrisSched.getCount();
			if (numResults <= 0) {
				// no results returned; return empty PickupInfo object
				Log.e(this.getClass().getName(),
						"no yardDebrisSchedResults for query: "
								+ selectYardDebrisSched);
			}
			// record each date, store
			int colDateIndex = resultsYardDebrisSched.getColumnIndex(DATABASE_COL_DATE); 
			for (int r=0; r<numResults; r++) {
				int date = resultsYardDebrisSched.getInt(colDateIndex);
				Time dateTime = new Time();
				dateTime.set(date*1000);
				dateTime.normalize(true);
				yardDebrisSchedule.addPickupDate(new PickupDate(dateTime));
				
				resultsYardDebrisSched.moveToNext();
			}
		
		} catch (CursorIndexOutOfBoundsException e) {
			// no results returned; return empty PickupInfo object
			Log.e(this.getClass().getName(), "no yardDebrisSchedResults for query: "+selectYardDebrisSched);
		}
		
		// get recycling info
		RecyclingSchedule recyclingSchedule = new RecyclingSchedule();
		String selectRecycleWeek = "SELECT * FROM "+DATABASE_TABLE_RECYCLE_WEEK+" WHERE division = '"+divisionID+"' AND year = '"+yearID+"'";
		Log.v(this.getClass().getName(), "query: "+selectYardDebrisSched);
		Cursor resultsRecycleWeek = db.rawQuery(selectRecycleWeek, null);
		
		int startWeek = 0;
		try {
			resultsRecycleWeek.moveToFirst();
			int numResults = resultsRecycleWeek.getCount();
			if (numResults <= 0) {
				// no results returned; return empty PickupInfo object
				Log.e(this.getClass().getName(),"no recycleWeek for query: "+ selectRecycleWeek);
			}
			startWeek = resultsRecycleWeek.getColumnIndex(DATABASE_COL_START_WEEK);
			
		} catch (CursorIndexOutOfBoundsException e) {
			// no results returned; return empty PickupInfo object
			Log.e(this.getClass().getName(), "no recycleWeek for query: "+selectRecycleWeek);
		}
		
		recyclingSchedule.setStartWeek(startWeek);
		
		// build overall DivisionInfo
		DivisionInfo divisionInfo = new DivisionInfo();
		divisionInfo.setName(division.name());
		divisionInfo.setYardDebrisSchedule(yardDebrisSchedule);
		divisionInfo.setRecyclingSchedule(recyclingSchedule);
		divisionInfo.setDivision(division);
		
		return divisionInfo;
		
	}
	
	
	
	
	
}
