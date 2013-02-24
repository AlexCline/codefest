package com.pghrecycles.pghrecycles.data.providers;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.Holiday;
import com.pghrecycles.pghrecycles.data.HolidayList;

public class DBHolidayListProvider implements HolidayListProvider {

	//TABLE holiday ( year INTEGER, name STRING, date INTEGER );
	// table names
	public static final String DATABASE_TABLE_HOLIDAY = "holiday";
	// col names
	public static final String DATABASE_COL_YEAR = "year";
	public static final String DATABASE_COL_NAME = "name";
	public static final String DATABASE_COL_DATE = "date";

	// information about the global application environment
	private static Context context;
	// private DatabaseOpener dbOpener;
	private DataBaseHelper dbOpener;
	private SQLiteDatabase db;
	
	public DBHolidayListProvider(Context context) {
		DBHolidayListProvider.context = context;
		dbOpener = DataBaseHelper.getDBHelper(context);
		db = dbOpener.getReadableDatabase();
	}
	
	@Override
	public HolidayList getHolidayList(Time year) {
		
		// given year, get list of holidays
		int yearInt = year.year;
		
		String selectHoliday = "SELECT * FROM "+DATABASE_TABLE_HOLIDAY+" WHERE "+DATABASE_COL_YEAR+" = "+yearInt;
		Cursor resultsHoliday = db.rawQuery(selectHoliday, null);
		HolidayList holidayList = new HolidayList();
		
		try {
			resultsHoliday.moveToFirst();
			int numResults = resultsHoliday.getCount();
			if (numResults <= 0) {
				// no holidays returned
				Log.e(this.getClass().getName(),
						"no holidays for query: "
								+ selectHoliday);
			}
			
			// iterate over all holidays, return each
			int colHolidayName = resultsHoliday.getColumnIndex(DATABASE_COL_NAME);
			int colHolidayDate = resultsHoliday.getColumnIndex(DATABASE_COL_DATE);
			for (int r=0; r<numResults; r++) {
				int dateEpoch = resultsHoliday.getInt(colHolidayDate);
				String name = resultsHoliday.getString(colHolidayName);
				Time time = new Time();
				time.set((long)dateEpoch * (long)1000);
				time.normalize(true);
				Holiday holiday = new Holiday(name, time);
				holidayList.add(holiday);
				resultsHoliday.moveToNext();
			}
			
		} catch (CursorIndexOutOfBoundsException e) {
			// no holidays returned 
			Log.e(this.getClass().getName(), "no holidays for query: "+selectHoliday);
		}
		
		return holidayList;
		
	}

}
